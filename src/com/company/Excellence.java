package com.company;

import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.model.shape.PosnImpl;
import com.company.model.shape.shapes.Ellipse;
import com.company.model.shape.shapes.Rectangle;
import com.company.util.AnimationBuilder;
import com.company.util.AnimationReader;
import com.company.view.SVG.SVGViewImpl;
import com.company.view.SVGView;
import com.company.view.VisualView;
import com.company.view.swing.SwingView;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The main class that runs the program.
 */
public final class Excellence {
  public static void old_main(String[] args) {
    AnimatorModel model = new AnimatorModelImpl();
    model.setCanvasWidth(640);
    model.setCanvasHeight(400);
    model.setCanvasX(300);
    model.setCanvasY(200);
    model.createKeyframe("E",
        new Ellipse(new PosnImpl(310, 210), 10, 20,
            new Color(0, 144, 144)), 0);
    model.createKeyframe("E",
        new Ellipse(new PosnImpl(500, 400), 15, 15,
            new Color(255, 150, 150)), 5);
    model.createKeyframe("R",
        new Rectangle(new PosnImpl(300, 234), 21, 34,
            new Color(200, 200, 200)), 0);
    model.createKeyframe("R",
        new Rectangle(new PosnImpl(300, 234), 21, 34,
            new Color(200, 200, 200)), 2);
    model.createKeyframe("R",
        new Rectangle(new PosnImpl(890, 600), 50, 20,
            new Color(100, 100, 255)), 5);
    model.createKeyframe("R",
        new Rectangle(new PosnImpl(890, 600), 50, 20,
            new Color(100, 100, 255)), 100);
    VisualView visualView = new SwingView(model, 30);
    visualView.renderVisual();
  }

  public static void main(String[] args) {
    try {
      BufferedReader bigBang = Files.newBufferedReader(Paths.get("big-bang-big-crunch.txt"));
      AnimationBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();
      AnimatorModel model = AnimationReader.parseFile(bigBang, builder);
//      SVGView svg = new SVGViewImpl(model, 30);
//      BufferedWriter writer = Files.newBufferedWriter(Paths.get("toh-12.svg"));
//      svg.outputSVG(writer);
//      writer.flush();
//      writer.close();
      System.out.println("Done!");
      VisualView view = new SwingView(model, 300);
      view.renderVisual();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
