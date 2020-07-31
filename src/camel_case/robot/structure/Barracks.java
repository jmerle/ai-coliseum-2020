package camel_case.robot.structure;

import aic2020.user.Direction;
import aic2020.user.UnitController;
import aic2020.user.UnitType;

public class Barracks extends Structure {
  public Barracks(UnitController uc) {
    super(uc, UnitType.BARRACKS);
  }

  @Override
  public void run() {
    for (Direction direction : adjacentDirections) {
      if (trySpawn(spawnableTypes.SOLDIER, direction)) {
        return;
      }
    }
  }
}
