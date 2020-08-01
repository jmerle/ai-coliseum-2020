package waller.util;

import aic2020.user.Location;

public class Locations {
  public static int toInt(Location location) {
    return location.x * 10000 + location.y;
  }

  public static Location fromInt(int num) {
    int x = num / 10000;
    int y = num % 10000;
    return new Location(x, y);
  }

  public static Location applyOffset(Location location, int[] offset) {
    return location.add(offset[0], offset[1]);
  }

  public static boolean equals(Location a, Location b) {
    if (a == null && b == null) {
      return true;
    }

    if (a == null || b == null) {
      return false;
    }

    return a.x == b.x && a.y == b.y;
  }
}
