package camel_case.build;

import aic2020.user.Location;
import camel_case.type.TypeWrapper;
import camel_case.type.WrapperTypes;

public class BuildOrder {
  private Location location;
  private TypeWrapper type;

  public BuildOrder(Location location, TypeWrapper type) {
    this.location = location;
    this.type = type;
  }

  public Location getLocation() {
    return location;
  }

  public TypeWrapper getType() {
    return type;
  }

  public int serialize() {
    String str = type.getId() + padCoordinate(location.x) + padCoordinate(location.y);
    return Integer.parseInt(str);
  }

  public static BuildOrder fromSerialized(int serialized, WrapperTypes wrapperTypes) {
    String str = Integer.toString(serialized);

    int id = Integer.parseInt(str.substring(0, 1));
    int x = Integer.parseInt(str.substring(1, 5));
    int y = Integer.parseInt(str.substring(5, 9));

    return new BuildOrder(new Location(x, y), wrapperTypes.fromId(id));
  }

  private String padCoordinate(int coordinate) {
    String str = Integer.toString(coordinate);
    return new String(new char[4 - str.length()]).replace('\0', '0') + str;
  }
}
