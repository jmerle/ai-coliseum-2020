package camel_case.robot.structure;

import aic2020.user.Direction;
import aic2020.user.UnitController;
import aic2020.user.UnitInfo;
import aic2020.user.UnitType;

public class Base extends Structure {
  public Base(UnitController uc) {
    super(uc, UnitType.BASE);
  }

  @Override
  public void run() {
    if (tryAttackClosestEnemy()) {
      return;
    }

    UnitType requiredUnit = getRequiredUnit();
    if (requiredUnit == null) {
      return;
    }

    for (Direction direction : adjacentDirections) {
      if (trySpawn(requiredUnit, direction)) {
        return;
      }
    }
  }

  private UnitType getRequiredUnit() {
    if (countNearbyFriendlies(UnitType.ESSENTIAL_WORKER) < 3) {
      return UnitType.ESSENTIAL_WORKER;
    }

    if (countNearbyFriendlies(UnitType.FUMIGATOR) < 3) {
      return UnitType.FUMIGATOR;
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
