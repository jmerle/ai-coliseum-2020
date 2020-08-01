package camel_case.robot.structure;

import aic2020.user.Direction;
import aic2020.user.UnitController;

public class Barracks extends Structure {
  public Barracks(UnitController uc) {
    super(uc);
  }

  @Override
  public void run() {
    for (Direction direction : adjacentDirections) {
      if (trySpawn(wrapperTypes.SOLDIER, direction)) {
        return;
      }
    }
  }
}
