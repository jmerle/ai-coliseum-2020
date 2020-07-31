package camel_case;

import aic2020.user.GameConstants;
import aic2020.user.UnitController;
import aic2020.user.UnitType;
import camel_case.robot.Robot;
import camel_case.robot.structure.Barracks;
import camel_case.robot.structure.Base;
import camel_case.robot.structure.Hospital;
import camel_case.robot.structure.Laboratory;
import camel_case.robot.structure.Market;
import camel_case.robot.unit.EssentialWorker;
import camel_case.robot.unit.Fumigator;
import camel_case.robot.unit.Infecter;
import camel_case.robot.unit.Soldier;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;

public class UnitPlayer {
  private DecimalFormat intFormatter = new DecimalFormat("###,###");
  private boolean isFirstRound = true;

  public void run(UnitController uc) {
    Robot robot = createRobot(uc);

    //noinspection InfiniteLoopStatement
    while (true) {
      performTurn(uc, robot);
      uc.yield();
    }
  }

  private void performTurn(UnitController uc, Robot robot) {
    int startRound = uc.getRound();
    int startEnergy = uc.getEnergyUsed();

    try {
      robot.getBuildQueue().update();
      robot.run();
    } catch (Exception e) {
      StringWriter stringWriter = new StringWriter();
      e.printStackTrace(new PrintWriter(stringWriter));

      uc.println(stringWriter.toString());
    }

    if (isFirstRound) {
      isFirstRound = false;
      return;
    }

    int endRound = uc.getRound();
    int endEnergy = uc.getEnergyUsed();

    int maxEnergy = GameConstants.MAX_ENERGY;

    int totalEnergy;
    if (startRound == endRound) {
      totalEnergy = endEnergy - startEnergy;
    } else {
      int firstRoundEnergy = maxEnergy - startEnergy;
      int middleRoundEnergy = (endRound - startRound - 1) * maxEnergy;
      totalEnergy = firstRoundEnergy + middleRoundEnergy + endEnergy;
    }

    double percentage = (double) totalEnergy / (double) maxEnergy * 100.0;
    if (percentage >= 90.0) {
      uc.println(
          String.format(
              "High bytecode usage: %s/%s (%s%%)",
              intFormatter.format(totalEnergy),
              intFormatter.format(maxEnergy),
              intFormatter.format((int) Math.round(percentage))));
    }
  }

  private Robot createRobot(UnitController uc) {
    UnitType type = uc.getType();

    if (type == UnitType.ESSENTIAL_WORKER) {
      return new EssentialWorker(uc);
    } else if (type == UnitType.FUMIGATOR) {
      return new Fumigator(uc);
    } else if (type == UnitType.SOLDIER) {
      return new Soldier(uc);
    } else if (type == UnitType.INFECTER) {
      return new Infecter(uc);
    } else if (type == UnitType.BASE) {
      return new Base(uc);
    } else if (type == UnitType.MARKET) {
      return new Market(uc);
    } else if (type == UnitType.BARRACKS) {
      return new Barracks(uc);
    } else if (type == UnitType.LABORATORY) {
      return new Laboratory(uc);
    } else if (type == UnitType.HOSPITAL) {
      return new Hospital(uc);
    }

    throw new IllegalArgumentException("Unknown unit type: " + type);
  }
}
