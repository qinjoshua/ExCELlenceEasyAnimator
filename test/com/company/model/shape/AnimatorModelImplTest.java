package com.company.model.shape;

import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.model.shape.shapes.Ellipse;
import com.company.model.shape.shapes.Rectangle;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the animator model.
 */
public class AnimatorModelImplTest {
  AnimatorModel testModel = new AnimatorModelImpl();
  AnimatorModel testRectangleMove = new AnimatorModelImpl();

  private void initTests() {
    testModel.createKeyframe("E", new Ellipse(new PosnCart(10, 10), 10, 20, new Color(0, 144,
            144)), 10);
    testModel.createKeyframe("E", new Ellipse(new PosnCart(20, 20), 15, 15, new Color(144,
            150, 150)), 20);
    testModel.createKeyframe("E", new Ellipse(new PosnCart(131, 20), 21, 34, new Color(200,
            200, 200)), 27);

    testModel.createKeyframe("R", new Ellipse(new PosnCart(131, 20), 21, 34, new Color(200,
            200, 200)), 10);
  }

  @Test
  public void testModelRenderString() {
    this.initTests();

    String result = "shape E ellipse\n" +
            "motion\tE\t10\t10\t10\t10\t20\t0\t144\t144\t\tE\t20\t20\t20\t15\t15\t144\t150\t150\n" +
            "motion\tE\t20\t20\t20\t15\t15\t144\t150\t150\t\tE\t27\t131\t20\t21\t34\t200\t200\t" +
            "200\n\n" +
            "shape R ellipse\n" +
            "motion\tR\t10\t131\t20\t21\t34\t200\t200\t200";

    assertEquals(result, testModel.renderShapes());
  }

  @Test
  public void testGetModelFrame() {

  }

  @Test
  public void testCreateKeyframe() {
    // test that creating a keyframe over an existing keyframe overwrites it
    this.initTests();
    testModel.createKeyframe("E", new Ellipse(new PosnCart(10, 10), 10, 20, new Color(0, 144,
            144)), 10);

    String result = "shape E ellipse\n" +
            "motion\tE\t10\t10\t10\t10\t20\t0\t144\t144\t\tE\t20\t20\t20\t15\t15\t144\t150\t150\n" +
            "motion\tE\t20\t20\t20\t15\t15\t144\t150\t150\t\tE\t27\t131\t20\t21\t34\t200\t200" +
            "\t200\n\n" +
            "shape R ellipse\n" +
            "motion\tR\t10\t131\t20\t21\t34\t200\t200\t200";

    assertEquals(result, testModel.renderShapes());

    // tests an ordinary example of creating two keyframes, from start to finish
    AnimatorModel testModelCreateKeyframe = new AnimatorModelImpl();
    result = "";
    assertEquals(result, testModelCreateKeyframe.renderShapes());

    testModelCreateKeyframe.createKeyframe("E", new Ellipse(
            new PosnCart(10, 10), 10, 20, new Color(0, 144,144)), 10);
    result = "shape E ellipse\n" +
            "motion\tE\t10\t10\t10\t10\t20\t0\t144\t144";
    assertEquals(result, testModelCreateKeyframe.renderShapes());

    testModelCreateKeyframe.createKeyframe("E", new Ellipse(
            new PosnCart(20, 20), 15, 15, new Color(144,150, 150)), 20);
    result = "shape E ellipse\n" +
            "motion\tE\t10\t10\t10\t10\t20\t0\t144\t144\t\tE\t20\t20\t20\t15\t15\t144\t150\t150";
    assertEquals(result, testModelCreateKeyframe.renderShapes());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateKeyframeNegativeTime() {
    testModel.createKeyframe("R", new Ellipse(new PosnCart(131, 20), 21, 34, new Color(200,
            200, 200)), -10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateKeyframeNotSameType() {
    this.initTests();
    testModel.createKeyframe("R", new Rectangle(new PosnCart(131, 20), 21, 34, new Color(200,
            200, 200)), 10);
  }
}
