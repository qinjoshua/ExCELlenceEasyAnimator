package com.company.model.shape;

import java.util.Objects;

/**
 * Represents a coordinate on the screen with x and y positions, with a Cartesian coordinate system.
 * TODO what coordinate system is this?
 */
public class PosnCart implements Posn {
  private final double x;
  private final double y;

  /**
   * Constructs a Posn with the given x and y coordinates.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public PosnCart(double x, double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public double getX() {
    return this.x;
  }

  @Override
  public double getY() {
    return this.y;
  }

  @Override
  public Posn interpolate(Posn to, double progress) {
    if (progress < 0 || progress > 1) {
      throw new IllegalArgumentException("Progress must be between 0 and 1");
    }

    return new PosnCart(this.x + progress * (to.getX() - this.x),
        this.y + progress * (to.getY() - this.y));
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof PosnCart)) {
      return false;
    }
    PosnCart posnCart = (PosnCart) other;
    return x == posnCart.getX() && y == posnCart.getY();
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}