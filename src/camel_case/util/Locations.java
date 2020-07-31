package camel_case.util;

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
}
