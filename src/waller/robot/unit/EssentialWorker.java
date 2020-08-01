package waller.robot.unit;

import aic2020.user.Location;
import aic2020.user.UnitController;
import java.util.List;
import waller.build.BuildOrder;
import waller.util.Locations;

public class EssentialWorker extends Unit {
  public EssentialWorker(UnitController uc) {
    super(uc);
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

    BuildOrder activeOrder = null;
    int bestCost = Integer.MAX_VALUE;

    int toiletPaper = uc.getToiletPaper();

    for (BuildOrder order : orders) {
      if (order.getType().getCost() > toiletPaper) {
        continue;
      }

      if (order.getType().getCost() < bestCost) {
        activeOrder = order;
        bestCost = order.getType().getCost();
      }
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
