package waller.robot.unit;

import aic2020.user.UnitController;

public class Fumigator extends Unit {
  public Fumigator(UnitController uc) {
    super(uc);
  }

  @Override
  public void run() {
    tryMoveRandom();
  }
}
