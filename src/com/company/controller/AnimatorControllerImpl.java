package com.company.controller;

import com.company.controller.console.ArgumentsProcessImpl;
import com.company.controller.console.ArgumentsProcessor;

/**
 * Implementation of the animator controller that begins the animation.
 */
public class AnimatorControllerImpl implements AnimatorController {
  private final String[] arguments;

  private final ArgumentsProcessor processor;

  /**
   * Creates this controller with the given set of arguments.
   *
   * @param arguments command-line arguments used to run the program
   */
  public AnimatorControllerImpl(String[] arguments) {
    this.arguments = arguments;
    this.processor = new ArgumentsProcessImpl();
  }

  @Override
  public void run() {
    String viewArgument = "view";

    try {
      for (String arg : arguments) {
        if (arg.indexOf("view") == 0) {
          viewArgument = arg;
        } else {
          processor.processArguments(arg);
        }
      }

      processor.processArguments(viewArgument);
    } catch (IllegalStateException e) {
      System.out.println(e.getMessage());
    }

  }
}
