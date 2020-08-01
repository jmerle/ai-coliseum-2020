package waller.build;

import aic2020.user.Location;
import aic2020.user.UnitController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import waller.type.TypeWrapper;
import waller.type.WrapperTypes;
import waller.util.Locations;

public class BuildQueue {
  private final int COMM_OFFSET = 10000;
  private final WrapperTypes wrapperTypes = new WrapperTypes();

  private UnitController uc;
  private Map<Integer, BuildOrder> orders = new HashMap<>();

  private int lastReadIndex = -1;

  public BuildQueue(UnitController uc) {
    this.uc = uc;
  }

  public void update() {
    int nextIndex = getNextIndex();

    for (int i = lastReadIndex + 1; i <= nextIndex - 1; i++) {
      int serialized = uc.read(COMM_OFFSET + 1 + i);
      lastReadIndex = i;

      if (serialized < 0) {
        orders.remove(serialized * -1);
      } else {
        BuildOrder order = BuildOrder.fromSerialized(serialized, wrapperTypes);
        orders.put(Locations.toInt(order.getLocation()), order);
      }
    }
  }

  public List<BuildOrder> getOrders() {
    return new ArrayList<>(orders.values());
  }

  public void addOrder(Location location, TypeWrapper type) {
    int locationNum = Locations.toInt(location);

    BuildOrder currentOrder = orders.get(locationNum);
    if (currentOrder != null && currentOrder.getType().getId() == type.getId()) {
      return;
    }

    BuildOrder order = new BuildOrder(location, type);
    orders.put(locationNum, order);

    addMessage(order.serialize());
  }

  public void removeOrder(Location location) {
    int locationNum = Locations.toInt(location);

    BuildOrder order = orders.remove(locationNum);
    if (order == null) {
      return;
    }

    addMessage(-locationNum);
  }

  public void removeOrder(BuildOrder order) {
    removeOrder(order.getLocation());
  }

  private void addMessage(int num) {
    int nextIndex = getNextIndex();

    uc.write(COMM_OFFSET + 1 + nextIndex, num);
    setNextIndex(nextIndex + 1);

    lastReadIndex++;
  }

  private int getNextIndex() {
    return uc.read(COMM_OFFSET);
  }

  private void setNextIndex(int nextIndex) {
    uc.write(COMM_OFFSET, nextIndex);
  }
}
