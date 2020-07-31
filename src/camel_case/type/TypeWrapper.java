package camel_case.type;

import aic2020.user.UnitType;

public class TypeWrapper {
  private int id;
  private UnitType unitType;
  private int cost;

  public TypeWrapper(int id, UnitType unitType, int cost) {
    this.id = id;
    this.unitType = unitType;
    this.cost = cost;
  }

  public TypeWrapper(int id, UnitType unitType) {
    this(id, unitType, unitType.cost);
  }

  public TypeWrapper(int id, int cost) {
    this(id, null, cost);
  }

  public int getId() {
    return id;
  }

  public UnitType getUnitType() {
    return unitType;
  }

  public int getCost() {
    return cost;
  }
}
