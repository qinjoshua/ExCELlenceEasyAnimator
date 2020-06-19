package com.company.view.swing.editor.boundingbox;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a box that bounds a bounds a highlighted shape. The box may be resized and
 * repositioned, and contains anchors/handles that allows resizing operations to be done.
 */
public class BoundingBoxImpl implements BoundingBox {
  private final Rectangle boundingBox;
  private List<Anchor> anchorPoints;

  /**
   * Constructor that initializes the bounding box to be the size of the currently highlighted
   * shape.
   *
   * @param currentlyHighlightedShape the currently highlighted shape that the bounding box bounds
   */
  public BoundingBoxImpl(Shape currentlyHighlightedShape) {
    Shape highlightedShape = currentlyHighlightedShape;
    boundingBox = highlightedShape.getBounds();

    anchorPoints = new ArrayList<>();
    for (AnchorType type : AnchorType.values()) {
      anchorPoints.add(new Anchor(type, this.boundingBox));
    }
  }

  @Override
  public void translate(int dx, int dy) {
    this.boundingBox.translate(dx, dy);
    for (Anchor anchor : anchorPoints) {
      anchor.translate(dx, dy);
    }
  }

  @Override
  public void renderTo(Graphics2D g) {
    Color originalColor = g.getColor();

    g.setColor(Color.LIGHT_GRAY);
    g.draw(this.boundingBox);

    for (Anchor anchor : anchorPoints) {
      anchor.renderTo(g);
    }

    g.setColor(originalColor);
  }

  /**
   * Recomputes where the anchors should be relative to the bounding box.
   */
  private void recomputeAnchors() {
    anchorPoints = new ArrayList<>();
    for (AnchorType type : AnchorType.values()) {
      anchorPoints.add(new Anchor(type, this.boundingBox));
    }
  }

  @Override
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

  @Override
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
  @Override
  public Anchor getAnchorAtPoint(int x, int y) {
    for (Anchor anchor : anchorPoints) {
      if (anchor.contains(x, y)) {
        return anchor;
      }
    }
    return null;
  }

  @Override
  public int getX() {
    return this.boundingBox.x;
  }

  @Override
  public int getY() {
    return this.boundingBox.y;
  }

  @Override
  public double getWidth() {
    return this.boundingBox.getWidth();
  }

  @Override
  public double getHeight() {
    return this.boundingBox.getHeight();
  }
}