package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;
import com.company.model.shape.PosnImpl;
import com.company.model.shape.ShapeType;

import java.awt.Color;

public class CreateNewShape implements AnimatorAction {
  private final String name;
  private final int x;
  private final int y;
  private final double width;
  private final double height;
  private final Color color;
  private final ShapeType type;
  private final int tick;

  /**
   * Initializes all the properties that are useful for creating a new shape.
   *
   * @param name   name of the shape that will be created
   * @param x      x position of the shape that will be created
   * @param y      y position of the shape that will be created
   * @param width  width of the shape that will be created
   * @param height height of the shape that will be created
   * @param color  color of the shape that will be created
   * @param type   type of shape being created
   * @param tick   initial tick of the shape that will be created
   */
  public CreateNewShape(String name, int x, int y, double width, double height, Color color,
                        ShapeType type, int tick) {
    this.name = name;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.color = color;
    this.type = type;
    this.tick = tick;
  }

  @Override
  public void actOn(AnimatorModel model) throws IllegalStateException {
    model.createKeyframe(this.name, type.getShape(new PosnImpl(this.x, this.y), this.width,
        this.height, this.color), this.tick);
  }
}
