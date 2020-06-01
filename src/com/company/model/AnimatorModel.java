package com.company.model;

import java.util.Map;

/**
 * This interface represents the model for an animation that allows adding keyframes for specific
 * types of objects, and getting the state of the animation at a specific time.
 */
public interface AnimatorModel {


  /**
   *
   * 
   *
   * @param time
   * @return A map of the names
   */
  Map<String, Shape> frameAt(double time);


}
