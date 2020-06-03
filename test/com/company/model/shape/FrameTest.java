package com.company.model.shape;

import com.company.model.Frame;
import com.company.model.FrameImpl;
import com.company.model.shape.shapes.Ellipse;
import com.company.model.shape.shapes.Rectangle;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 * Tests for implementations of Frame.
 */
public class FrameTest {
  Frame testFrame = new FrameImpl(3, new Ellipse(new PosnCart(15, 25), 10, 15, Color.RED));
  Frame testFrame2 = new FrameImpl(1, new Rectangle(new PosnCart(20, 20), 20, 20, Color.BLUE));
  Frame testFrame3 = new FrameImpl(5, new Rectangle(new PosnCart(17, 13), 12, 14, Color.GREEN));
  Frame testFrame4 = new FrameImpl(5, new Ellipse(new PosnCart(20, 30), 13, 17, Color.RED));

  // Test that frame implementation will initialize with expected parameters
  @Test
  public void testInitializeFrameImpl() {
    Frame frame = new FrameImpl(1, new Ellipse(new PosnCart(20, 20), 20, 20, Color.BLUE));

    // Some basic sanity checks to ensure that it initialized properly
    assertEquals(1.0, frame.getTime(), 0.01);
    assertEquals(new PosnCart(20, 20), frame.getShape().getPosition());
  }

  // Test that frame throws an error when given negative time
  @Test(expected = IllegalArgumentException.class)
  public void testInitializeFrameImplNegativeTime() {
    Frame frame = new FrameImpl(-1, new Ellipse(new PosnCart(20, 20), 20, 20, Color.BLUE));
  }

  // Test get time
  public void testGetTime() {
    assertEquals(3, testFrame.getTime(), 0.01);
    assertEquals(1, testFrame2.getTime(), 0.01);
  }

  // Test get shape
  public void testGetShape() {
    assertEquals(new Ellipse(new PosnCart(15, 25), 10, 15, Color.RED), testFrame.getShape());
    assertEquals(new Ellipse(new PosnCart(15, 25), 10, 15, Color.RED), testFrame.getShape());
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
    assertEquals(testFrame2.getShape(), testFrame2.interpolateFrame(testFrame3, 0));

    assertEquals(testFrame3.getShape(), testFrame2.interpolateFrame(testFrame3, 1));

    System.out.println(testFrame.interpolateFrame(testFrame4, 0.7).getColor().getRed());
    System.out.println(testFrame.interpolateFrame(testFrame4, 0.7).getColor().getGreen());
    System.out.println(testFrame.interpolateFrame(testFrame4, 0.7).getColor().getBlue());

    System.out.println(testFrame.interpolateFrame(testFrame4, 0.7).getWidth());
    System.out.println(testFrame.interpolateFrame(testFrame4, 0.7).getHeight());
    System.out.println(testFrame.interpolateFrame(testFrame4, 0.7).getPosition().getX());
    System.out.println(testFrame.interpolateFrame(testFrame4, 0.7).getPosition().getY());

    assertEquals(new Rectangle(new PosnCart(18.5, 16.5), 16, 17, new Color(0, 127, 127)),
            testFrame2.interpolateFrame(testFrame3, 0.5));

    assertEquals(new Ellipse(new PosnCart(18.5, 28.5), 12.1, 16.4, new Color(255, 0, 0)),
            testFrame.interpolateFrame(testFrame4, 0.7));
  }

  // Test negative progress
  @Test(expected = IllegalArgumentException.class)
  public void testInterpolationNegativeProgress() {
    testFrame2.interpolateFrame(testFrame3, -1);
  }

  // Test progress out of range
  @Test(expected = IllegalArgumentException.class)
  public void testInterpolationGreaterThanOne() {
    testFrame2.interpolateFrame(testFrame3, 1.1);
  }

  // Test to frame comes before from frame
  @Test(expected = IllegalArgumentException.class)
  public void testInterpolationToFrameBefore() {
    testFrame3.interpolateFrame(testFrame2, 0.5);
  }
}
