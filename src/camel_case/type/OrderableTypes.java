package camel_case.type;

import aic2020.user.UnitType;

public class OrderableTypes {
  private final int MARKET_ID = 1;
  private final int BARRACKS_ID = 2;
  private final int LABORATORY_ID = 3;
  private final int HOSPITAL_ID = 4;
  private final int FARM_ID = 5;
  private final int BARRICADE_ID = 6;

  public final TypeWrapper MARKET = new TypeWrapper(MARKET_ID, UnitType.MARKET);
  public final TypeWrapper BARRACKS = new TypeWrapper(BARRACKS_ID, UnitType.BARRACKS);
  public final TypeWrapper LABORATORY = new TypeWrapper(LABORATORY_ID, UnitType.LABORATORY);
  public final TypeWrapper HOSPITAL = new TypeWrapper(HOSPITAL_ID, UnitType.HOSPITAL);
  public final TypeWrapper FARM = new TypeWrapper(FARM_ID, 10);
  public final TypeWrapper BARRICADE = new TypeWrapper(BARRICADE_ID, UnitType.BARRICADE);

  public TypeWrapper fromId(int id) {
    if (id == MARKET_ID) {
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
    }

    return null;
  }
}
