package com.company.model.shape.shapes;

import com.company.model.shape.AShape;
import com.company.model.shape.Posn;
import com.company.model.shape.ShapeType;

import java.awt.*;

public class Rectangle extends AShape {
  public Rectangle(Posn posn, int width, int height, Color color) {
    super(posn, width, height, color, ShapeType.Rectangle);
  }
}
