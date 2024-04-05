package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
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
        this.edgeManager = new EdgeManager();
        this.mapgenerator = new Mapgenerator(this, isten);
        mapgenerator.generate(minRoomSize);

        printMap();
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
    @Override
    public void onUpdate(Isten isten, double deltaTime) {

        //for testing
        delta += deltaTime;
        if(delta > 5 && !merged) {
            //mergeRooms(rooms.get(0), rooms.get(0).getAdjacentRooms().get(0), isten);
            splitRooms(rooms.get(0), isten);
            System.out.println();
            System.out.println();
            //printMap();

            merged = true;
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
    private boolean splitRooms(Room r1, Isten isten)
    {
        // removeoljuk a szomszedos roomok szomszedossagi listaibol a szobat, es a func vegen hozzaadjuk a ket szetvalasztott szoba egyiket/mindekettot
        for(Room neighbourRoom : r1.getAdjacentRooms()){
            neighbourRoom.getAdjacentRooms().remove(r1);
        }
        //egyenlőre minden szoba ami splittel lesz createlve ilyen type-val rendelkezik
        //int newID = generateNewRoomID(); //már kész van, teszt miatt nincs hasznalva
        int newId = 999;
        Room newRoom = new Room(newId);
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
        if( kruskalAlgoImplementation(oldRoomWithoutNewRoom) && kruskalAlgoImplementation(addableUnitRooms)) {
            for (UnitRoom addUnitRoomToNewRoom : addableUnitRooms) {
                //kivesszük az előző szobából a  aunitroomot
                addUnitRoomToNewRoom.getOwnerRoom().getUnitRooms().remove(addUnitRoomToNewRoom);
                addUnitRoomToNewRoom.setOwnerRoom(newRoom);
                //hozzáadjuk az új szobához a unitroomot
                newRoom.getUnitRooms().add(addUnitRoomToNewRoom);
            }
            rooms.add(newRoom);
            newRoom.setAdjacentRooms();
            r1.setAdjacentRooms();

            //set the images
            for(UnitRoom unitRoom : newRoom.getUnitRooms()) {
                unitRoom.addRightImage(isten);
            }
            //update nodeRooms and generate the new ones
            //also updates the images and colliders
            edgeManager.updateEdgesAfterSplit(r1, newRoom, isten);
            return true;
        }
        return false;
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
    //nem biztos hogy így a legjobb
    private boolean wouldRoomBeCoherent(ArrayList<UnitRoom> newRoomUnits)
    {
        UnitRoom starterRoom = newRoomUnits.get(0);
        ArrayList<UnitRoom> coherentGraph = new ArrayList<>();
        coherentGraph.add(starterRoom);
        for(int i = 0;i<newRoomUnits.size();i++){
            for(UnitRoom unitRoomToBeAddedToGraph : newRoomUnits){
                 /*ha a size i-vel egyenlő, vagy kisebb nála, akkor tudjuk, hogy nem alkotnak összefüggő gráfot a UnitRoomok a Roomban,
                    mert különben az előző körhöz képest legalább 1-et fel kellett volna tuidjak venni, vagy pedig már előtte többet kellett volna tudjak felvenni,
                    rekurzív gondolat, mukodik (remelem)*/
                if(coherentGraph.size()> i)
                {
                    if (!unitRoomToBeAddedToGraph.equals(coherentGraph.get(i))
                            && !coherentGraph.contains(unitRoomToBeAddedToGraph)
                            && unitRoomToBeAddedToGraph.isAdjacent(coherentGraph.get(i)))
                    {
                        coherentGraph.add(unitRoomToBeAddedToGraph);
                        //break; ezzel valszeg effektivebb
                    }
                }
                else{
                    System.out.println("Nem lennenek koherensek a szobak");
                    return false;
                }
            }
        }
        //mivel minden indexen vegig tudtunk menni ezert tudunk truet returnolni, azert biztonsag kedveert meg egy kontrollt bennhagyok
        if(coherentGraph.size() == newRoomUnits.size()) return true;
            //hogyha nem egyenlok akkor false menjen ki, bar egyenloknek kene lenniuk
        else return false;
    }
    //elozo fv, vagyis wouldRoomBeCoherent atirasa generikusra, es akkor egy wouldMapBeCoherent fv-t is helyettesit.
    private<T extends Graph> boolean kruskalAlgoImplementation(ArrayList<T> newCoherentElements)
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
                        //break; ezzel valszeg effektivebb
                    }
                }
                else{
                    System.out.println("Nem lennenek koherensek a szobak");
                    return false;
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
        if(!r1.isAdjacent(r2) || r1.getID() == r2.getID()){
            System.err.println("cant be merged");
            return;
        }
        System.out.println(r2.getID() + "(r2) is merged to (r1)" + r1.getID());
        //remove r2 and keep r1;

        //set colliders

        edgeManager.deleteEdge(r1,r2,isten);
        edgeManager.updateEdgesAfterMerge(r1,r2);

        for(UnitRoom unitRoom : r2.getUnitRooms()){
            //r1.getUnitRooms().add(unitroom);
            unitRoom.setOwnerRoom(r1);

            //setting the new images of the deleted room
            //this method cares about the renderable items too
            unitRoom.addRightImage(isten);
        }

        r1.getUnitRooms().addAll(r2.getUnitRooms()); //insted of this: r1.getUnitRooms().add(unitroom);

        r1.getAdjacentRooms().remove(r2);
        r2.getAdjacentRooms().remove(r1);
        for(Room adj : r2.getAdjacentRooms()){
            if(!r1.getAdjacentRooms().contains(adj) && !adj.equals(r1)){
                //System.out.println("adjroom added in r1: " + adj.getID());
                r1.getAdjacentRooms().add(adj);
            }
            adj.getAdjacentRooms().remove(r2);
            if(!adj.getAdjacentRooms().contains(r1)) {
                adj.getAdjacentRooms().add(r1);
            }
        }

        r2.getAdjacentRooms().clear();
        r2.getUnitRooms().clear();

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
