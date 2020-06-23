package com.company.model.shape;

import com.company.controller.animatoractions.ChangeColor;
import com.company.controller.animatoractions.ChangeHeight;
import com.company.controller.animatoractions.ChangeWidth;
import com.company.controller.animatoractions.ChangeX;
import com.company.controller.animatoractions.ChangeY;
import com.company.controller.animatoractions.CreateKeyframe;
import com.company.controller.animatoractions.CreateNewShape;
import com.company.controller.animatoractions.DeleteShape;
import com.company.controller.animatoractions.RemoveKeyframe;
import com.company.model.AnimatorModel;
import com.company.model.Frame;
import com.company.model.shape.shapes.Ellipse;
import com.company.model.shape.shapes.Rectangle;

import org.junit.Test;

import java.awt.Color;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import static org.junit.Assert.assertEquals;

/**
 * Tests that the model is receiving the correct information from the controller when various
 * commands are called.
 */
public class MockModelTests {
  final Appendable out = new StringBuilder();
  final AnimatorModel mock = new MockModel(out);

  @Test
  public void testChangeColor() {
    new ChangeColor("mock", 5, Color.RED).actOn(mock);
    assertEquals("5 Name: mock Shape: x: 0.000000 y: 0.000000"
            + " w: 0.000000 h: 0.000000 r: 255 " +
            "g: 0" +
            " b: 0 Tick: 5 ", out.toString());
  }

  @Test
  public void testChangeHeight() {
    new ChangeHeight("mock", 10, 20).actOn(mock);
    assertEquals(
            "10 Name: mock Shape: x: 0.000000 y: 0.000000 w: 0.000000 h: 20.000000"
                    + " r: 0 g: 255 b: 0 Tick: 10 ", out.toString());
  }

  @Test
  public void testChangeWidth() {
    new ChangeWidth("mock", 11, 15).actOn(mock);
    assertEquals(
            "11 Name: mock Shape: x: 0.000000 y: 0.000000 w: 15.000000 h: 0.000000"
                    + " r: 0 g: 255 b: 0 Tick: 11 ", out.toString());
  }

  @Test
  public void testChangeX() {
    new ChangeX("mock", 15, 30).actOn(mock);
    assertEquals(
            "15 Name: mock Shape: x: 30.000000 y: 0.000000 w: 0.000000 h: 0.000000"
                    + " r: 0 g: 255 b: 0 Tick: 15 ", out.toString());
  }

  @Test
  public void testChangeY() {
    new ChangeY("mock", 20, 35).actOn(mock);
    assertEquals(
            "20 Name: mock Shape: x: 0.000000 y: 35.000000 w: 0.000000 h: 0.000000"
                    + " r: 0 g: 255 b: 0 Tick: 20 ", out.toString());
  }

  @Test
  public void testCreateKeyframe() {
    new CreateKeyframe("mock2", 12).actOn(mock);
    assertEquals(
            "12 12 Name: mock2 Shape: x: 10.000000 y: " +
                    "20.000000 w: 20.000000 h: 10.000000 r: 0" +
                    " " +
                    "g: 0 b: 255 Tick: 12 ", out.toString());
  }

  @Test
  public void testCreateNewShape() {
    new CreateNewShape(
            "shape-name", 21, 52, 54,
            78, Color.BLUE, ShapeType.Ellipse, 11).actOn(mock);
    assertEquals(
            "Name: shape-name Shape: x: 21.000000 y:" +
                    " 52.000000 w: 54.000000 h: 78.000000 r: 0" +
                    " g:" +
                    " 0 b: 255 Tick: 11 ", out.toString());
  }

  @Test
  public void testDeleteShape() {
    new DeleteShape("shape-name").actOn(mock);
    assertEquals(
            "shape-name", out.toString());
  }

  @Test
  public void testRemoveKeyframe() {
    new RemoveKeyframe("to-be-removed", 11).actOn(mock);
    assertEquals(
            "Name: to-be-removed Tick: 11 ", out.toString());
  }

  /**
   * Mock model used for the purpose of testing.
   */
  private class MockModel implements AnimatorModel {
    final Appendable out;

    /**
     * Initializes the appendable out where the testing strings are to be written to.
     *
     * @param out where the test results are written to
     */
    public MockModel(Appendable out) {
      this.out = out;
    }

    private String shapeToString(Shape shape) {
      return String.format("x: %f y: %f w: %f h: %f r: %d g: %d b: %d",
              shape.getPosition().getX(), shape.getPosition().getY(),
              shape.getWidth(), shape.getHeight(),
              shape.getColor().getRed(), shape.getColor().getGreen(), shape.getColor().getBlue());
    }

    @Override
    public void createKeyframe(String shapeName, Shape shape, int tick)
            throws IllegalArgumentException {
      try {
        out.append("Name: ").append(shapeName).append(" Shape: ").append(this.shapeToString(shape)).
                append(" Tick: ").append(String.valueOf(tick)).append(" ");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void removeKeyframe(String shapeName, int tick) {
      try {
        out.append("Name: ").append(shapeName).append(" Tick: ").
                append(String.valueOf(tick)).append(" ");
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
    public void moveLayerUp(String layerName) {

    }

    @Override
    public void moveLayerDown(String layerName) {

    }

    @Override
    public void addLayer(String layerName) {

    }

    @Override
    public void deleteLayer(String layerName) {

    }

    @Override
    public Map<String, Shape> shapesAt(int tick) throws IllegalArgumentException {
      try {
        out.append(String.valueOf(tick)).append(" ");
      } catch (IOException e) {
        e.printStackTrace();
      }
      Map<String, Shape> returnMap = new HashMap<>();
      returnMap.put("mock", new Rectangle(new PosnImpl(0, 0), 0, 0, Color.GREEN));
      returnMap.put("mock2", new Ellipse(new PosnImpl(10, 20), 20, 10, Color.BLUE));

      return returnMap;
    }

    @Override
    public Collection<String> getLayers() {
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
    public void setCanvasHeight(int canvasHeight) {
      try {
        out.append(String.valueOf(canvasHeight)).append(" ");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public int getCanvasWidth() {
      return 0;
    }

    @Override
    public void setCanvasWidth(int canvasWidth) {
      try {
        out.append(String.valueOf(canvasWidth)).append(" ");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public int getCanvasX() {
      return 0;
    }

    @Override
    public void setCanvasX(int canvasX) {
      try {
        out.append(String.valueOf(canvasX)).append(" ");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public int getCanvasY() {
      return 0;
    }

    @Override
    public void setCanvasY(int canvasY) {
      try {
        out.append(String.valueOf(canvasY)).append(" ");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public double lastTick() {
      return 0;
    }

    @Override
    public void createKeyframe(String shapeName, Shape shape, int tick, String layerName) throws IllegalArgumentException {
      createKeyframe(shapeName, shape, tick);
    }

    @Override
    public List<String> getShapesInLayer(String layerName) {
      return null;
    }
  }
}
