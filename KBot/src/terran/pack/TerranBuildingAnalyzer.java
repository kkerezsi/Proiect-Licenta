package terran.pack;

import base.BaseClass;
import builder.pack.BuildOrder;
import builder.pack.Building;

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
        if(BuildOrder.getInstance().peekNextBuilding() == null) {
            if (TerranSupplyDepot.getInstance().shouldBuild()) {
                TerranSupplyDepot.getInstance().canBuild();
            }

            else if (TerranBarracks.getInstance().shouldBuild()) {
                TerranBarracks.getInstance().canBuild();
            }

            else if (TerranRefinery.getInstance().shouldBuild()) {
                TerranRefinery.getInstance().canBuild();
            }

            else if (TerranCommandCenter.getInstance().shouldBuild()) {
                TerranCommandCenter.getInstance().canBuild();
            }
        }
    }
}
