package camel_case.robot.structure;

import aic2020.user.Direction;
import aic2020.user.Location;
import aic2020.user.UnitController;
import aic2020.user.UnitInfo;
import aic2020.user.UnitType;
import camel_case.type.TypeWrapper;
import camel_case.build.MapAnalyzer;

public class Base extends Structure {
  private boolean initialBuildOrdersDispatched = false;

  public Base(UnitController uc) {
    super(uc);
  }

  @Override
  public void run() {
    if (!initialBuildOrdersDispatched) {
      initialBuildOrdersDispatched = true;
      dispatchInitialBuildOrders();
    }

    if (tryAttackClosestEnemy()) {
      return;
    }

    TypeWrapper requiredUnit = getRequiredUnit();
    if (requiredUnit == null) {
      return;
    }

    for (Direction direction : adjacentDirections) {
      if (trySpawn(requiredUnit, direction)) {
        return;
      }
    }
  }

  private void dispatchInitialBuildOrders() {
    MapAnalyzer analyzer = new MapAnalyzer(uc);

    Location[] visibleLocations = uc.getVisibleLocations();
    for (Location location : visibleLocations) {
      if (analyzer.isBuildable(location)) {
        drawPoint(location, colors.RED);
      }
    }
  }

  private TypeWrapper getRequiredUnit() {
    if (countNearbyFriendlies(UnitType.ESSENTIAL_WORKER) < 1) {
      return spawnableTypes.ESSENTIAL_WORKER;
    }

    if (countNearbyFriendlies(UnitType.FUMIGATOR) < 2) {
      return spawnableTypes.FUMIGATOR;
    }

    return null;
  }

  private int countNearbyFriendlies(UnitType type) {
    UnitInfo[] units = uc.senseUnits(myTeam);
    int total = 0;

    for (UnitInfo unit : units) {
      if (unit.getType() == type) {
        total++;
      }
    }

    return total;
  }
}
