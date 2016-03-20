import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import unit.pack.*;

import attack.pack.*;
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

        Worker.getInstance().init(game, self);
        WorkerCoordinator.getInstance().init(game, self);
        
        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
        
        int i = 0;
        for(BaseLocation baseLocation : BWTA.getBaseLocations()){
        	System.out.println("Base location #" + (++i) +". Printing location's region polygon:");
        	for(Position position: baseLocation.getRegion().getPolygon().getPoints()){
        		System.out.print(position + ", ");
        	}
        	System.out.println();
        }
    }

    @Override
    public void onFrame() {
        AttackCoordinator.getInstance().runCoordinator(game, self);
        BuilderCoordinator.getInstance().runCoordinator(game,self);
        BuilderSupplyCoordinator.getInstance().runCoordinator(game, self);

        Tuple<Long, Long> currentTime = TimeManager.getInstance().getTimeDifference(game);
        game.drawTextScreen(10, 10, currentTime.toString());
        
    	for (BaseLocation base : BWTA.getBaseLocations()) {
			game.drawTextMap(base.getX(), base.getY(), base.getPoint().toTilePosition().toString());
		}
    	
    	Worker.getInstance().runWorkers(game,self);
    }

    public static void main(String[] args) {
        new Main().run();
    }
}