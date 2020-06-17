package com.company.view.swing.editor.boundingbox;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a box that bounds a bounds a shape
 */
public class BoundingBox {
  private final Shape highlightedShape;
  private final Rectangle boundingBox;
  private List<Anchor> anchorPoints;

  public BoundingBox(Shape highlightedShape) {
    this.highlightedShape = highlightedShape;
    boundingBox = highlightedShape.getBounds();

    anchorPoints = new ArrayList<>();
    for (AnchorType type : AnchorType.values()) {
      anchorPoints.add(new Anchor(type, this.boundingBox));
    }
  }

  /**
   * @param dx
   * @param dy
   */
  public void translate(int dx, int dy) {
    this.boundingBox.translate(dx, dy);
    for (Anchor anchor : anchorPoints) {
      anchor.translate(dx, dy);
    }
  }

  public void renderTo(Graphics2D g) {
    Color originalColor = g.getColor();

    g.setColor(Color.LIGHT_GRAY);
    g.draw(this.boundingBox);

    for (Anchor anchor : anchorPoints) {
      anchor.renderTo(g);
    }

    g.setColor(originalColor);
  }

  private void recomputeAnchors() {
    anchorPoints = new ArrayList<>();
    for (AnchorType type : AnchorType.values()) {
      anchorPoints.add(new Anchor(type, this.boundingBox));
    }
  }

  public void growFromAnchor(AnchorType type, int dx, int dy) {
    int widthChange = dx * type.getX();
    int heightChange = dy * type.getY();

    this.boundingBox.width += widthChange;
    this.boundingBox.height += heightChange;

    if (type.getX() < 0) {
      this.translate(dx * Math.abs(type.getX()), 0);
    }
    if (type.getY() < 0) {
      this.translate(0, dy * Math.abs(type.getY()));
    }

    this.recomputeAnchors();
  }

  public boolean contains(int x, int y) {
    return this.boundingBox.contains(x, y);
  }

  /**
   * Gets the anchor at a given point, or null if there is no anchor at the given point.
   *
   * @param x x-coordinate to get the anchor at
   * @param y y-coordinate to get the anchor at
   * @return the anchor at the given point, or null
   */
  public Anchor getAnchorAtPoint(int x, int y) {
    for (Anchor anchor : anchorPoints) {
      if (anchor.contains(x, y)) {
        return anchor;
      }
    }
    return null;
  }

  public int getX() {
    return this.boundingBox.x;
  }

  public int getY() {
    return this.boundingBox.y;
  }

  public double getWidth() {
    return this.boundingBox.getWidth();
  }

  public double getHeight() {
    return this.boundingBox.getHeight();
  }
}