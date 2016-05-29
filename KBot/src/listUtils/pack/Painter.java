package listUtils.pack;
import base.BaseClass;
import base.TimeManager;
import base.Tuple;
import bwapi.*;
import bwapi.CoordinateType.Enum;
import bwta.BWTA;
import bwta.BaseLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Painter extends BaseClass {

	private final int timeConsumptionLeftOffset = 575;
	private final int timeConsumptionTopOffset = 30;
	private final int timeConsumptionBarMaxWidth = 50;
	private final int timeConsumptionBarHeight = 14;
	private final int timeConsumptionYInterval = 16;


	private List<Position> tileDraws = new ArrayList<>();

	private static Painter _instance;
	
	private Painter(){
	}
	
	public static Painter getInstance(){
		if(_instance == null){
			_instance = new Painter();
		}
		
		return _instance;
	}
	
	public void paintTimeConsumption() {
		int counter = 0;
		double maxValue = ListUtils.getMaxElement(CodeProfiler.getAspectsTimeConsumption()
				.values());
				
		// System.out.println(TimeMeasurer.getAspectsTimeConsumption().keySet().size());
		for (String aspectTitle : CodeProfiler.getAspectsTimeConsumption().keySet()) {
			int x = timeConsumptionLeftOffset;
			int y = timeConsumptionTopOffset + timeConsumptionYInterval * counter++;

			int value = CodeProfiler.getAspectsTimeConsumption().get(aspectTitle).intValue();

			// Draw aspect time consumption bar
			int barWidth = (int) (timeConsumptionBarMaxWidth * value / maxValue);
			
			if (barWidth < 3) {
				barWidth = 3;
			}
			if (barWidth > timeConsumptionBarMaxWidth) {
				barWidth = timeConsumptionBarMaxWidth;
			}

			_game.drawBox( bwapi.CoordinateType.Enum.Screen, x, y, x + timeConsumptionBarMaxWidth, y + timeConsumptionBarHeight, Color.White, true);
			_game.drawBox( bwapi.CoordinateType.Enum.Screen, x, y, x + barWidth, y + timeConsumptionBarHeight, Color.Red, true);
			
			_game.drawTextScreen(x, y, aspectTitle);
		}
	}
	
	public void paintGameTime(){
        Tuple<Long, Long> currentTime = TimeManager.getInstance().getTimeDifference(_game);
        _game.drawTextScreen(10, 10, currentTime.toString());
	}
	
	public void paintBaseLocations(){
    	for (BaseLocation base : BWTA.getBaseLocations()) {
			_game.drawTextMap(base.getX(), base.getY(), base.getPoint().toTilePosition().toString());
		}
	}
	
	public void paintAll(){
		paintTimeConsumption();
		paintBaseLocations();
		paintGameTime();
		drawTile();
	}

	public void addTile(int x, int y){
		tileDraws.add(new Position(x,y));
	}

	public void drawTile() {
		for(Position p: tileDraws)
			_game.drawBox(Enum.Map, p.getX() ,p.getY(),p.getX() + 32, p.getY() + 32,Color.Green);
	}

	public void clearTiles(){
		tileDraws.clear();
	}


	public void paintUnit(Unit unit) {
		_game.drawTextMap(unit.getX(), unit.getY(), unit.getPoint().toTilePosition().toString());
	}

	public void paintPosition(TilePosition tile) {
		_game.drawTextMap(tile.getX(), tile.getY(), tile.toString());
	}
}
