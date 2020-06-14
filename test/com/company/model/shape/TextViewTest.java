package com.company.model.shape;

import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.model.shape.shapes.Ellipse;
import com.company.view.text.TextAnimatorView;

import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TextViewTest {
  AnimatorModel testModel = new AnimatorModelImpl();
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
    testModel.setCanvasWidth(500);
    testModel.setCanvasHeight(500);
    testModel.setCanvasX(100);
    testModel.setCanvasY(100);
  }

  @Test
  public void testModelRenderString() {
    this.initTests();

    String result = "canvas 500 500 100 100\n" +
            "shape E ellipse\n" +
            "motion\tE\t10\t10\t10\t10\t20\t0\t144\t144\t\tE\t20\t20\t20\t15\t15\t144\t150\t150\n" +
            "motion\tE\t20\t20\t20\t15\t15\t144\t150\t150\t\tE\t27\t131\t20\t21\t34\t200\t200\t" +
            "200\n\n" +
            "shape R ellipse\n" +
            "motion\tR\t10\t131\t20\t21\t34\t200\t200\t200";

    Appendable output = new StringBuilder();

    try {
      new TextAnimatorView(testModel).outputText(output);
      assertEquals(result, output.toString());
    }
    catch(Exception e) {
      fail();
    }
  }
}
