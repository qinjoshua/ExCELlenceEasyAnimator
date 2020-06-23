package com.company.view.swing.editor;

import com.company.view.swing.DecoratedShape;

import java.awt.Point;
import java.awt.geom.Point2D;

public class MousePointUtils {
  public static Point transformPoint(Point point, DecoratedShape shape) {
    return transformPoint(point.x, point.y, shape.shape.getBounds2D().getCenterX(),
            shape.shape.getBounds2D().getCenterY(), shape.angle);
  }

  public static Point transformPoint(int x, int y, double centerX, double centerY, double angle) {
    int offsetX = 0;
    int offsetY = 0;

    Point2D origin = new Point2D.Double(centerX, centerY);

    double radius = Math.hypot(x - origin.getX(), y - origin.getY());

    double theta = Math.atan((y - origin.getY()) / (x - origin.getX()));
    theta += angle;

    int x2 = (int) (Math.cos(theta) * radius + offsetX + origin.getX());
    int y2 = (int) (Math.sin(theta) * radius + offsetY + origin.getY());

    return new Point(x2, y2);
  }
}
