package com.company;

import com.company.controller.AnimatorController;
import com.company.controller.AnimatorControllerImpl;
import com.company.controller.viewactions.playeractions.PlayerActionConsumerImpl;
import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.util.AnimationBuilder;
import com.company.util.AnimationReader;
import com.company.view.VisualView;
import com.company.view.player.PlayerView;
import com.company.view.player.PlayerViewImpl;

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
  public static void oldMain(String[] args) {
    AnimatorController controller = new AnimatorControllerImpl(formatArgs(args));
    controller.run();
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

    PlayerView view = new PlayerViewImpl(model, 20);
    view.setCallback(new PlayerActionConsumerImpl(view));
    view.renderVisual();
  }

  /**
   * Private helper method for formatting arguments into an acceptable format.
   *
   * @param args the array of arguments to be formatted
   * @return the array of formatted arguments
   */
  private static String[] formatArgs(String[] args) {
    List<String> arguments = new ArrayList<String>();
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
