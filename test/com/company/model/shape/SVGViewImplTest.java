package com.company.model.shape;

import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.model.shape.shapes.Rectangle;
import com.company.view.SVG.SVGViewImpl;
import com.company.view.SVGView;

import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SVGViewImplTest {
  AnimatorModel model = new AnimatorModelImpl();

  private void initTests() {
    model.createKeyframe("c", new Rectangle(new PosnCart(100, 100), 150, 150, Color.RED), 0);
    model.createKeyframe("c", new Rectangle(new PosnCart(200, 200), 150, 150, Color.GREEN), 10);
    model.createKeyframe("c", new Rectangle(new PosnCart(200, 100), 150, 150, Color.BLUE), 20);
    model.createKeyframe("d", new Rectangle(new PosnCart(100, 100), 150, 150, Color.RED), 0);
    model.createKeyframe("a", new Rectangle(new PosnCart(200, 100), 150, 150, Color.RED), 0);
    model.createKeyframe("a", new Rectangle(new PosnCart(200, 100), 150, 150, Color.BLUE), 10);
  }

  @Test
  public void testSVGAnimatorViewOutput() {
    this.initTests();
    SVGView view = new SVGViewImpl(this.model, 500, 500);
    try {
      Appendable appendable = new StringBuilder();
      view.outputSVG(appendable);

      String output = appendable.toString();

      assertEquals(5, output.split("animate", -1).length-1);

    }
    catch(Exception e) {
      fail();
    }
  }
}
