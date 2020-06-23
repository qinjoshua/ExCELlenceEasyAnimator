package com.company.view.swing.editor.boundingbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

/**
 * Represents an anchor attached to a bounding box that allows the bounding box to be resized.
 */
public class Anchor {
  private static final int RADIUS = 5;
  private final Ellipse2D.Double anchor;
  private final AnchorType type;

  /**
   * Initializes the anchor with a given anchor type and a rectangle representing the bounding box,
   * in order to get the relevant positions.
   *
   * @param type        the type of anchor that this anchor is
   * @param boundingBox the bounding box that the anchors should surround
   */
  public Anchor(AnchorType type, Rectangle boundingBox) {
    double x = type.getX() * boundingBox.getWidth() / 2 + boundingBox.getCenterX() - RADIUS;
    double y = type.getY() * boundingBox.getHeight() / 2 + boundingBox.getCenterY() - RADIUS;
    anchor = new Ellipse2D.Double(x, y, RADIUS * 2, RADIUS * 2);
    this.type = type;
  }

  /**
   * Translates the anchor with the given x and y values.
   *
   * @param dx delta x, the distance to move in the x direction
   * @param dy delta y, the distance to move in the y direction
   */
  public void translate(int dx, int dy) {
    anchor.x += dx;
    anchor.y += dy;
  }

  /**
   * Checks if the given x and y coordinates are contained within this anchor.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return true if the given x and y coordinates are in this anchor, false otherwise
   */
  public boolean contains(int x, int y) {
    return anchor.contains(x, y);
  }

  /**
   * Gets the type of this anchor.
   *
   * @return the type of this anchor
   */
  public AnchorType getType() {
    return this.type;
  }

  /**
   * Renders this anchor to the given graphics.
   *
   * @param g the graphics to render this anchor to
   */
  public void renderTo(Graphics2D g) {
    Color oldColor = g.getColor();
    Stroke oldStroke = g.getStroke();
    g.setStroke(new BasicStroke(1.5f));
    g.setColor(new Color(240, 240, 240));
    g.fill(anchor);
    g.setColor(new Color(140, 140, 140));
    g.draw(anchor);
    g.setStroke(oldStroke);
    g.setColor(oldColor);
  }
}