package camel_case.robot.unit;

import aic2020.user.Direction;
import aic2020.user.Location;
import aic2020.user.UnitController;
import aic2020.user.UnitType;
import camel_case.robot.Robot;
import camel_case.util.BetterRandom;
import camel_case.util.Locations;

// The bug moving in this class has been adapted from TheDuck314's Battlecode 2015 code
// Source: https://github.com/TheDuck314/battlecode2015/blob/master/teams/zephyr26_final/Nav.java

public abstract class Unit extends Robot {
  private final int BUG_STATE_DIRECT = 0;
  private final int BUG_STATE_BUG = 1;

  private final int WALL_SIDE_LEFT = 0;
  private final int WALL_SIDE_RIGHT = 1;

  protected Location bugTarget;

  private int bugState;
  private int bugWallSide = WALL_SIDE_LEFT;

  private int bugStartDistSq;

  private Direction bugLastMoveDir;
  private Direction bugLookStartDir;

  private int bugRotationCount;
  private int bugMovesSinceSeenObstacle = 0;

  public Unit(UnitController uc) {
    super(uc);
  }

  protected boolean tryMove(Direction direction) {
    if (uc.canMove(direction)) {
      if (bugTarget != null) {
        drawLine(bugTarget, colors.YELLOW);
      }

      uc.move(direction);
      myLocation = uc.getLocation();

      return true;
    }

    return false;
  }

  protected boolean tryMoveRandom() {
    bugTarget = null;

    Location hq = uc.getInitialLocation(myTeam);

    for (int i = 0; i < 10; i++) {
      Direction direction = adjacentDirections[BetterRandom.nextInt(adjacentDirections.length)];

      Location newLocation = myLocation.add(direction);
      if (newLocation.distanceSquared(hq) > UnitType.BASE.visionRange) {
        continue;
      }

      if (tryMove(direction)) {
        return true;
      }
    }

    return false;
  }

  protected void tryMoveTo(Location target) {
    if (!Locations.equals(bugTarget, target)) {
      bugTarget = target;
      bugState = BUG_STATE_DIRECT;
    }

    if (Locations.equals(myLocation, target)) {
      return;
    }

    bugMove();
  }

  private void bugMove() {
    if (bugState == BUG_STATE_BUG) {
      if (canEndBug()) {
        bugState = BUG_STATE_DIRECT;
      }
    }

    if (bugState == BUG_STATE_DIRECT) {
      if (!tryMoveDirect()) {
        bugState = BUG_STATE_BUG;
        startBug();
      }
    }

    if (bugState == BUG_STATE_BUG) {
      bugTurn();
    }
  }

  private boolean canEndBug() {
    if (bugMovesSinceSeenObstacle >= 4) {
      return true;
    }

    return (bugRotationCount <= 0 || bugRotationCount >= 8)
        && myLocation.distanceSquared(bugTarget) <= bugStartDistSq;
  }

  private boolean tryMoveDirect() {
    Direction directionToTarget = directionTowards(bugTarget);

    if (tryMove(directionToTarget)) {
      return true;
    }

    Direction[] directions = new Direction[2];
    Direction dirLeft = directionToTarget.rotateLeft();
    Direction dirRight = directionToTarget.rotateRight();

    int distanceLeft = myLocation.add(dirLeft).distanceSquared(bugTarget);
    int distanceRight = myLocation.add(dirRight).distanceSquared(bugTarget);

    if (distanceLeft < distanceRight) {
      directions[0] = dirLeft;
      directions[1] = dirRight;
    } else {
      directions[0] = dirRight;
      directions[1] = dirLeft;
    }

    for (Direction direction : directions) {
      if (tryMove(direction)) {
        return true;
      }
    }

    return false;
  }

  private void startBug() {
    bugStartDistSq = myLocation.distanceSquared(bugTarget);
    bugLastMoveDir = directionTowards(bugTarget);
    bugLookStartDir = directionTowards(bugTarget);
    bugRotationCount = 0;
    bugMovesSinceSeenObstacle = 0;

    Direction leftTryDir = bugLastMoveDir.rotateLeft();
    for (int i = 0; i < 3; i++) {
      if (!uc.canMove(leftTryDir)) {
        leftTryDir = leftTryDir.rotateLeft();
      } else {
        break;
      }
    }

    Direction rightTryDir = bugLastMoveDir.rotateRight();
    for (int i = 0; i < 3; i++) {
      if (!uc.canMove(rightTryDir)) {
        rightTryDir = rightTryDir.rotateRight();
      } else {
        break;
      }
    }

    int distanceLeft = myLocation.distanceSquared(myLocation.add(leftTryDir));
    int distanceRight = myLocation.distanceSquared(myLocation.add(rightTryDir));

    if (distanceLeft < distanceRight) {
      bugWallSide = WALL_SIDE_RIGHT;
    } else {
      bugWallSide = WALL_SIDE_LEFT;
    }
  }

  private void bugTurn() {
    if (detectBugIntoEdge()) {
      reverseBugWallFollowDir();
    }

    Direction direction = findBugMoveDir();

    if (direction != null) {
      bugMove(direction);
    }
  }

  private void bugMove(Direction direction) {
    if (tryMove(direction)) {
      bugRotationCount += calculateBugRotation(direction);
      bugLastMoveDir = direction;

      if (bugWallSide == WALL_SIDE_LEFT) {
        bugLookStartDir = direction.rotateLeft().rotateLeft();
      } else {
        bugLookStartDir = direction.rotateRight().rotateRight();
      }
    }
  }

  private Direction findBugMoveDir() {
    bugMovesSinceSeenObstacle++;
    Direction direction = bugLookStartDir;

    for (int i = 8; i-- > 0; ) {
      if (uc.canMove(direction)) {
        return direction;
      }

      direction = bugWallSide == WALL_SIDE_LEFT ? direction.rotateRight() : direction.rotateLeft();
      bugMovesSinceSeenObstacle = 0;
    }

    return null;
  }

  private boolean detectBugIntoEdge() {
    if (bugWallSide == WALL_SIDE_LEFT) {
      return uc.isOutOfMap(myLocation.add(bugLastMoveDir.rotateLeft()));
    } else {
      return uc.isOutOfMap(myLocation.add(bugLastMoveDir.rotateRight()));
    }
  }

  private void reverseBugWallFollowDir() {
    bugWallSide = bugWallSide == WALL_SIDE_LEFT ? WALL_SIDE_RIGHT : WALL_SIDE_LEFT;
    startBug();
  }

  private int calculateBugRotation(Direction direction) {
    if (bugWallSide == WALL_SIDE_LEFT) {
      return numRightRotations(bugLookStartDir, direction)
          - numRightRotations(bugLookStartDir, bugLastMoveDir);
    } else {
      return numLeftRotations(bugLookStartDir, direction)
          - numLeftRotations(bugLookStartDir, bugLastMoveDir);
    }
  }

  private int numLeftRotations(Direction start, Direction end) {
    return (-end.ordinal() + start.ordinal() + 8) % 8;
  }

  private int numRightRotations(Direction start, Direction end) {
    return (end.ordinal() - start.ordinal() + 8) % 8;
  }
}
