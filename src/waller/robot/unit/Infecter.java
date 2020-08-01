package waller.robot.unit;

import aic2020.user.UnitController;

public class Infecter extends Unit {
  public Infecter(UnitController uc) {
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
