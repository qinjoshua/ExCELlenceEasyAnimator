package com.company.model.shape;
import java.util.Objects;

/**
 * Represents a coordinate on the screen with a publicly accessible x and y position.
 */
public class Posn {
  public final int x;
  public final int y;

  public Posn(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Posn)) {
      return false;
    }
    Posn posn = (Posn) other;
    return x == posn.x && y == posn.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
