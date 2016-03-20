package unit.pack;

import java.util.ArrayList;

import base.BaseClass;
import bwapi.*;

public class WorkerCoordinator extends BaseClass {
	private static WorkerCoordinator _instance;
	private ArrayList<Unit> workerList;
	
	//base miners
	private ArrayList<ArrayList<Unit>> _baseMiners;
	
	//base extractors
	private ArrayList<ArrayList<Unit>> _baseExtractors;
	
	//base builders
	private ArrayList<ArrayList<Unit>> _baseBuilders;
	
	private WorkerCoordinator(){
		workerList = new ArrayList<Unit>();
	}
	
	public static WorkerCoordinator getInstance(){
		if(_instance == null){
			_instance = new WorkerCoordinator();
		}
		
		return _instance;
	}
	
	public void registerWorker(Unit worker){
		if(isWorkerContained(worker)){
			return;
		}
		
		workerList.add(worker);
	}
	
	public boolean isWorkerContained(Unit worker) {
		return workerList.contains(worker);
	}
	
	public void manageWorkers(){
	}
	
	public void manageWorker(Unit worker){
		if(worker.isIdle()){
			
		}
	}

	public ArrayList<ArrayList<Unit>> get_baseMiners() {
		return _baseMiners;
	}

	public void set_baseMiners(ArrayList<ArrayList<Unit>> _baseMiners) {
		this._baseMiners = _baseMiners;
	}

	public ArrayList<ArrayList<Unit>> get_baseExtractors() {
		return _baseExtractors;
	}

	public void set_baseExtractors(ArrayList<ArrayList<Unit>> _baseExtractors) {
		this._baseExtractors = _baseExtractors;
	}

	public ArrayList<ArrayList<Unit>> get_baseBuilders() {
		return _baseBuilders;
	}

	public void set_baseBuilders(ArrayList<ArrayList<Unit>> _baseBuilders) {
		this._baseBuilders = _baseBuilders;
	}
}
