import action.pack.ActionQueue;
import attack.pack.AttackCoordinator;
import bwapi.*;
import bwta.BWTA;
import listUtils.pack.CodeProfiler;
import listUtils.pack.ListUtils;
import listUtils.pack.Painter;
import resource.pack.ResourceCoordinator;
import terran.pack.*;
import unit.pack.*;
import base.TimeManager;
import builder.pack.*;

public class Main extends DefaultBWListener {
    private Mirror mirror = new Mirror();
    private Game game;
    private Player self;


    private int frameCounter = 0;
    private int secondCounter = 0;

    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
        System.out.println("New unit " + unit.getType());
    }

    @Override
    public void onStart() {
        game = mirror.getGame();
        self = game.self();
        game.enableFlag(1);

        TimeManager.getInstance();

        ListUtils._game = game;
        ListUtils._self = self;

        // Register components
        Painter.getInstance().init(game,self);

        WorkerCoordinator.getInstance().init(game, self);
        BuilderCoordinator.getInstance().init(game,self);
        BuildLogicsCoordinator.getInstance().init(game,self);
        ActionQueue.getInstance().init(game,self);
        SupplyCoordinator.getInstance().init(game, self);
        ResourceCoordinator.getInstance().init(game, self);
        ScoutCoordinator.getInstance().init(game,self);

        TerranRefinery.getInstance().init(game,self);
        TerranSupplyDepot.getInstance().init(game,self);
        TerranBarracks.getInstance().init(game,self);
        TerranCommandCenter.getInstance().init(game,self);

        AttackCoordinator.getInstance().init(game,self);
        //End register

        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
    }

    @Override
    public void onFrame() {
        try {
            frameCounter = game.getFrameCount();
            secondCounter = frameCounter / 30;

            if(getFrames() % 13 == 0){
                CodeProfiler.startMeasuring("Builder");
                BuilderCoordinator.getInstance().runCoordinator();
                CodeProfiler.endMeasuring("Builder");
            }

            if(getFrames() % 9 == 0) {
                CodeProfiler.startMeasuring("Supply");
                SupplyCoordinator.getInstance().runCoordinator();
                CodeProfiler.endMeasuring("Supply");
            }

            if(getFrames() % 11 == 0) {
                CodeProfiler.startMeasuring("WorkerCoordinator");
                WorkerCoordinator.getInstance().runWorkers();
                CodeProfiler.endMeasuring("WorkerCoordinator");
            }

            if(getFrames() % 12 == 0){
                CodeProfiler.startMeasuring("BuildingAnalyzer");
                TerranBuildingAnalyzer.getInstance().runAnalyzer();
                CodeProfiler.endMeasuring("BuildingAnalyzer");
            }

            if(getFrames() % 8 == 0){
                CodeProfiler.startMeasuring("ActionQueue");
                ActionQueue.getInstance().executeActions();
                CodeProfiler.endMeasuring("ActionQueue");
            }

            if(getFrames() % 15 == 0){
                CodeProfiler.startMeasuring("ActionCoordinator");
                AttackCoordinator.getInstance().runCoordinator();
                CodeProfiler.endMeasuring("ActionCoordinator");
            }

            if(getFrames() % 17 == 0){
                CodeProfiler.startMeasuring("ScoutCoordinator");
                AttackCoordinator.getInstance().runCoordinator();
                CodeProfiler.endMeasuring("ScoutCoordinator");
            }

            Painter.getInstance().paintAll();
        }
        catch (Exception ex){
            System.out.println("---------------------");
            System.out.println(" Error occurred with message: " +  ex.getMessage() + "\n <___________> \n" + ex.getLocalizedMessage());
            ex.printStackTrace(System.out);
        }
    }

    public int getFrames() {
        return frameCounter;
    }

    public static void main(String[] args) {
        new Main().run();
    }
}