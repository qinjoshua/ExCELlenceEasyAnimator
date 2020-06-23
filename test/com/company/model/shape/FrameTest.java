package com.company.model.shape;

import com.company.model.AnimatorModel;
import com.company.model.Frame;
import com.company.model.FrameImpl;
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
 * Tests for implementations of Frame.
 */
public class FrameTest {
  final Frame testFrame =
          new FrameImpl(3, new Ellipse(new PosnImpl(15, 25), 10, 15, Color.RED));
  final Frame testFrame2 =
          new FrameImpl(1, new Rectangle(new PosnImpl(20, 20), 20, 20, Color.BLUE));
  final Frame testFrame3 =
          new FrameImpl(5, new Rectangle(new PosnImpl(17, 13), 12, 14, Color.GREEN));
  final Frame testFrame4 =
          new FrameImpl(5, new Ellipse(new PosnImpl(20, 30), 13, 17, Color.RED));

  // Test that frame implementation will initialize with expected parameters
  @Test
  public void testInitializeFrameImpl() {
    Frame frame = new FrameImpl(1, new Ellipse(new PosnImpl(20, 20), 20, 20, Color.BLUE));

    // Some basic sanity checks to ensure that it initialized properly
    assertEquals(1.0, frame.getTime(), 0.01);
    assertEquals(new PosnImpl(20, 20), frame.getShape().getPosition());
  }

  // Test that frame throws an error when given negative time
  @Test(expected = IllegalArgumentException.class)
  public void testInitializeFrameImplNegativeTime() {
    Frame frame = new FrameImpl(-1, new Ellipse(new PosnImpl(20, 20), 20, 20, Color.BLUE));
  }

  // Test get time
  @Test
  public void testGetTime() {
    assertEquals(3, testFrame.getTime(), 0.01);
    assertEquals(1, testFrame2.getTime(), 0.01);
  }

  // Test get shape
  @Test
  public void testGetShape() {
    assertEquals(new Ellipse(new PosnImpl(15, 25), 10, 15, Color.RED), testFrame.getShape());
    assertEquals(new Ellipse(new PosnImpl(15, 25), 10, 15, Color.RED), testFrame.getShape());
  }

  // Test compareTo
  @Test
  public void testCompareTo() {
    assertEquals(1, testFrame.compareTo(testFrame2));
    assertEquals(-1, testFrame2.compareTo(testFrame));
    assertEquals(0, testFrame.compareTo(testFrame));

    assertEquals(-1, testFrame2.compareTo(testFrame3));
    assertEquals(1, testFrame3.compareTo(testFrame));
  }

  // Test interpolation
  @Test
  public void testInterpolation() {
    assertEquals(testFrame2.getShape(), testFrame2.interpolateShape(testFrame3, 0));

    assertEquals(testFrame3.getShape(), testFrame2.interpolateShape(testFrame3, 1));

    // Tests correct interpolation of rectangles, going from
    assertEquals(new Rectangle(new PosnImpl(18.5, 16.5), 16, 17, new Color(0, 128, 128)),
        testFrame2.interpolateShape(testFrame3, 0.5));

    assertEquals(new Ellipse(new PosnImpl(18.5, 28.5), 12.1, 16.4, new Color(255, 0, 0)),
        testFrame.interpolateShape(testFrame4, 0.7));
  }

  // Test negative progress
  @Test(expected = IllegalArgumentException.class)
  public void testInterpolationNegativeProgress() {
    testFrame2.interpolateShape(testFrame3, -1);
  }

  // Test progress out of range
  @Test(expected = IllegalArgumentException.class)
  public void testInterpolationGreaterThanOne() {
    testFrame2.interpolateShape(testFrame3, 1.1);
  }

  // Test to frame comes before from frame
  @Test(expected = IllegalArgumentException.class)
  public void testInterpolationToFrameBefore() {
    testFrame3.interpolateShape(testFrame2, 0.5);
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
    public void setCanvasWidth(int canvasWidth) {
      try {
        out.append(String.valueOf(canvasWidth)).append(" ");
      } catch (IOException e) {
        e.printStackTrace();
      }
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
    public void setCanvasX(int canvasX) {
      try {
        out.append(String.valueOf(canvasX)).append(" ");
      } catch (IOException e) {
        e.printStackTrace();
      }
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
