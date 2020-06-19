package com.company.model.shape;

import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.model.shape.shapes.Ellipse;
import com.company.model.shape.shapes.Rectangle;

import org.junit.Test;

import java.awt.Color;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the animator model.
 */
public class AnimatorModelImplTest {
  final AnimatorModel testModel = new AnimatorModelImpl();
  AnimatorModel testRectangleMove = new AnimatorModelImpl();

  private void initTests() {
    testModel.createKeyframe("E", new Ellipse(new PosnImpl(10, 10), 10, 20, new Color(0, 144,
        144)), 10);
    testModel.createKeyframe("E", new Ellipse(new PosnImpl(20, 20), 15, 15, new Color(144,
        150, 150)), 20);
    testModel.createKeyframe("E", new Ellipse(new PosnImpl(131, 20), 21, 34, new Color(200,
        200, 200)), 27);

    testModel.createKeyframe("R", new Ellipse(new PosnImpl(131, 20), 21, 34, new Color(200,
        200, 200)), 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateKeyframeNegativeTime() {
    testModel.createKeyframe("R", new Ellipse(new PosnImpl(131, 20), 21, 34, new Color(200,
        200, 200)), -10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateKeyframeNotSameType() {
    this.initTests();
    testModel.createKeyframe("R", new Rectangle(new PosnImpl(131, 20), 21, 34, new Color(200,
        200, 200)), 10);
  }

  @Test
  public void shapesAt_happyPath() {
    this.initTests();
    Map<String, Shape> shapes12 = testModel.shapesAt(12);
    assertEquals(2, shapes12.entrySet().size());
    Shape e12 = shapes12.get("E");
    Shape r12 = shapes12.get("R");
    assertEquals(new Ellipse(
        new PosnImpl(12, 12),
        11, 19,
        new Color(29, 145, 145)), e12);
    assertEquals(r12, testModel.shapesAt(10).get("R"));
    assertEquals(0, testModel.shapesAt(9).size());
    assertEquals(0, testModel.shapesAt(0).size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void shapesAt_timeNegative() {
    this.initTests();
    testModel.shapesAt(-1);
  }
}
