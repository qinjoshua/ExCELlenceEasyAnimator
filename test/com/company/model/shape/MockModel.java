package com.company.model.shape;

import com.company.model.AnimatorModel;
import com.company.model.Frame;

import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;

public class MockModel implements AnimatorModel {
  Appendable out;

  public MockModel(Appendable out) {
    this.out = out;
  }

  private String shapeToString(Shape shape) {
    return String.format("x: %d y: %d w: %d h: %d r: %d g: %d b: %d",
            shape.getPosition().getX(), shape.getPosition().getY(),
            shape.getWidth(), shape.getHeight(),
            shape.getColor().getRed(), shape.getColor().getGreen(), shape.getColor().getBlue());
  }

  @Override
  public void createKeyframe(String shapeName, Shape shape, int tick) throws IllegalArgumentException {
    try {
      out.append("Name: " + shapeName + " Shape: " + this.shapeToString(shape) + "Tick: " + tick);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setCanvasWidth(int canvasWidth) {
    try {
      out.append(Integer.toString(canvasWidth));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setCanvasHeight(int canvasHeight) {
    try {
      out.append(Integer.toString(canvasHeight));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setCanvasX(int canvasX) {
    try {
      out.append(Integer.toString(canvasX));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setCanvasY(int canvasY) {
    try {
      out.append(Integer.toString(canvasY));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeKeyframe(String shapeName, int tick) {
    try {
      out.append("Name: " + shapeName + " Tick: " + tick);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteShape(String shapeName) {
    try {
      out.append(shapeName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Map<String, Shape> shapesAt(int tick) throws IllegalArgumentException {
    try {
      out.append(Integer.toString(tick));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Map<String, SortedSet<Frame>> getKeyframes() {
    return null;
  }

  @Override
  public int getCanvasHeight() {
    return 0;
  }

  @Override
  public int getCanvasWidth() {
    return 0;
  }

  @Override
  public int getCanvasX() {
    return 0;
  }

  @Override
  public int getCanvasY() {
    return 0;
  }
}
