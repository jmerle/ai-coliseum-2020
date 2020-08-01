package waller.robot;

import aic2020.user.Direction;
import aic2020.user.Location;
import aic2020.user.Team;
import aic2020.user.UnitController;
import aic2020.user.UnitInfo;
import aic2020.user.UnitType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import waller.build.BuildQueue;
import waller.color.Color;
import waller.color.Colors;
import waller.type.TypeWrapper;
import waller.type.WrapperTypes;
import waller.util.Locations;
import waller.util.Offsets;

public abstract class Robot {
  protected UnitController uc;

  protected UnitType me;

  protected Team myTeam;
  protected Team enemyTeam;

  protected BuildQueue buildQueue;

  protected Map<UnitType, Integer> attackPriorities = new HashMap<>();

  protected Colors colors = new Colors();
  protected WrapperTypes wrapperTypes = new WrapperTypes();
  protected Offsets offsets = new Offsets();

  protected Direction[] adjacentDirections = {
    Direction.NORTH,
    Direction.EAST,
    Direction.SOUTH,
    Direction.WEST,
    Direction.NORTHEAST,
    Direction.SOUTHEAST,
    Direction.SOUTHWEST,
    Direction.NORTHWEST
  };

  protected Location myLocation;

  public Robot(UnitController uc) {
    this.uc = uc;

    me = uc.getType();

    myTeam = uc.getTeam();
    enemyTeam = myTeam.getOpponent();

    buildQueue = new BuildQueue(uc);

    attackPriorities.put(UnitType.BASE, 10);
    attackPriorities.put(UnitType.SOLDIER, 9);
    attackPriorities.put(UnitType.INFECTER, 8);
    attackPriorities.put(UnitType.ZOMBIE, 7);
    attackPriorities.put(UnitType.BARRACKS, 6);
    attackPriorities.put(UnitType.HOSPITAL, 5);
    attackPriorities.put(UnitType.BARRICADE, 4);
  }

  public void update() {
    buildQueue.update();
    myLocation = uc.getLocation();
  }

  public abstract void run();

  protected boolean trySpawn(TypeWrapper type, Direction direction) {
    if (type.getId() == wrapperTypes.FARM.getId()) {
      if (uc.canBuildFarm(direction)) {
        uc.buildFarm(direction);
        return true;
      }

      return false;
    }

    UnitType unitType = type.getUnitType();

    if (uc.canSpawn(unitType, direction)) {
      uc.spawn(unitType, direction);
      return true;
    }

    return false;
  }

  protected boolean trySpawn(TypeWrapper type, Location location) {
    return isAdjacentTo(location) && trySpawn(type, directionTowards(location));
  }

  protected boolean tryAttack(UnitInfo unit) {
    Location location = unit.getLocation();

    if (uc.canAttack(location)) {
      drawLine(location, colors.RED);
      uc.attack(location);
      return true;
    }

    return false;
  }

  protected boolean tryAttackClosestEnemy() {
    UnitInfo[] nearbyUnits = uc.senseUnits();
    List<UnitInfo> nearbyEnemies = new ArrayList<>(nearbyUnits.length);

    for (UnitInfo unit : nearbyUnits) {
      if (unit.getTeam() == myTeam) {
        continue;
      }

      UnitType type = unit.getType();

      if (type == UnitType.FUMIGATOR) {
        continue;
      }

      if (!attackPriorities.containsKey(type)) {
        continue;
      }

      nearbyEnemies.add(unit);
    }

    Arrays.sort(
        nearbyUnits,
        Comparator.comparingInt(
            unit -> {
              int priority = attackPriorities.getOrDefault(unit.getType(), Integer.MIN_VALUE);
              int distance = myLocation.distanceSquared(unit.getLocation());
              return -priority * 10000 + distance;
            }));

    for (UnitInfo unit : nearbyEnemies) {
      if (tryAttack(unit)) {
        return true;
      }
    }

    return false;
  }

  protected int countNearbyFriendlies(UnitType type) {
    UnitInfo[] units = uc.senseUnits(myTeam);
    int total = 0;

    for (UnitInfo unit : units) {
      if (unit.getType() == type) {
        total++;
      }
    }

    return total;
  }

  protected Direction directionTowards(Location from, Location to) {
    if (from.x < to.x && from.y < to.y) {
      return Direction.NORTHEAST;
    } else if (from.x < to.x && from.y > to.y) {
      return Direction.SOUTHEAST;
    } else if (from.x > to.x && from.y < to.y) {
      return Direction.NORTHWEST;
    } else if (from.x > to.x && from.y > to.y) {
      return Direction.SOUTHWEST;
    } else if (from.x < to.x) {
      return Direction.EAST;
    } else if (from.x > to.x) {
      return Direction.WEST;
    } else if (from.y < to.y) {
      return Direction.NORTH;
    } else if (from.y > to.y) {
      return Direction.SOUTH;
    } else {
      return Direction.ZERO;
    }
  }

  protected Direction directionTowards(Location to) {
    return directionTowards(myLocation, to);
  }

  protected boolean isAdjacentTo(Location a, Location b) {
    return !a.equals(b) && Locations.equals(a.add(directionTowards(b)), b);
  }

  protected boolean isAdjacentTo(Location location) {
    return isAdjacentTo(myLocation, location);
  }

  protected void drawLine(Location from, Location to, Color color) {
    uc.drawLine(from, to, color.getRed(), color.getGreen(), color.getBlue());
  }

  protected void drawLine(Location to, Color color) {
    drawLine(myLocation, to, color);
  }

  protected void drawPoint(Location location, Color color) {
    uc.drawPoint(location, color.getRed(), color.getGreen(), color.getBlue());
  }
}
