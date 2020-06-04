package com.company.model.shape;

import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.model.shape.shapes.Ellipse;

import org.junit.Test;

import java.awt.*;

public class AnimatorModelImplTest {
  AnimatorModel testModel = new AnimatorModelImpl();

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
    System.out.println(testModel.renderShapes());
  }

}
