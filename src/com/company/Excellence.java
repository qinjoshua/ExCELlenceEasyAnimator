package com.company;

import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.model.shape.PosnCart;
import com.company.model.shape.shapes.Ellipse;
import com.company.model.shape.shapes.Rectangle;
import com.company.view.VisualView;
import com.company.view.swing.SwingView;

import java.awt.*;

/**
 * The main class that runs the program.
 */
public class Excellence {
  public static void main(String[] args) {
    AnimatorModel model = new AnimatorModelImpl();
    model.createKeyframe("E",
        new Ellipse(new PosnCart(10, 10), 10, 20,
            new Color(0, 144, 144)), 0);
    model.createKeyframe("E",
        new Ellipse(new PosnCart(200, 200), 15, 15,
            new Color(255, 150, 150)), 5);
    model.createKeyframe("R",
        new Rectangle(new PosnCart(0, 34), 21, 34,
            new Color(200, 200, 200)), 0);
    model.createKeyframe("R",
        new Rectangle(new PosnCart(590, 400), 50, 20,
            new Color(100, 100, 255)), 5);
    model.createKeyframe("R",
        new Rectangle(new PosnCart(590, 400), 50, 20,
            new Color(100, 100, 255)), 100);
    VisualView visualView = new SwingView(model, 30);
    visualView.renderVisual();
  }
}
