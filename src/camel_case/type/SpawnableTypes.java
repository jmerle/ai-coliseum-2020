package camel_case.type;

import aic2020.user.UnitType;

public class SpawnableTypes extends OrderableTypes {
  private final int ESSENTIAL_WORKER_ID = 7;
  private final int FUMIGATOR_ID = 8;
  private final int SOLDIER_ID = 9;
  private final int INFECTER_ID = 10;

  public final TypeWrapper ESSENTIAL_WORKER =
      new TypeWrapper(ESSENTIAL_WORKER_ID, UnitType.ESSENTIAL_WORKER);
  public final TypeWrapper FUMIGATOR = new TypeWrapper(FUMIGATOR_ID, UnitType.FUMIGATOR);
  public final TypeWrapper SOLDIER = new TypeWrapper(SOLDIER_ID, UnitType.SOLDIER);
  public final TypeWrapper INFECTER = new TypeWrapper(INFECTER_ID, UnitType.INFECTER);

  @Override
  public TypeWrapper fromId(int id) {
    if (id == ESSENTIAL_WORKER_ID) {
      return ESSENTIAL_WORKER;
    } else if (id == FUMIGATOR_ID) {
      return FUMIGATOR;
    } else if (id == SOLDIER_ID) {
      return SOLDIER;
    } else if (id == INFECTER_ID) {
      return INFECTER;
    }

    return super.fromId(id);
  }
}
