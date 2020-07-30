package camel_case.robot;

import aic2020.user.Location;
import aic2020.user.Team;
import aic2020.user.UnitController;
import aic2020.user.UnitType;
import camel_case.color.Color;
import camel_case.color.Colors;

public abstract class Robot {
  protected UnitController uc;

  protected UnitType me;

  protected Team myTeam;
  protected Team enemyTeam;

  protected Colors colors = new Colors();

  public Robot(UnitController uc, UnitType type) {
    this.uc = uc;

    me = type;

    myTeam = uc.getTeam();
    enemyTeam = myTeam.getOpponent();
  }

  public abstract void run();

  protected void drawLine(Location from, Location to, Color color) {
    uc.drawLine(from, to, color.getRed(), color.getGreen(), color.getBlue());
  }

  protected void drawLine(Location to, Color color) {
    drawLine(uc.getLocation(), to, color);
  }

  protected void drawPoint(Location location, Color color) {
    uc.drawPoint(location, color.getRed(), color.getGreen(), color.getBlue());
  }
}
