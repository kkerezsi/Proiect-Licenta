package listUtils.pack;

import builder.pack.BuildOrder;
import bwapi.UnitType;
import resource.pack.CompleteResourceModel;
import resource.pack.ResourceCoordinator;

/**
 * Created by Alex on 4/30/2016.
 */
public class BuildUtils {

    public static boolean canGenericBuild(UnitType buildingType){
        ResourceCoordinator resourceCoord = ResourceCoordinator.getInstance();

        CompleteResourceModel mineralsAndGasRequired = resourceCoord.getRequirementsForType(buildingType);
        CompleteResourceModel myResources = resourceCoord.getMyResources();

        if(mineralsAndGasRequired != null
                && mineralsAndGasRequired.getMinerals() <= myResources.getMinerals()
                && mineralsAndGasRequired.getGas() <= myResources.getGas()){


            return true;
        }

        return false;
    }
}
