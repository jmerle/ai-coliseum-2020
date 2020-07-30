package camel_case;

import aic2020.user.UnitController;

public class UnitPlayer {
  public void run(UnitController uc) {
    //noinspection InfiniteLoopStatement
    while (true) {
      uc.yield();
    }
  }
}
