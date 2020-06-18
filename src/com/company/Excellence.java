package com.company;

import com.company.controller.AnimatorController;
import com.company.controller.AnimatorControllerImpl;
import com.company.controller.animatoractions.AnimatorActionConsumerImpl;
import com.company.controller.viewactions.editoractions.EditorActionConsumerImpl;
import com.company.controller.viewactions.playeractions.PlayerActionConsumerImpl;
import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.util.AnimationBuilder;
import com.company.util.AnimationReader;
import com.company.view.swing.editor.EditorView;
import com.company.view.swing.editor.EditorViewImpl;
import com.company.view.swing.editor.LCHColorChooser;
import com.company.view.swing.player.PlayerView;
import com.company.view.swing.player.PlayerViewImpl;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class that runs the program.
 */
public final class Excellence {

  /**
   * Main entry point for the application.
   *
   * @param args list of input arguments for the animator
   */
  public static void olderMain(String[] args) {
    AnimatorController controller = new AnimatorControllerImpl(formatArgs(args));
    controller.run();
  }

  /**
   * Main entry point for the application.
   *
   * @param args list of input arguments for the animator
   */
  public static void oldMain(String[] args) throws IOException {
    BufferedReader input = Files.newBufferedReader(Paths.get("toh-3.txt"));
    AnimationBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();
    AnimatorModel model = AnimationReader.parseFile(input, builder);

    PlayerView view = new PlayerViewImpl(model, 20);
    view.setCallback(new PlayerActionConsumerImpl(view));
    view.renderVisual();
  }

  /**
   * Main entry point for the application.
   *
   * @param args list of input arguments for the animator
   */
  public static void main(String[] args) throws IOException {
    BufferedReader input = Files.newBufferedReader(Paths.get("toh-3.txt"));
    AnimationBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();
    AnimatorModel model = AnimationReader.parseFile(input, builder);

    EditorView view = new EditorViewImpl(model, new AnimatorActionConsumerImpl(model));
    view.setCallback(new EditorActionConsumerImpl(view));
    view.renderVisual();
  }

  public static void testRGB(String[] args) {
    LCHColorChooser.XYZColor xyz = new LCHColorChooser.XYZColor(0.2, 0.2, 0.5);
    LCHColorChooser.LCHColor lch = xyz.toLCH();

    System.out.println(lch);
    System.out.println(xyz);
    System.out.println(new LCHColorChooser.XYZColor(lch));
    System.out.println("\n\n");
    System.out.println(lch.toRGB());
    System.out.println(xyz.toRGB());
    System.out.println(new LCHColorChooser.LCHColor(lch.toRGB()));
  }

  /**
   * Private helper method for formatting arguments into an acceptable format.
   *
   * @param args the array of arguments to be formatted
   * @return the array of formatted arguments
   */
  private static String[] formatArgs(String[] args) {
    List<String> arguments = new ArrayList<>();
    boolean flag = true;
    for (String arg : args) {
      if (flag) {
        arguments.add(arg.substring(arg.indexOf('-') + 1));
        flag = false;
      } else {
        arguments.set(arguments.size() - 1, arguments.get(arguments.size() - 1) + " " + arg);
        flag = true;
      }
    }

    String[] argumentsArray = new String[arguments.size()];
    for (int i = 0; i < arguments.size(); i += 1) {
      argumentsArray[i] = arguments.get(i);
    }
    return argumentsArray;
  }
}
