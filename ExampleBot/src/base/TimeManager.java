package base;

import java.util.Date;

import bwapi.Game;

public class TimeManager {
	private static TimeManager _instance;
	private Date _startTime;
    
    private int _currentFPS = 0;
    private int _fps = 0;
    private long _start = 0;
	
	private TimeManager(){
		set_startingPoint(new Date());
	}
	
	public static TimeManager getInstance(){
		if(_instance == null){
			_instance = new TimeManager();
		}
		
		return _instance;
	}
	
	public Tuple<Long,Long> getTimeDifference(Game game){
		long elapsedMinutes = game.elapsedTime() / 60;
		long elapsedSeconds = game.elapsedTime() % 60;
		
		return new Tuple<Long,Long>(elapsedMinutes,elapsedSeconds);
	}

	public Date get_startingPoint() {
		return _startTime;
	}

	public void set_startingPoint(Date _startingPoint) {
		this._startTime = _startingPoint;
	}
	
    public void tick() {
        _currentFPS++;
        if(System.currentTimeMillis() - _start >= 1000) {
            _fps = _currentFPS;
            _currentFPS = 0;
            _start = System.currentTimeMillis();
        }
    }
    
    public int getFPS() {
        return _fps;
    }
}
