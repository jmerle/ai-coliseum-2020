package camel_case.robot.unit;

import aic2020.user.Direction;
import aic2020.user.UnitController;
import aic2020.user.UnitType;
import camel_case.robot.Robot;
import camel_case.util.BetterRandom;

public abstract class Unit extends Robot {
  public Unit(UnitController uc, UnitType type) {
    super(uc, type);
  }

  protected boolean tryMove(Direction direction) {
    if (uc.canMove(direction)) {
      uc.move(direction);
      return true;
    }

    return false;
  }

  protected boolean tryMoveRandom() {
    for (int i = 0; i < 10; i++) {
      Direction direction = adjacentDirections[BetterRandom.nextInt(adjacentDirections.length)];
      if (tryMove(direction)) {
        return true;
      }
    }

    return false;
  }
}
