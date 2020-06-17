package com.company.view.swing.editor.boundingbox;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

public class Anchor {
  private static final int RADIUS = 5;
  private final Ellipse2D.Double anchor;
  private final AnchorType type;

  public Anchor(AnchorType type, Rectangle boundingBox) {
    double x = type.x * boundingBox.getWidth() / 2 + boundingBox.getCenterX() - RADIUS;
    double y = type.y * boundingBox.getHeight() / 2 + boundingBox.getCenterY() - RADIUS;
    anchor = new Ellipse2D.Double(x, y, RADIUS * 2, RADIUS * 2);
    this.type = type;
  }

  public void translate(int dx, int dy) {
    anchor.x += dx;
    anchor.y += dy;
  }

  public boolean contains(int x, int y) {
    return anchor.contains(x, y);
  }

  public AnchorType getType() {
    return this.type;
  }

  public void renderTo(Graphics2D g) {
    Color oldColor = g.getColor();
    g.setColor(new Color(240, 240, 240));
    g.fill(anchor);
    g.setColor(new Color(40, 40, 40));
    g.draw(anchor);
    g.setColor(oldColor);
  }
}