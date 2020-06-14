package com.company.controller;

import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.util.AnimationBuilder;
import com.company.util.AnimationReader;
import com.company.view.SVG.SVGView;
import com.company.view.SVG.SVGViewImpl;
import com.company.view.swing.SwingView;
import com.company.view.swing.VisualView;
import com.company.view.text.TextAnimatorView;
import com.company.view.text.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Implementation of the argument processor that stores all of the necessary arguments as fields.
 * In order to run correctly, this argument processor must be asked to process view after all the
 * other arguments have been processed.
 */
public class ArgumentsProcessImpl implements ArgumentsProcessor {
  Map<String, Function<String, Void>> knownArguments;
  private Integer fps;
  private Appendable out;
  private Readable in;

  /**
   * Default constructor that sets up known arguments and initializes all other fields to null.
   */
  public ArgumentsProcessImpl() {
    fps = 1;
    out = System.out;
    in = null;

    knownArguments = new HashMap<>();

    knownArguments.put("in", new inputFunction());
    knownArguments.put("out", new outputFunction());
    knownArguments.put("speed", new fpsFunction());
    knownArguments.put("view", new viewFunction());
  }

  @Override
  public void processArguments(String argument) throws IllegalStateException {
    Scanner scan = new Scanner(argument);

    Function<String, Void> toRun = knownArguments.get(scan.next());
    if (toRun == null) {
      throw new IllegalStateException("Unrecognized argument");
    } else {
      if (scan.hasNext()) {
        toRun.apply(scan.next());
      } else {
        throw new IllegalStateException("No value recognized for the argument " + argument);
      }

    }
  }

  private class inputFunction implements Function<String, Void> {
    @Override
    public Void apply(String s) {
      try {
        BufferedReader input = Files.newBufferedReader(Paths.get(s));
        in = input;
      } catch (IOException e) {
        throw new IllegalStateException("The input file was not able to be processed.");
      }
      return null;
    }
  }

  private class outputFunction implements Function<String, Void> {
    @Override
    public Void apply(String s) {
      try {
        out = Files.newBufferedWriter(Paths.get(s));
      } catch (IOException e) {
        throw new IllegalStateException("The output file path was not able to be written to.");
      }
      return null;
    }
  }

  private class fpsFunction implements Function<String, Void> {
    @Override
    public Void apply(String s) {
      fps = Integer.parseInt(s);
      return null;
    }
  }

  private class viewFunction implements Function<String, Void> {
    @Override
    public Void apply(String s) {
      AnimationBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();
      AnimatorModel model;

      if (in == null) {
        throw new IllegalStateException("No input file was provided");
      } else {
        model = AnimationReader.parseFile(in, builder);
      }

      if (s.equalsIgnoreCase("svg")) {
        SVGView svg = new SVGViewImpl(model, fps);
        try {
          svg.outputSVG(out);
        } catch (IOException e) {
          throw new IllegalStateException("The output was not able to be written to.");
        }
      } else if (s.equalsIgnoreCase("visual")) {
        VisualView visual = new SwingView(model, fps);
        visual.renderVisual();
      } else if (s.equalsIgnoreCase("text")) {
        TextView text = new TextAnimatorView(model);
        try {
          text.outputText(out);
        } catch (IOException e) {
          throw new IllegalStateException("The output was not able to be written to.");
        }
      } else {
        throw new IllegalStateException("The type of view was not recognized.");
      }
      return null;
    }
  }
}
