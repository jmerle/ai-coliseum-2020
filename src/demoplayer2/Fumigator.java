package demoplayer2;

import aic2020.user.UnitController;

public class Fumigator extends MyUnitClass {
  Fumigator(UnitController uc) {
    super(uc);
  }

  void play() {
    moveRandomly();
  }
}
