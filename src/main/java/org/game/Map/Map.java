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
    private  boolean isGenerated = false;

    public Map(Isten isten, int rowNumber, int columnNumber, int minRoomSize){
        this.mapRowSize = rowNumber;
        this.mapColumnSize = columnNumber;
        this.minRoomSize = minRoomSize;
        //unitrooms is set in the generator --> onstart
        this.rooms = new ArrayList<>();
        this.edgeManager = new EdgeManager(isten);
        initUnitRooms();
    }
    public void init(Isten isten) {
        isten.addUpdatable(edgeManager);
        this.mapgenerator = new Mapgenerator(this, isten);
        isGenerated = true;
        mapgenerator.generate(minRoomSize);
    }
    @Override
    public void onStart(Isten isten) {
        Mapgenerator mg = new Mapgenerator(this,isten);
        mg.generateSideWalls();
    }

    //for testing

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

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

    //a slitelesnel csak a minroomsize fele engedelyezett



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


    //BFS, ellenorzi hogy a graf osszefuggo-e, iranyitott graf eseten is
    //meg lehetne csinalni T parameterrel is, de akkor kellene egy getAdjacentrooms fv az interfaceba
    public  boolean isGraphKoherent(ArrayList<Room> graphNodes){
        Queue<Room> queue = new LinkedList<>();
        ArrayList<Room> visited = new ArrayList<>();
        //vegig kell menni az osszes csucsbol indulva
        for(Room startNode : graphNodes){
            queue.add(startNode);
            visited.add(startNode);
            while(!queue.isEmpty()){
                Room current = queue.poll();
                for(Room adjacent :current.getDoorAdjacentRooms()){
                    if(!visited.contains(adjacent)){
                        queue.add(adjacent);
                        visited.add(adjacent);
                    }
                }
            }
            if(graphNodes.size() != visited.size()) return false;
            visited.clear();
        }
        return true;
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


    public int getMinRoomSize() {
        return minRoomSize;
    }
}
