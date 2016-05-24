package builder.pack;

import base.BaseClass;
import bwapi.*;
import constants.pack.Requirements;
import listUtils.pack.ListUtils;
import listUtils.pack.Painter;

import java.util.ArrayList;
import java.util.List;

public class BuildLogicsCoordinator extends BaseClass {
    private static BuildLogicsCoordinator _instance;

    private BuildLogicsCoordinator() {
    }

    public static BuildLogicsCoordinator getInstance() {
        if (_instance == null) {
            _instance = new BuildLogicsCoordinator();
        }

        return _instance;
    }

    public TilePosition determineSpecialBuildPosition(UnitType unit) {
        if (unit.isRefinery()) {
            List<Unit> geysers = _game.getGeysers();
            List<Unit> bases = ListUtils.getAllUnitsByType(UnitType.Terran_Command_Center, _self);

            for (Unit commandCenter : bases) {
                Unit nearestGeyser = ListUtils.getUnitNearestFromList(commandCenter.getX(), commandCenter.getY(), geysers, true, false);

                if (nearestGeyser != null)
                    return nearestGeyser.getTilePosition();

                return null;
            }
        }

        if (unit == UnitType.Terran_Bunker) {

        }

        if (unit == UnitType.Terran_Command_Center) {

        }

        return null;
    }

    public TilePosition determineBuildPosition(UnitType unitType) {
        Unit base = ListUtils.getRandomBase();

        if(base != null)
            return getLegitTileToBuildNear(unitType,base.getTilePosition(),6, Requirements.MAX_SEARCH_RANGE);

        return null;
    }

    public TilePosition getLegitTileToBuildNear(UnitType buildingType, TilePosition point, int minSearchRadius,
                                                       int maxSearchRadius) {
        UnitType type = buildingType;

        //boolean isSpecialBuilding = type.isBase() || type.isBunker() || type.isRefinery();

        // =========================================================
        // Try to find possible place to build starting in given point and
        // gradually increasing search radius
        int currSearchRadius = minSearchRadius;

        while (currSearchRadius <= maxSearchRadius) {
            int minTileX = point.getX() - currSearchRadius;
            int maxTileX = point.getX() + currSearchRadius;
            int minTileY = point.getY() - currSearchRadius;
            int maxTileY = point.getY() + currSearchRadius;

            for (int tileX = minTileX; tileX <= maxTileX; tileX += 1) {

                // Leave space on some rows/columns so unit can have corridors
                if (tileX % 7 == 0)
                    continue;

                for (int tileY = minTileY; tileY <= maxTileY; tileY += 1) {
                    // Leave space on some rows/columns so unit can have
                    // corridors
                    if (tileY % 5 == 0)
                        continue;

                    // Check if it's possible and reasonable to build this type
                    // of building in this place
                    TilePosition position = shouldBuildHere(type, tileX, tileY);
                    if (position != null) {

                        return position;
                    }
                }

                currSearchRadius += 1;
            }
        }

        return null;
    }

    private TilePosition shouldBuildHere(UnitType type, int tileX, int tileY) {
        boolean isBase = type == UnitType.Terran_Command_Center;
        // boolean isDepot = type.isSupplyDepot();

        boolean skipCheckingIsFreeFromUnits = false;
        boolean skipCheckingRegion = _game.elapsedTime() > 380 || isBase || type == UnitType.Terran_Bunker
                || type == UnitType.Terran_Missile_Turret || type.isAddon();

        int x = tileX * 32;
        int y = tileY * 32;

        Painter.getInstance().addTile(x,y);

        TilePosition place = new TilePosition(tileX, tileY);

        // Is it physically possible to build here?
        if (canBuildAt(place, type)) {

            // If it's possible to build here, now check whether it
            // makes sense. If it's not in stupid place or colliding
            // etc.
            Unit builderUnit = ListUtils.getNearestWorkerTo(place);
            if (builderUnit != null
                    && (skipCheckingIsFreeFromUnits || isBuildTileFreeFromUnits(type,
                    builderUnit.getID(), tileX, tileY))) {

                // We should avoid building place:
                // - between workers and minerals
                // - right next to another building
                // - in the place where next base should be built
                // - too close to a chokepoint (passage problem)
                // - that are in other region (wrong neighborhood)

                if (!isBase && type != UnitType.Terran_Bunker && isTooNearMineralsOrGeyser(type, place)) {
                    return null;
                }

                if (!isBase && !isEnoughPlaceToOtherBuildings(place, type)) {
                    return null;
                }

                if (!isBase && isOverlappingNextBase(place, type)) {
                    return null;
                }

                if (!isBase && isTooCloseToAnyChokePoint(place)) {
                    return null;
                }

                if (!isBase && !skipCheckingRegion && !isInAllowedRegions(place)) {
                    return null;
                }

                else {
                    return place;
                }
            }
        }

        // No place has been found, return null.
        return null;
    }

    private boolean isInAllowedRegions(TilePosition place) {
        return true;
    }

    private boolean isTooCloseToAnyChokePoint(TilePosition place) {
        return false;
    }

    private boolean isOverlappingNextBase(TilePosition place, UnitType type) {
        return false;
    }

    private boolean isEnoughPlaceToOtherBuildings(TilePosition place, UnitType type) {
        // Base and refinery can be placed anywhere
        if (type == UnitType.Terran_Command_Center || type == UnitType.Resource_Vespene_Geyser) {
            return true;
        }

        // Define buildings that are near this build tile
        ArrayList<Unit> buildingsNearby = ListUtils.getUnitsInRadius(place, 8, ListUtils.getUnitsBuildings(_self));

        for (Unit otherBuilding : buildingsNearby) {

            Unit builder = ListUtils.getNearestWorkerTo(place);
            if (wouldNewBuildingCollideWith(place, type, otherBuilding, builder)) {
                return false;
            }
            // }
        }

        // No building collides, allow this building location
        return true;
    }

    private boolean wouldNewBuildingCollideWith(TilePosition buildingPlace,
                                                       UnitType buildingType, Unit existingBuilding, Unit builder) {

        // Consider space for add-ons
        if (buildingType.canBuildAddon() || existingBuilding.getType().canBuildAddon()) {
            if (!isPhysicallyPossibleToBuildAt(builder, new TilePosition(buildingPlace.getX() + 64, buildingPlace.getX()),
                    buildingType)) {
                return true;
            }
        }

        return false;
    }

    public boolean isPhysicallyPossibleToBuildAt(Unit builder, TilePosition point, UnitType type) {
        return _game.canBuildHere(point,type);
    }

    private boolean isTooNearMineralsOrGeyser(UnitType type, TilePosition place) {
        return false;
    }

    public boolean canBuildAt(TilePosition point, UnitType type) {
        Unit builder = ListUtils.getNearestWorkerTo(point);
        if (builder == null || point == null) {
            return false;
        }

        // builder.getID(),
        return _game.canBuildHere(point.getPoint(), type);
    }

    public boolean isBuildTileFreeFromUnits(UnitType type, int builderID, int tileX,
                                                   int tileY) {
        TilePosition point = new TilePosition((tileX) * 32, (tileY) * 32);

        double buildingApproxDimension = type.tileWidth() + 0.5;

        // Check if units are blocking this tile
        boolean unitsInWay = false;
        for (Unit u : _game.getAllUnits()) {
            if (u.getID() == builderID) {
                continue;
            }
            if (ListUtils.getDistanceBetween(u.getX(),u.getY(),point.getX(), point.getY()) <= buildingApproxDimension) {
                unitsInWay = true;
            }
        }

        return !unitsInWay;
    }

}
