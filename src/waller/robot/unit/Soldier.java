package waller.robot.unit;

import aic2020.user.UnitController;

public class Soldier extends Unit {
  public Soldier(UnitController uc) {
    super(uc);
  }

  @Override
  public void run() {
    if (tryAttackClosestEnemy()) {
      return;
    }

    tryMoveRandom();
  }
}
