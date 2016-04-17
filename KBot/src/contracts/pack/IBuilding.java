package contracts.pack;

import java.util.List;

import bwapi.Unit;

public interface IBuilding {

	boolean shouldBuild();
	
	boolean canBuild();
	
	void forceBuild();
	
	int getNumberOfThisType();
	
	List<Unit> getBuildingsOfThisType();
}
