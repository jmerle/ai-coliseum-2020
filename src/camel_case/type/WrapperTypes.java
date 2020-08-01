package camel_case.type;

import aic2020.user.UnitType;

public class WrapperTypes {
  private final int ESSENTIAL_WORKER_ID = 8;
  private final int FUMIGATOR_ID = 9;
  private final int SOLDIER_ID = 10;
  private final int INFECTER_ID = 11;

  private final int BASE_ID = 1;
  private final int MARKET_ID = 2;
  private final int BARRACKS_ID = 3;
  private final int LABORATORY_ID = 4;
  private final int HOSPITAL_ID = 5;

  private final int FARM_ID = 6;
  private final int BARRICADE_ID = 7;

  private final int ZOMBIE_ID = 12;

  public final TypeWrapper ESSENTIAL_WORKER =
      new TypeWrapper(ESSENTIAL_WORKER_ID, UnitType.ESSENTIAL_WORKER);
  public final TypeWrapper FUMIGATOR = new TypeWrapper(FUMIGATOR_ID, UnitType.FUMIGATOR);
  public final TypeWrapper SOLDIER = new TypeWrapper(SOLDIER_ID, UnitType.SOLDIER);
  public final TypeWrapper INFECTER = new TypeWrapper(INFECTER_ID, UnitType.INFECTER);

  public final TypeWrapper BASE = new TypeWrapper(BASE_ID, UnitType.BASE);
  public final TypeWrapper MARKET = new TypeWrapper(MARKET_ID, UnitType.MARKET);
  public final TypeWrapper BARRACKS = new TypeWrapper(BARRACKS_ID, UnitType.BARRACKS);
  public final TypeWrapper LABORATORY = new TypeWrapper(LABORATORY_ID, UnitType.LABORATORY);
  public final TypeWrapper HOSPITAL = new TypeWrapper(HOSPITAL_ID, UnitType.HOSPITAL);

  public final TypeWrapper FARM = new TypeWrapper(FARM_ID, 10);
  public final TypeWrapper BARRICADE = new TypeWrapper(BARRICADE_ID, UnitType.BARRICADE);

  public final TypeWrapper ZOMBIE = new TypeWrapper(ZOMBIE_ID, UnitType.ZOMBIE);

  public TypeWrapper fromId(int id) {
    if (id == ESSENTIAL_WORKER_ID) {
      return ESSENTIAL_WORKER;
    } else if (id == FUMIGATOR_ID) {
      return FUMIGATOR;
    } else if (id == SOLDIER_ID) {
      return SOLDIER;
    } else if (id == INFECTER_ID) {
      return INFECTER;
    } else if (id == BASE_ID) {
      return BASE;
    } else if (id == MARKET_ID) {
      return MARKET;
    } else if (id == BARRACKS_ID) {
      return BARRACKS;
    } else if (id == LABORATORY_ID) {
      return LABORATORY;
    } else if (id == HOSPITAL_ID) {
      return HOSPITAL;
    } else if (id == FARM_ID) {
      return FARM;
    } else if (id == BARRICADE_ID) {
      return BARRICADE;
    } else if (id == ZOMBIE_ID) {
      return ZOMBIE;
    }

    throw new IllegalArgumentException("Invalid id: " + id);
  }
}
