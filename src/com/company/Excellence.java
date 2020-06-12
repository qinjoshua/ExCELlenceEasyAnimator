package com.company;

import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.model.shape.PosnCart;
import com.company.model.shape.shapes.Ellipse;
import com.company.model.shape.shapes.Rectangle;
import com.company.view.AnimatorView;
import com.company.view.swing.SwingView;

import java.awt.*;
import java.io.IOException;

/**
 * The main class that runs the program.
 */
public class Excellence {
  public static void main(String[] args) throws IOException {
    AnimatorModel model = new AnimatorModelImpl();
    model.createKeyframe("S",
        new Ellipse(new PosnCart(10, 10), 10, 20,
            new Color(0, 144, 144)), 0);
    model.createKeyframe("S",
        new Ellipse(new PosnCart(200, 200), 15, 15,
            new Color(255, 150, 150)), 5);
    model.createKeyframe("R",
        new Rectangle(new PosnCart(0,0), 21, 34,
            new Color(200, 200, 200)), 0);
    model.createKeyframe("R",
        new Rectangle(new PosnCart(590, 380), 50, 20,
            new Color(100, 100, 255)), 5);
    model.createKeyframe("R",
        new Rectangle(new PosnCart(590, 380), 50, 20,
            new Color(100, 100, 255)), 100);
    AnimatorView visualView = new SwingView(model, 640, 400);
    visualView.output(null);
  }
}
