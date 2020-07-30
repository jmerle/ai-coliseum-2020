package camel_case.robot.structure;

import aic2020.user.Direction;
import aic2020.user.UnitController;
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

    for (Direction direction : adjacentDirections) {
      if (trySpawn(UnitType.FUMIGATOR, direction)) {
        return;
      }
    }
  }
}
