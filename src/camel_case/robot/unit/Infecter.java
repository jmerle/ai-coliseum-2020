package camel_case.robot.unit;

import aic2020.user.UnitController;
import aic2020.user.UnitType;

public class Infecter extends Unit {
  public Infecter(UnitController uc) {
    super(uc, UnitType.INFECTER);
  }

  @Override
  public void run() {
    if (tryAttackClosestEnemy()) {
      return;
    }
  }
}
