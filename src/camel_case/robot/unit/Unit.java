package camel_case.robot.unit;

import aic2020.user.Direction;
import aic2020.user.Location;
import aic2020.user.UnitController;
import aic2020.user.UnitType;
import camel_case.robot.Robot;
import camel_case.util.BetterRandom;
import camel_case.util.Locations;

public abstract class Unit extends Robot {
  protected Location currentTarget;

  private boolean isBugMoving;
  private int distanceBeforeBugMoving;
  private boolean huggingLeftWall;
  private Location lastHuggedWall;

  private int distanceToTarget;
  private int turnsSpentMovingTowardsTarget;

  public Unit(UnitController uc, UnitType type) {
    super(uc, type);
  }

  protected boolean isStuck() {
    return turnsSpentMovingTowardsTarget > distanceToTarget * 1.5;
  }

  protected boolean tryMove(Direction direction) {
    if (uc.canMove(direction)) {
      if (currentTarget != null) {
        drawLine(currentTarget, colors.YELLOW);
      }

      uc.move(direction);
      return true;
    }

    return false;
  }

  protected boolean tryMoveRandom() {
    currentTarget = null;
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

  protected boolean tryMoveTo(Location target) {
    if (!Locations.equals(target, currentTarget)) {
      currentTarget = target;

      isBugMoving = false;
      lastHuggedWall = null;

      distanceToTarget = (int) Math.ceil(Math.sqrt(uc.getLocation().distanceSquared(target)));
      turnsSpentMovingTowardsTarget = 1;
    } else {
      turnsSpentMovingTowardsTarget++;
    }

    if (isBugMoving) {
      if (uc.getLocation().distanceSquared(currentTarget) < distanceBeforeBugMoving) {
        if (tryMove(directionTowards(currentTarget))) {
          isBugMoving = false;
          lastHuggedWall = null;
          return true;
        }
      }
    } else {
      if (tryMove(directionTowards(currentTarget))) {
        return true;
      } else {
        isBugMoving = true;
        distanceBeforeBugMoving = uc.getLocation().distanceSquared(currentTarget);

        determineBugMoveDirection();
      }
    }

    return makeBugMove(true);
  }

  private void determineBugMoveDirection() {
    Direction forward = directionTowards(currentTarget);

    Direction left = forward.rotateLeft();
    int leftDistance = Integer.MAX_VALUE;

    Direction right = forward.rotateRight();
    int rightDistance = Integer.MAX_VALUE;

    for (int i = 0; i < 8; i++) {
      if (uc.canMove(left)) {
        leftDistance = uc.getLocation().add(left).distanceSquared(currentTarget);
        break;
      }

      left = left.rotateLeft();
    }

    for (int i = 0; i < 8; i++) {
      if (uc.canMove(right)) {
        rightDistance = uc.getLocation().add(right).distanceSquared(currentTarget);
        break;
      }

      right = right.rotateRight();
    }

    if (leftDistance > rightDistance) {
      huggingLeftWall = true;
      lastHuggedWall = uc.getLocation().add(right.rotateLeft());
    } else {
      huggingLeftWall = false;
      lastHuggedWall = uc.getLocation().add(left.rotateRight());
    }
  }

  private boolean makeBugMove(boolean firstCall) {
    Direction currentDirection = directionTowards(lastHuggedWall);

    for (int i = 0; i < 8; i++) {
      if (huggingLeftWall) {
        currentDirection = currentDirection.rotateRight();
      } else {
        currentDirection = currentDirection.rotateLeft();
      }

      Location newLocation = uc.getLocation().add(currentDirection);

      if (firstCall && uc.isOutOfMap(newLocation)) {
        huggingLeftWall = !huggingLeftWall;
        return makeBugMove(false);
      }

      if (tryMove(currentDirection)) {
        return true;
      } else {
        lastHuggedWall = uc.getLocation().add(currentDirection);
      }
    }

    return false;
  }
}
