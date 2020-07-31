package camel_case.robot.unit;

import aic2020.user.Direction;
import aic2020.user.Location;
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
    Location hq = uc.getInitialLocation(myTeam);

    for (int i = 0; i < 10; i++) {
      Direction direction = adjacentDirections[BetterRandom.nextInt(adjacentDirections.length)];

      Location newLocation = uc.getLocation().add(direction);
      if (newLocation.distanceSquared(hq) > UnitType.BASE.visionRange) {
        continue;
      }

      if (tryMove(direction)) {
        return true;
      }
    }

    return false;
  }
}
