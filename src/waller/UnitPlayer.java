package waller;

import aic2020.user.GameConstants;
import aic2020.user.UnitController;
import aic2020.user.UnitType;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import waller.robot.Robot;
import waller.robot.structure.Barracks;
import waller.robot.structure.Base;
import waller.robot.structure.Hospital;
import waller.robot.structure.Laboratory;
import waller.robot.structure.Market;
import waller.robot.unit.EssentialWorker;
import waller.robot.unit.Fumigator;
import waller.robot.unit.Infecter;
import waller.robot.unit.Soldier;

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
      robot.update();
      robot.run();
    } catch (Exception e) {
      StringWriter stringWriter = new StringWriter();
      e.printStackTrace(new PrintWriter(stringWriter));

      uc.println(stringWriter.toString());
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
              "High bytecode usage: %s/%s (%s%%)" + (isFirstRound ? " (unit's first round)" : ""),
              intFormatter.format(totalEnergy),
              intFormatter.format(maxEnergy),
              intFormatter.format((int) Math.round(percentage))));
    }

    isFirstRound = false;
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
