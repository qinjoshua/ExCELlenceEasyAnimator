package com.company;

import com.company.controller.AnimatorController;
import com.company.controller.AnimatorControllerImpl;

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
  public static void main(String[] args) {
    AnimatorController controller = new AnimatorControllerImpl(formatArgs(args));
    controller.run();
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
