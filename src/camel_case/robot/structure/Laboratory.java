package camel_case.robot.structure;

import aic2020.user.UnitController;
import aic2020.user.UnitType;

public class Laboratory extends Structure {
  public Laboratory(UnitController uc) {
    super(uc, UnitType.LABORATORY);
  }

  @Override
  public void run() {
    // TODO: Implement
  }
}
