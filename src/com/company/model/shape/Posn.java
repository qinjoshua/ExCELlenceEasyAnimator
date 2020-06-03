package com.company.model.shape;

public interface Posn {
  /**
   * Gets the x position.
   *
   * @return the x position.
   */
  double getX();

  /**
   * Gets the y position.
   *
   * @return the y position.
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
