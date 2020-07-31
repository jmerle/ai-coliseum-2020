package camel_case.robot.structure;

import aic2020.user.Direction;
import aic2020.user.Location;
import aic2020.user.UnitController;
import aic2020.user.UnitInfo;
import aic2020.user.UnitType;
import camel_case.build.BuildOrder;
import camel_case.type.TypeWrapper;

public class Base extends Structure {
  public Base(UnitController uc) {
    super(uc, UnitType.BASE);
  }

  @Override
  public void run() {
    visualizeOrders();

    if (uc.getRound() == 5) {
      for (Direction direction : adjacentDirections) {
        Location orderLocation = uc.getLocation().add(direction);

        if (uc.isOutOfMap(orderLocation)) {
          continue;
        }

        buildQueue.addOrder(orderLocation, orderableTypes.BARRACKS);
        break;
      }
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

  private void visualizeOrders() {
    for (BuildOrder order : buildQueue.getOrders()) {
      drawPoint(order.getLocation(), colors.WHITE);
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
