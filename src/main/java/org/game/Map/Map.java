package main.java.org.game.Map;

import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.util.*;


public class Map extends Updatable {
    private Mapgenerator mapgenerator;
    ArrayList<Room> rooms;
    private UnitRoom[][] unitRooms;
    private final int mapRowSize;
    private final int mapColumnSize;
    private EdgeManager edgeManager;
    private final int minRoomSize;
    //private boolean

    @Override
    public void onStart(Isten isten) {
        this.edgeManager = new EdgeManager(isten);
        this.mapgenerator = new Mapgenerator(this, isten);
        mapgenerator.generate(minRoomSize);

        //printMap();
    }

    public Map(int rowNumber, int columnNumber, int minRoomSize){
        this.mapRowSize = rowNumber;
        this.mapColumnSize = columnNumber;
        this.minRoomSize = minRoomSize;
        //unitrooms is set in the generator --> onstart
        this.rooms = new ArrayList<>();
        initUnitRooms();

    }

    //for testing
    boolean merged = false;
    double delta = 0;
    int cnt = 0;
    int r = 0;
    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        //for testing
        delta += deltaTime;
        if (delta > 5) {

            //TESTCASE 1:::
            if(cnt%3==0){
                addDoorToEdgeWithoutDoor(isten);
                System.out.println("ajtoaddolas tortent");
            }
            else{
                TakeOutDoor(isten);
                System.out.println("ajtokivetel tortent");
            }
            //TESTCASE 2:
            //mergeRooms(rooms.get(0), rooms.get(0).getPhysicallyAdjacentRooms().get(0), isten);
            //TESTCASE 3:
            /*for(Room splittable : rooms){
                if(splitRooms(splittable,isten))
                {
                    System.out.println("sikerult a split");
                    break;
                }
            }*/
            cnt++;
            delta = 0;
        }
    }

    @Override
    public void onDestroy() {

    }
    public void initUnitRooms(){
        unitRooms = new UnitRoom[mapRowSize][mapColumnSize];
        for(int i = 0; i<mapRowSize;i++)
        {
            for(int j = 0;j<mapColumnSize;j++)
            {
                unitRooms[i][j] = new UnitRoom(new Vec2(j,i));
            }
        }
    }

    //csak akkor ha minden ajto nyitva van!!
    //a slitelesnel csak a minroomsize fele engedelyezett
    private boolean splitRooms(Room r1, Isten isten)
    {
        if(r1.getUnitRooms().size() < minRoomSize) return false;
        //egyenlőre minden szoba ami splittel lesz createlve ilyen type-val rendelkezik
        int newID = generateNewRoomID(); //már kész van, teszt miatt nincs hasznalva
        Room newRoom = new Room(newID);
        int lowestRowIdx = getRoomWithLowestRowIdx(r1);
        ArrayList<UnitRoom> addableUnitRooms = new ArrayList<>();
        int distance = 0;
        ArrayList<UnitRoom> UnitRoomsWithDistanceXFromLowestRow;
        //addig, amíg az új szoba a méret fele nem lesz
        while(addableUnitRooms.size()<r1.getUnitRooms().size()/2){
            UnitRoomsWithDistanceXFromLowestRow=getUnitRoomsWithXDistanceFromLowestRowIdxInOrderByColumn(r1,lowestRowIdx,distance++); //tavolsag novelese, es igy soronkent egyesevel balrol jobbra az osszes unitroom hozzaadasa, amig kell
            for(UnitRoom addableUnitRoom:UnitRoomsWithDistanceXFromLowestRow){
                if(addableUnitRooms.size()<r1.getUnitRooms().size()/2 )
                {
                    addableUnitRooms.add(addableUnitRoom);
                }

            }
        }
        ArrayList<UnitRoom> oldRoomWithoutNewRoom = getDifference(r1.getUnitRooms(),addableUnitRooms);
        //ellenorzom, hogy osszefuggoek lennének-e: ha igen:
        //id mindegy micsoda, itt igazabol nem hasznalom
        if( kruskalForCheckingIfGraphIsCoherent(oldRoomWithoutNewRoom) && kruskalForCheckingIfGraphIsCoherent(addableUnitRooms)) {
            // removeoljuk a szomszedos roomok szomszedossagi listaibol a szobat fizikalisan, es a func vegen hozzaadjuk a ket szetvalasztott szoba egyiket/mindekettot
            for(Room physicalNeighbourRoom : r1.getPhysicallyAdjacentRooms()){
                physicalNeighbourRoom.getPhysicallyAdjacentRooms().remove(r1);
            }
            //itt pedig az ajtos szomszeddossagi listabol is removeoljuk
            for(Room byDoorNeighbourRoom: r1.getDoorAdjacentRooms()){
                byDoorNeighbourRoom.getDoorAdjacentRooms().remove(r1);
            }
            for (UnitRoom addUnitRoomToNewRoom : addableUnitRooms) {
                //kivesszük az előző szobából a  unitroomot
                addUnitRoomToNewRoom.getOwnerRoom().getUnitRooms().remove(addUnitRoomToNewRoom);
                addUnitRoomToNewRoom.setOwnerRoom(newRoom);
                //hozzáadjuk az új szobához a unitroomot
                newRoom.getUnitRooms().add(addUnitRoomToNewRoom);
            }
            rooms.add(newRoom);
            newRoom.setPhysicallyAdjacentRooms();
            r1.setPhysicallyAdjacentRooms();

            //set the images
            for(UnitRoom unitRoom : newRoom.getUnitRooms()) {
                unitRoom.addRightImage(isten);
            }
            //update nodeRooms and generate the new ones
            //also updates the images and colliders
            edgeManager.updateEdgesAfterSplit(r1, newRoom);
            setByDoorAdjacentRooms(newRoom);
            setByDoorAdjacentRooms(r1);
            return true;
        }
        return false;
    }
    public void setByDoorAdjacentRooms(Room r){
        r.getDoorAdjacentRooms().clear();
        //tesztelesre meg jo lehet
        //ArrayList<EdgeBetweenRooms> edgesOfRoom = edgeManager.getAllEdgeForARoom(r1);
        //az ajtoban tarolhato, hogy egyiranyu-e, meg ezt nem tudjuk fixre
        for(Room adjacentRoom : r.getPhysicallyAdjacentRooms()){
            if(edgeManager.getEdgeBetweenRooms(r,adjacentRoom).hasDoor())
            {
                r.getDoorAdjacentRooms().add(adjacentRoom);
                adjacentRoom.getDoorAdjacentRooms().add(r);

            }

        }


    }


    public static ArrayList<UnitRoom> getDifference(ArrayList<UnitRoom> u1, ArrayList<UnitRoom> u2)
    {
        ArrayList<UnitRoom> difference = new ArrayList<>();
        for (UnitRoom element : u1) {
            if (!u2.contains(element)) {
                difference.add(element);
            }
        }
        return difference;
    }
    //elozo fv, vagyis wouldRoomBeCoherent atirasa generikusra, es akkor egy wouldMapBeCoherent fv-t is helyettesit.
    public <T extends Graph> boolean kruskalForCheckingIfGraphIsCoherent(ArrayList<T> newCoherentElements)
    {
        T starterRoom = newCoherentElements.get(0);
        ArrayList<T> coherentGraph = new ArrayList<>();
        coherentGraph.add(starterRoom);
        for(int i = 0;i<newCoherentElements.size();i++){
            for(T ElementToBeAddedToGraph : newCoherentElements){
                 /*ha a size i-vel egyenlő, vagy kisebb nála, akkor tudjuk, hogy nem alkotnak összefüggő gráfot a UnitRoomok a Roomban,
                    mert különben az előző körhöz képest legalább 1-et fel kellett volna tuidjak venni, vagy pedig már előtte többet kellett volna tudjak felvenni,
                    rekurzív gondolat, mukodik (remelem)*/
                if(coherentGraph.size()> i)
                {
                    if (!ElementToBeAddedToGraph.equals(coherentGraph.get(i))
                            && !coherentGraph.contains(ElementToBeAddedToGraph)
                            && ElementToBeAddedToGraph.isAdjacent(coherentGraph.get(i)))
                    {
                        coherentGraph.add(ElementToBeAddedToGraph);

                    }
                }
            }
        }
        //mivel minden indexen vegig tudtunk menni ezert tudunk truet returnolni, azert biztonsag kedveert meg egy kontrollt bennhagyok
        if(coherentGraph.size() == newCoherentElements.size()) return true;
            //hogyha nem egyenlok akkor false menjen ki, bar egyenloknek kene lenniuk
        else return false;
    }

    //function hogy megtalaljam a legkisebb sorindexet a tombben, viszonyitasi parameter lesz.
    // splitRooms func-on belül használva
    private int getRoomWithLowestRowIdx(Room r1) {
        UnitRoom min = unitRooms[mapRowSize-1][mapColumnSize-1];
        for(UnitRoom unitRoom : r1.getUnitRooms())
        {
            if(unitRoom.getRowNum()<min.getRowNum())
            {
                min = unitRoom;
            }
        }
        return min.getRowNum();
    }
    //function hogy megtalaljam azon UnitRoomokat, amik egy adott szamu soraban vannak a szobanaka alulrol nezve, amit a distance hataroz meg
    // splitRooms func-on belül használva
    private ArrayList<UnitRoom> getUnitRoomsWithXDistanceFromLowestRowIdxInOrderByColumn(Room r1, int lowestRowIdx, int distance) {
        ArrayList<UnitRoom> ret = new ArrayList<>();
        for(UnitRoom unitRoom: r1.getUnitRooms()){
            if(unitRoom.getRowNum()==lowestRowIdx+distance){
                ret.add(unitRoom);
            }
        }
        ret.sort(Comparator.comparing(UnitRoom::getColNum));
        return ret;
    }

    //ez a fv a mapgenerátorban is hasonlóan szerepel (colliderek és imagek nélkül)
    private void mergeRooms(Room r1, Room r2, Isten isten) {
        if(!r1.isPhysicallyAdjacent(r2) || r1.getID() == r2.getID()){
            System.err.println("cant be merged");
            return;
        }
        System.out.println(r2.getID() + "(r2) is merged to (r1)" + r1.getID());
        //remove r2 and keep r1;

        //set colliders

        edgeManager.deleteEdge(r1,r2);
        edgeManager.updateEdgesAfterMerge(r1,r2);

        for(UnitRoom unitRoom : r2.getUnitRooms()){
            //r1.getUnitRooms().add(unitroom);
            unitRoom.setOwnerRoom(r1);

            //setting the new images of the deleted room
            //this method cares about the renderable items too
            unitRoom.addRightImage(isten);
        }

        r1.getUnitRooms().addAll(r2.getUnitRooms()); //insted of this: r1.getUnitRooms().add(unitroom);

        //kiszedjuk a torolt szobat mindket szomszedossagi listabol
        r1.getPhysicallyAdjacentRooms().remove(r2);
        r1.getDoorAdjacentRooms().remove(r2);

        //vagy ez kell ide, vagy a feltetel, de igazabol mindegy, hadd maradjon  mind2,de a feltetel jobb,
        // mert a kikommentelt kodresz egyel lejjebb lehet errort dobna
        //r2.getThroughDoorAdjacentRooms().remove(r1);
        r2.getPhysicallyAdjacentRooms().remove(r1);
        //vegigiteralunk az r2 fizikalis szomszedossagi listajan
        for(Room adj : r2.getPhysicallyAdjacentRooms()){
            //ha r1 nem tartalmazza r2 fiz szomszedjat, akkor hozzaadjuk r1 listajahoz
            if(!r1.getPhysicallyAdjacentRooms().contains(adj) ){
                //System.out.println("adjroom added in r1: " + adj.getID());
                r1.getPhysicallyAdjacentRooms().add(adj);
            }
            //kivesszük magának r2 szomszédjának a fiz szomszédossági listájából r2-t
            adj.getPhysicallyAdjacentRooms().remove(r2);
            //vegul ha r1 nem tartalmazzaa a fiz szomszedossagi listajaban r2 szomszedjat, akkor r1 szomszedjava tesszuk
            if(!adj.getPhysicallyAdjacentRooms().contains(r1)) {
                adj.getPhysicallyAdjacentRooms().add(r1);
            }
        }
        //ugyanezt megcsinaljuk az ajton keresztuli szomszedossagra is.
        for(Room physicallyAdjacentRoom: r1.getPhysicallyAdjacentRooms()){
            if(edgeManager.getEdgeBetweenRooms(r1,physicallyAdjacentRoom).hasDoor()){
                if(!r1.getDoorAdjacentRooms().contains(physicallyAdjacentRoom))
                {
                    r1.getDoorAdjacentRooms().add(physicallyAdjacentRoom);
                }
                if(!physicallyAdjacentRoom.getDoorAdjacentRooms().contains(r1))
                {
                    physicallyAdjacentRoom.getDoorAdjacentRooms().add(r1);
                }

            }
        }
        // a folotti implementaciot haszanljuk most, de elvileg mindketto mukodik
        /*for(Room adj : r2.getDoorAdjacentRooms()){
            if(!r1.getDoorAdjacentRooms().contains(adj)){
                //System.out.println("adjroom added in r1: " + adj.getID());
                r1.getDoorAdjacentRooms().add(adj);
            }
            adj.getDoorAdjacentRooms().remove(r2);
            if(!adj.getDoorAdjacentRooms().contains(r1)) {
                adj.getDoorAdjacentRooms().add(r1);
            }
        }*/

        //r1.setDiscovered(r2.isDiscovered());
        //r1.setPlayerCount(r1.getPlayerCount() + r2.getPlayerCount());
        //r1.setRoomType(r2.getRoomType());
        r1.setMaxPlayerCount(r1.getMaxPlayerCount() + r2.getMaxPlayerCount());
        rooms.remove(r2);
    }

    //merge the rooms until every room has minimumm size of the given number
    private void printMap(){
        for(int i = 0;i < mapRowSize;i++){ //test
            for(int j = 0; j< mapColumnSize;j++){
                if(unitRooms[i][j].getOwnerRoom().getID() < 10) {
                    System.out.print(unitRooms[i][j].getOwnerRoom().getID() + "     ");
                }
                else if(unitRooms[i][j].getOwnerRoom().getID() >= 10 && unitRooms[i][j].getOwnerRoom().getID() < 100) {
                    System.out.print(unitRooms[i][j].getOwnerRoom().getID() + "    ");
                }
                else if(unitRooms[i][j].getOwnerRoom().getID() >= 100) {
                    System.out.print(unitRooms[i][j].getOwnerRoom().getID() + "   ");
                }
            }
            System.out.println();
            System.out.println();
        }
    }
    private int generateNewRoomID(){
        int newID = 0;
        while(true){
            int roomCnt = 0;
            for(Room room : rooms){
                if(room.getID() == newID){
                    newID++;
                    break; //not found, try the next ID
                }
                roomCnt++;
            }
            if(roomCnt == rooms.size()){
                return newID;
            }


        }
    }
    //egyenlore a fv-t hivom meg, és azon kívül választom meg, hogy melyik két edge között akarom a funcot meghívni, ez változhat
    public boolean TakeOutDoor(Isten isten){

        Collections.shuffle(edgeManager.getRoomEdges());
        //mindig nagyon ugyanonnan fog maajd ajtot kivenni
        for(EdgeBetweenRooms chosenEdge : edgeManager.getRoomEdges()){

            //csak akkor veszek ki ajtót, ha mindkét szobának ami között van az edge van legalább 2 szomszédja
            Room r1 = chosenEdge.getNodeRooms().get(0);
            Room r2 = chosenEdge.getNodeRooms().get(1);
            //ha van ajtaja
            if(chosenEdge.hasDoor()) {
                //csak akkor, ha van legalabb 2 ajtaja mindkettonekj, egyebkent mindenkepp szar lenne
                if (r1.getDoorAdjacentRooms().size() >= 2 &&
                        (r2.getDoorAdjacentRooms().size() >= 2)) {
                    r1.getDoorAdjacentRooms().remove(r2);
                    r2.getDoorAdjacentRooms().remove(r1);
                    //megnézem, hogy osszefuggo lenne-e az uj graph
                    if (kruskalForCheckingIfGraphIsCoherent(rooms)) {
                        EdgeBetweenRooms edgeBeingModified = edgeManager.getEdgeBetweenRooms(r1, r2);
                        ArrayList<EdgePiece> edgePieces = edgeBeingModified.getWalls();
                        for (EdgePiece edgePiece : edgePieces) {
                            if (edgePiece.isDoor()) {
                                edgeBeingModified.switchDoorToWall(edgePiece, isten);
                                r1.getDoorAdjacentRooms().remove(r2);
                                r2.getDoorAdjacentRooms().remove(r1);
                                System.out.println("talatam jot");
                                return true;
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Nem lett volna koherens");
                        r1.getDoorAdjacentRooms().add(r2);
                        r2.getDoorAdjacentRooms().add(r1);
                    }
                }
                else System.out.println("nem volt eleg ajto");
            }else System.out.println("nincs ajto itt");
        }
        return false;
    }
    //fv ami az ajtok hozzaadasat valositja meg
    public boolean addDoorToEdgeWithoutDoor(Isten isten){
        //vegigiteralok az eleken
        Collections.shuffle(edgeManager.getRoomEdges());
        for(EdgeBetweenRooms chosenEdge: edgeManager.getRoomEdges()){
            //csak olyan el erdekel, amin nincs ajto, ergo a ket szoba nem atjarhato
            if(!chosenEdge.hasDoor()){
                Room r1 = chosenEdge.getNodeRooms().get(0);
                Room r2 = chosenEdge.getNodeRooms().get(1);
                //vegigmegyek a falakon, mert elkepzelheto, hogy van olyan, amire nem illesztheto aajto
                for(EdgePiece chosenPiece: chosenEdge.getWalls()){
                    //ha tudok raa ajtot illeszteni, aakkor ezt megteszem
                    if(chosenEdge.switchWallToDoor(chosenPiece,isten))
                    {
                        //frissitem a szomszedossagi listakat
                        r1.getDoorAdjacentRooms().add(r2);
                        r2.getDoorAdjacentRooms().add(r1);
                        System.out.println("Ajto hozzaadva");
                        return true;
                    }
                }
            }
        }
        //ha nem tudtam ajtot hozzaadni, akkor teli a map
        System.out.println("Teli a map");
        return false;
    }


    public void setRooms(ArrayList<Room> rooms) {this.rooms = rooms;}

    public void setUnitRooms(UnitRoom[][] unitRooms) {
        this.unitRooms = unitRooms;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public UnitRoom[][] getUnitRooms() {
        return unitRooms;
    }

    public int getMapRowSize() {
        return mapRowSize;
    }

    public int getMapColumnSize() {
        return mapColumnSize;
    }
    public EdgeManager getEdgeManager(){ return edgeManager;}
}
