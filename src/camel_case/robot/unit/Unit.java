package camel_case.robot.unit;

import aic2020.user.UnitController;
import aic2020.user.UnitType;
import camel_case.robot.Robot;

public abstract class Unit extends Robot {
  public Unit(UnitController uc, UnitType type) {
    super(uc, type);
  }
}
