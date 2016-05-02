package terran.pack;

import base.BaseClass;

/**
 * Created by Alex on 4/30/2016.
 */
public class TerranBuildingAnalyzer extends BaseClass {

    private static TerranBuildingAnalyzer _instance;

    public static TerranBuildingAnalyzer getInstance(){
        if(_instance == null){
            _instance = new TerranBuildingAnalyzer();
        }

        return _instance;
    }

    private TerranBuildingAnalyzer(){
    }

    public void runAnalyzer(){

        if(TerranSupplyDepot.getInstance().shouldBuild()) {
            TerranSupplyDepot.getInstance().canBuild();
        }

        if(TerranRefinery.getInstance().shouldBuild()){
            TerranRefinery.getInstance().canBuild();
        }

        if(TerranCommandCenter.getInstance().shouldBuild()){
            TerranCommandCenter.getInstance().canBuild();
        }

        if(TerranBarracks.getInstance().shouldBuild()){
            TerranBarracks.getInstance().canBuild();
        }
    }
}
