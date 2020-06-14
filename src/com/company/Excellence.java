package com.company;

import com.company.controller.AnimatorController;
import com.company.controller.AnimatorControllerImpl;
import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.util.AnimationBuilder;
import com.company.util.AnimationReader;
import com.company.view.SVG.SVGViewImpl;
import com.company.view.SVG.SVGView;
import com.company.view.swing.VisualView;
import com.company.view.swing.SwingView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The main class that runs the program.
 */
public final class Excellence {
  public static void main(String[] args) {
    AnimatorController controller = new AnimatorControllerImpl(args);
    controller.run();
  }
}
