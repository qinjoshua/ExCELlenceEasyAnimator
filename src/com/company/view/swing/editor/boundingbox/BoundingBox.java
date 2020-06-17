package com.company.view.swing.editor.boundingbox;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class BoundingBox implements Shape {
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

  @Override
  public java.awt.Rectangle getBounds() {
    return this.boundingBox.getBounds();
  }

  @Override
  public Rectangle2D getBounds2D() {
    return this.boundingBox.getBounds2D();
  }

  @Override
  public boolean contains(double x, double y) {
    return this.boundingBox.contains(x, y);
  }

  @Override
  public boolean contains(Point2D p) {
    return this.boundingBox.contains(p);
  }

  @Override
  public boolean intersects(double x, double y, double w, double h) {
    return this.boundingBox.intersects(x, y, w, h);
  }

  @Override
  public boolean intersects(Rectangle2D r) {
    return this.boundingBox.intersects(r);
  }

  @Override
  public boolean contains(double x, double y, double w, double h) {
    return this.boundingBox.contains(x, y, w, h);
  }

  @Override
  public boolean contains(Rectangle2D r) {
    return this.boundingBox.contains(r);
  }

  @Override
  public PathIterator getPathIterator(AffineTransform at) {
    return this.boundingBox.getPathIterator(at);
  }

  @Override
  public PathIterator getPathIterator(AffineTransform at, double flatness) {
    return this.boundingBox.getPathIterator(at, flatness);
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

    g.setColor(Color.CYAN);
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
    int widthChange = dx * type.x;
    int heightChange = dy * type.y;

    this.boundingBox.width += widthChange;
    this.boundingBox.height += heightChange;

    if (type.x < 0) {
      this.translate(dx * Math.abs(type.x), 0);
    }
    if (type.y < 0) {
      this.translate(0, dy * Math.abs(type.y));
    }

    this.recomputeAnchors();
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
