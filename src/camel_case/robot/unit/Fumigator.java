package camel_case.robot.unit;

import aic2020.user.UnitController;
import aic2020.user.UnitType;

public class Fumigator extends Unit {
  public Fumigator(UnitController uc) {
    super(uc, UnitType.FUMIGATOR);
  }

  @Override
  public void run() {
    tryMoveRandom();
  }
}
