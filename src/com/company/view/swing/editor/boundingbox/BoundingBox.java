package com.company.view.swing.editor.boundingbox;

import com.company.view.swing.editor.boundingbox.Anchor;
import com.company.view.swing.editor.boundingbox.AnchorType;

import java.awt.Graphics2D;

/**
 * An interface representing the bounding box of a shape.
 */
public interface BoundingBox {
  /**
   * Translates the bounding box by the given x and y values.
   *
   * @param dx delta x, distance to move in x direction
   * @param dy delta y, distance to move in y direction
   */
  void translate(int dx, int dy);

  /**
   * Renders the bounding box to the given graphics.
   *
   * @param g graphics to render the bounding box to
   */
  void renderTo(Graphics2D g);

  /**
   * Increases/decreases the size of the bounding box from the given anchor.
   *
   * @param type the anchor from which the bounding box is growing for, such as Top, Bottom, Bottom
   *             Left, etc.
   * @param dx   delta x, the distance to move in the x direction
   * @param dy   delta y, the distance to move in the y direction
   */
  void growFromAnchor(AnchorType type, int dx, int dy);

  /**
   * Checks to see if the bounding box contains the given x and y coordinate.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return true if the x and y coordinate are within the bounding box, false otherwise
   */
  boolean contains(int x, int y);

  /**
   * Gets the anchor point at the given x and y location.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return the anchor at the given x and y coordinates
   */
  Anchor getAnchorAtPoint(int x, int y);

  /**
   * Gets the x coordinate of this bounding box.
   *
   * @return the x coordinate of the bounding box
   */
  int getX();

  /**
   * Gets the y coordinate of this bounding box.
   *
   * @return the y coordinate of the bounding box
   */
  int getY();

  /**
   * Gets the width of this bounding box.
   *
   * @return the width of this bounding box
   */
  double getWidth();

  /**
   * Gets the height of this bounding box.
   *
   * @return the height of this bounding box
   */
  double getHeight();
}
