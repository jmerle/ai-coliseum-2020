package camel_case.robot;

import aic2020.user.Direction;
import aic2020.user.Location;
import aic2020.user.Team;
import aic2020.user.UnitController;
import aic2020.user.UnitInfo;
import aic2020.user.UnitType;
import camel_case.color.Color;
import camel_case.color.Colors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Robot {
  protected UnitController uc;

  protected UnitType me;

  protected Team myTeam;
  protected Team enemyTeam;

  protected Colors colors = new Colors();

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

  protected Map<UnitType, Integer> attackPriorities = new HashMap<>();

  public Robot(UnitController uc, UnitType type) {
    this.uc = uc;

    me = type;

    myTeam = uc.getTeam();
    enemyTeam = myTeam.getOpponent();

    attackPriorities.put(UnitType.BASE, 10);
    attackPriorities.put(UnitType.SOLDIER, 9);
    attackPriorities.put(UnitType.INFECTER, 8);
    attackPriorities.put(UnitType.ZOMBIE, 7);
    attackPriorities.put(UnitType.BARRACKS, 6);
    attackPriorities.put(UnitType.HOSPITAL, 5);
    attackPriorities.put(UnitType.BARRICADE, 4);
  }

  public abstract void run();

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
              int distance = uc.getLocation().distanceSquared(unit.getLocation());
              return -priority * 10000 + distance;
            }));

    for (UnitInfo unit : nearbyEnemies) {
      if (tryAttack(unit)) {
        return true;
      }
    }

    return false;
  }

  protected void drawLine(Location from, Location to, Color color) {
    uc.drawLine(from, to, color.getRed(), color.getGreen(), color.getBlue());
  }

  protected void drawLine(Location to, Color color) {
    drawLine(uc.getLocation(), to, color);
  }

  protected void drawPoint(Location location, Color color) {
    uc.drawPoint(location, color.getRed(), color.getGreen(), color.getBlue());
  }
}
