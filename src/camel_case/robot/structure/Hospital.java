package camel_case.robot.structure;

import aic2020.user.UnitController;
import aic2020.user.UnitType;

public class Hospital extends Structure {
  public Hospital(UnitController uc) {
    super(uc, UnitType.HOSPITAL);
  }

  @Override
  public void run() {
    // TODO: Implement
  }
}
