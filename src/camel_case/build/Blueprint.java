package camel_case.build;

import aic2020.user.Location;
import aic2020.user.UnitController;
import camel_case.type.TypeWrapper;
import camel_case.util.Locations;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Blueprint {
  private UnitController uc;

  private Map<Integer, TypeWrapper> structures = new HashMap<>();

  public Blueprint(UnitController uc) {
    this.uc = uc;
  }

  public void addStructure(Location location, TypeWrapper orderableType) {
    structures.put(Locations.toInt(location), orderableType);
  }

  public void dispatchOrders(BuildQueue buildQueue) {
    for (Entry<Integer, TypeWrapper> entry : structures.entrySet()) {
      Location location = Locations.fromInt(entry.getKey());

      if (uc.senseFarmAtLocation(location) == null && uc.senseUnitAtLocation(location) == null) {
        buildQueue.addOrder(location, entry.getValue());
      }
    }
  }
}
