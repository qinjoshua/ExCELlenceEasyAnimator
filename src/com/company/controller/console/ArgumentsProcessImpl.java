package com.company.controller.console;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.AnimatorActionConsumerImpl;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.controller.viewactions.editoractions.EditorActionConsumerImpl;
import com.company.controller.viewactions.playeractions.PlayerAction;
import com.company.controller.viewactions.playeractions.PlayerActionConsumerImpl;
import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.util.AnimationBuilder;
import com.company.util.AnimationReader;
import com.company.view.VisualView;
import com.company.view.svg.SVGView;
import com.company.view.svg.SVGViewImpl;
import com.company.view.swing.SwingView;
import com.company.view.swing.editor.EditorView;
import com.company.view.swing.editor.EditorViewImpl;
import com.company.view.swing.player.PlayerView;
import com.company.view.swing.player.PlayerViewImpl;
import com.company.view.text.TextAnimatorView;
import com.company.view.text.TextView;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Implementation of the argument processor that stores all of the necessary arguments as fields. In
 * order to run correctly, this argument processor must be asked to process view after all the other
 * arguments have been processed.
 */
public class ArgumentsProcessImpl implements ArgumentsProcessor {
  final Map<String, Function<String, Void>> knownArguments;
  private Integer fps;
  private Appendable out;
  private Readable in;
  private boolean closable;

  /**
   * Default constructor that sets up known arguments and initializes all other fields to null.
   */
  public ArgumentsProcessImpl() {
    fps = 1;
    out = System.out;
    in = null;
    closable = false;

    knownArguments = new HashMap<>();

    knownArguments.put("in", new InputFunction());
    knownArguments.put("out", new OutputFunction());
    knownArguments.put("speed", new FpsFunction());
    knownArguments.put("view", new ViewFunction());
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

  private class InputFunction implements Function<String, Void> {
    @Override
    public Void apply(String s) {
      try {
        in = Files.newBufferedReader(Paths.get(s));
      } catch (IOException e) {
        throw new IllegalStateException("The input file was not able to be processed.");
      }
      return null;
    }
  }

  private class OutputFunction implements Function<String, Void> {
    @Override
    public Void apply(String s) {
      try {
        out = new FileWriter(s);
        closable = true;
      } catch (IOException e) {
        throw new IllegalStateException("The output file path was not able to be written to.");
      }
      return null;
    }
  }

  private class FpsFunction implements Function<String, Void> {
    @Override
    public Void apply(String s) {
      fps = Integer.parseInt(s);
      return null;
    }
  }

  private class ViewFunction implements Function<String, Void> {
    @Override
    public Void apply(String s) {
      AnimationBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();
      AnimatorModel model;
      Consumer<AnimatorAction> callback;

      if (in == null) {
        throw new IllegalStateException("No input file was provided");
      } else {
        model = AnimationReader.parseFile(in, builder);
        callback = new AnimatorActionConsumerImpl(model);
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
      } else if (s.equalsIgnoreCase("edit")) {
        EditorView editorView = new EditorViewImpl(model, callback);
        editorView.setCallback(new EditorActionConsumerImpl(editorView));
        editorView.renderVisual();
      } else if (s.equalsIgnoreCase("play")) {
        PlayerView playerView = new PlayerViewImpl(model, fps);
        playerView.setCallback(new PlayerActionConsumerImpl(playerView));
      } else {
        throw new IllegalStateException("The type of view was not recognized.");
      }

      if (closable) {
        try {
          ((Closeable) out).close();
        } catch (IOException e) {
          System.out.println("Ran into an error with the output file.");
        }
      }

      return null;
    }
  }
}
