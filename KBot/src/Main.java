import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import listUtils.pack.CodeProfiler;
import listUtils.pack.ListUtils;
import listUtils.pack.Painter;
import resource.pack.ResourceCoordinator;
import terran.pack.TerranSupplyDepot;
import unit.pack.*;
import base.TimeManager;
import base.Tuple;
import builder.pack.*;

public class Main extends DefaultBWListener {
    private Mirror mirror = new Mirror();
    private Game game;
    private Player self;
    
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

        int i = 0;

        ListUtils._game = game;
        ListUtils._self = self;
        
        Worker.getInstance().init(game, self);
        WorkerCoordinator.getInstance().init(game, self);
        Painter.getInstance().init(game,self);
        BuilderCoordinator.getInstance().init(game,self);
        BuilderSupplyCoordinator.getInstance().init(game, self);
        ResourceCoordinator.getInstance().init(game, self);

        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
    }

    @Override
    public void onFrame() {
    	
    	CodeProfiler.startMeasuring("Builder");
        BuilderCoordinator.getInstance().runCoordinator();
        CodeProfiler.endMeasuring("Builder");
        
        CodeProfiler.startMeasuring("Supply");
        BuilderSupplyCoordinator.getInstance().runCoordinator(game, self);
        CodeProfiler.endMeasuring("Supply");
        
    	CodeProfiler.startMeasuring("Worker");
    	Worker.getInstance().runWorkers();
    	CodeProfiler.endMeasuring("Worker");
    	
        Painter.getInstance().paintAll();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}