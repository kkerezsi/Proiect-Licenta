package base;

import bwapi.Game;
import bwapi.Player;

public class BaseClass {
	protected Player _self;
	protected Game _game;
	
	public void init(Game game ,Player playerAI){
		_self = playerAI;
		_game = game;
	}
}
