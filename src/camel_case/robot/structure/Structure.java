package camel_case.robot.structure;

import aic2020.user.UnitController;
import aic2020.user.UnitType;
import camel_case.robot.Robot;

public abstract class Structure extends Robot {
  public Structure(UnitController uc, UnitType type) {
    super(uc, type);
  }
}
