package camel_case.robot.unit;

import aic2020.user.Location;
import aic2020.user.UnitController;
import aic2020.user.UnitType;
import camel_case.build.BuildOrder;
import camel_case.util.Locations;
import java.util.Comparator;
import java.util.List;

public class EssentialWorker extends Unit {
  public EssentialWorker(UnitController uc) {
    super(uc, UnitType.ESSENTIAL_WORKER);
  }

  @Override
  public void run() {
    if (tryCompleteBuildOrder()) {
      return;
    }

    tryMoveRandom();
  }

  private boolean tryCompleteBuildOrder() {
    List<BuildOrder> orders = buildQueue.getOrders();

    if (orders.size() == 0) {
      return false;
    }

    Location myLocation = uc.getLocation();
    orders.sort(Comparator.comparingInt(order -> order.getLocation().distanceSquared(myLocation)));

    BuildOrder activeOrder = null;
    int toiletPaper = uc.getToiletPaper();

    for (BuildOrder order : orders) {
      if (order.getType().getCost() > toiletPaper) {
        continue;
      }

      activeOrder = order;
      break;
    }

    if (activeOrder == null) {
      return false;
    }

    Location orderLocation = activeOrder.getLocation();

    if (Locations.equals(orderLocation, myLocation)) {
      tryMoveRandom();
      return true;
    }

    if (trySpawn(activeOrder.getType(), orderLocation)) {
      buildQueue.removeOrder(activeOrder);
      return true;
    }

    tryMoveTo(orderLocation);
    return true;
  }
}
