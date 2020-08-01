package camel_case.build;

import aic2020.user.Location;
import aic2020.user.UnitController;
import camel_case.util.Locations;
import camel_case.util.Offsets;

public class MapAnalyzer {
  private final int CELL_OBSTRUCTED = 0;
  private final int CELL_UNMARKED = 1;
  private final int CELL_BUILDABLE = 2;
  private final int CELL_OUT_OF_RANGE = 3;

  private UnitController uc;

  private Location myLocation;

  private int maxOffset;
  private int gridSize;

  private int[][] grid;

  private Offsets offsets = new Offsets();

  public MapAnalyzer(UnitController uc) {
    this.uc = uc;

    myLocation = uc.getLocation();

    maxOffset = (int) Math.ceil(Math.sqrt(uc.getType().visionRange));
    gridSize = maxOffset * 2 + 1;

    grid = new int[gridSize][gridSize];

    for (int y = 0; y < gridSize; y++) {
      int[] row = new int[gridSize];

      for (int x = 0; x < gridSize; x++) {
        row[x] = CELL_OBSTRUCTED;
      }

      grid[y] = row;
    }

    findBuildableLocations();
  }

  public boolean isBuildable(Location location) {
    return getValue(location) == CELL_BUILDABLE;
  }

  private void findBuildableLocations() {
    Location[] visibleLocations = uc.getVisibleLocations();

    for (Location location : visibleLocations) {
      boolean isBlocked =
          !uc.isAccessible(location)
              || uc.senseFarmAtLocation(location) != null
              || uc.senseUnitAtLocation(location) != null;

      setValue(location, isBlocked ? CELL_OBSTRUCTED : CELL_UNMARKED);
    }

    for (int[] offset : offsets.getRingOffsets(1)) {
      floodFill(Locations.applyOffset(myLocation, offset));
    }
  }

  private void floodFill(Location location) {
    int currentValue = getValue(location);

    if (currentValue != CELL_UNMARKED) {
      return;
    }

    setValue(location, CELL_BUILDABLE);

    for (int[] offset : offsets.getRingOffsets(1)) {
      floodFill(Locations.applyOffset(location, offset));
    }
  }

  private int getValue(Location location) {
    int x = getX(location);
    int y = getY(location);

    if (x < 0 || x > gridSize - 1 || y < 0 || y > gridSize - 1) {
      return CELL_OUT_OF_RANGE;
    }

    return grid[y][x];
  }

  private void setValue(Location location, int newValue) {
    grid[getY(location)][getX(location)] = newValue;
  }

  private int getX(Location location) {
    int offset = location.x - myLocation.x;
    return offset + maxOffset;
  }

  private int getY(Location location) {
    int offset = location.y - myLocation.y;
    return offset + maxOffset;
  }
}
