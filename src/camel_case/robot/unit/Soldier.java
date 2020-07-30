package camel_case.robot.unit;

import aic2020.user.UnitController;
import aic2020.user.UnitType;

public class Soldier extends Unit {
  public Soldier(UnitController uc) {
    super(uc, UnitType.SOLDIER);
  }

  @Override
  public void run() {
    if (tryAttackClosestEnemy()) {
      return;
    }

    tryMoveRandom();
  }
}
