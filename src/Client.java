import Common.MainMenu.MainWindow;
import ReplayVisualizer.Managers.ReplayManager;
import ReplayVisualizer.UIObjects.ReplayWindow;
import com.alee.laf.WebLookAndFeel;
import java.io.File;
import java.lang.reflect.Field;
import javax.swing.JFrame;

public class Client {
  public static void main(String[] args) {
    WebLookAndFeel.install();

    if (args.length == 0) {
      openMainWindow();
    } else {
      openReplays(args);
    }
  }

  private static void openMainWindow() {
    MainWindow window = new MainWindow();

    try {
      Field frameField = window.getClass().getDeclaredField("frame");
      frameField.setAccessible(true);

      JFrame frame = (JFrame) frameField.get(window);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static void openReplays(String[] args) {
    File[] replayFiles = new File[args.length];
    for (int i = 0; i < args.length; i++) {
      replayFiles[i] = new File(args[i]);
    }

    ReplayWindow window = new ReplayWindow(replayFiles);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    try {
      Field replayManagerField = window.getClass().getDeclaredField("replayManager");
      replayManagerField.setAccessible(true);

      ReplayManager replayManager = (ReplayManager) replayManagerField.get(window);
      replayManager.ToggleVisuals();
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
