package com.company.model.shape;

/**
 * A single 2D coordinate in a 2D coordinate system. Can be transformed to Cartesian coordinates.
 */
public interface Posn {
  /**
   * Gets the x position in Cartesian coordinates.
   *
   * @return the x position in Cartesian coordinates
   */
  double getX();

  /**
   * Gets the y position in Cartesian coordinates
   *
   * @return the y position in Cartesian coordinates
   */
  double getY();

  /**
   * Gets the interpolated position between this posn and the given posn, at the given progress.
   *
   * @param to       the position to interpolate to
   * @param progress a number between 0 and 1 that represents the progress between this posn and the
   *                 to posn
   * @return the interpolated position
   * @throws IllegalArgumentException if progress is not between 0 and 1
   */
  Posn interpolate(Posn to, double progress);
}
