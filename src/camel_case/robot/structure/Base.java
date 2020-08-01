package camel_case.robot.structure;

import aic2020.user.Direction;
import aic2020.user.Location;
import aic2020.user.UnitController;
import aic2020.user.UnitInfo;
import aic2020.user.UnitType;
import camel_case.build.Blueprint;
import camel_case.build.MapAnalyzer;
import camel_case.type.TypeWrapper;
import camel_case.util.Locations;

public class Base extends Structure {
  private Blueprint blueprint;

  public Base(UnitController uc) {
    super(uc);
  }

  @Override
  public void run() {
    if (blueprint == null) {
      blueprint = createBlueprint();
    }

    blueprint.dispatchOrders(buildQueue);

    if (tryAttackClosestEnemy()) {
      return;
    }

    TypeWrapper requiredUnit = getRequiredUnit();
    if (requiredUnit == null) {
      return;
    }

    requiredUnit = spawnableTypes.ESSENTIAL_WORKER;

    for (Direction direction : adjacentDirections) {
      if (trySpawn(requiredUnit, direction)) {
        return;
      }
    }
  }

  private Blueprint createBlueprint() {
    Blueprint blueprint = new Blueprint(uc);
    MapAnalyzer analyzer = new MapAnalyzer(uc);

    for (int[] offset : offsets.getRingOffsets(2)) {
      Location structureLocation = Locations.applyOffset(myLocation, offset);

      if (analyzer.isBuildable(structureLocation)) {
        blueprint.addStructure(structureLocation, orderableTypes.BARRICADE);
      }
    }

    return blueprint;
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
