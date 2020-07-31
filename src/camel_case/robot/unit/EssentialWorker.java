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

    uc.println("Orders: " + orders.size());

    Location myLocation = uc.getLocation();
    orders.sort(Comparator.comparingInt(order -> order.getLocation().distanceSquared(myLocation)));

    BuildOrder activeOrder = orders.get(0);
    Location orderLocation = activeOrder.getLocation();

    if (Locations.equals(orderLocation, myLocation)) {
      tryMoveRandom();
      uc.println("Moving at random");
      return true;
    }

    if (Locations.equals(orderLocation, currentTarget) && isStuck()) {
      if (orders.size() == 1) {
        return false;
      }

      activeOrder = orders.get(1);
      orderLocation = activeOrder.getLocation();
    }

    if (isAdjacentTo(orderLocation)
        && trySpawn(activeOrder.getType(), directionTowards(orderLocation))) {
      buildQueue.removeOrder(activeOrder);
      uc.println("Completed order");
      return true;
    }

    uc.println("Moving from " + myLocation + " to " + orderLocation);
    return tryMoveTo(orderLocation);
  }
}
