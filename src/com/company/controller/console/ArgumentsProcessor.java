package com.company.controller.console;

/**
 * Represents a tool that processes arguments.
 */
public interface ArgumentsProcessor {

  /**
   * Processes a single argument that is inputted.
   *
   * @param argument represents the argument, with the flag and the value
   * @throws IllegalStateException when one of the arguments cannot be processed
   */
  void processArguments(String argument) throws IllegalStateException;

}
