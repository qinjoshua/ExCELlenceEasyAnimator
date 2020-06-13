package com.company.model.shape;

import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.model.shape.shapes.Rectangle;
import com.company.view.SVG.SVGViewImpl;
import com.company.view.SVGView;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SVGViewImplTest {
  AnimatorModel model = new AnimatorModelImpl();
  AnimatorModel model2 = new AnimatorModelImpl();

  AnimatorModel smallModelManyAnimations = new AnimatorModelImpl();
  AnimatorModel smallModelManyShapes = new AnimatorModelImpl();

  private void initTests() {
    model.createKeyframe("c", new Rectangle(new PosnCart(100, 100), 150, 150, Color.RED), 0);
    model.createKeyframe("c", new Rectangle(new PosnCart(200, 200), 150, 150, Color.GREEN), 10);
    model.createKeyframe("c", new Rectangle(new PosnCart(200, 100), 150, 150, Color.BLUE), 20);

    model2.createKeyframe("dID", new Rectangle(new PosnCart(100, 100), 150, 150, Color.RED), 0);
    model2.createKeyframe("aID", new Rectangle(new PosnCart(200, 100), 150, 150, Color.RED), 0);
    model2.createKeyframe("aID", new Rectangle(new PosnCart(200, 100), 150, 150, Color.BLUE), 10);
    model2.createKeyframe("cID", new Rectangle(new PosnCart(200, 100), 150, 150, Color.BLUE), 10);

    smallModelManyAnimations.createKeyframe("c", new Rectangle(new PosnCart(100, 100), 150, 150,
            Color.RED), 0);
    smallModelManyAnimations.createKeyframe("c", new Rectangle(new PosnCart(120, 130), 100, 129,
            Color.BLUE),20);

    smallModelManyShapes.createKeyframe("c", new Rectangle(new PosnCart(100, 100), 150, 150,
            Color.RED),0);
    smallModelManyShapes.createKeyframe("c", new Rectangle(new PosnCart(120, 100), 150, 150,
            Color.RED),30);
    smallModelManyShapes.createKeyframe("z", new Rectangle(new PosnCart(150, 150), 100, 150,
            Color.GREEN), 0);
    smallModelManyShapes.createKeyframe("z", new Rectangle(new PosnCart(150, 150), 100, 150,
            Color.YELLOW), 50);
    smallModelManyShapes.createKeyframe("a", new Rectangle(new PosnCart(50, 10), 211, 250,
            Color.GREEN), 0);
    smallModelManyShapes.createKeyframe("a", new Rectangle(new PosnCart(50, 100), 211, 350,
            Color.GREEN), 20);
  }

  @Test
  public void testSVGAnimatorViewOutputProperties() {
    this.initTests();
    SVGView view = new SVGViewImpl(this.model);
    try {
      Appendable appendable = new StringBuilder();
      view.outputSVG(appendable);

      String output = appendable.toString();
      String[] outputLines = output.split("\n");

      // Check the number of animate tags is as expected
      assertEquals(5, output.split("animate", -1).length - 1);
      // Check that the last line is the closing tag for svg
      assertEquals("</svg>", outputLines[outputLines.length - 1]);
      // Check that the rect is only referenced twice
      assertEquals(2, output.split("rect", -1).length - 1);
      // Check that the durations fit the 1 second per frame
      assertEquals(5, output.split("10000.0ms", -1).length - 1);
      assertEquals(2, output.split("20000.0ms", -1).length - 1);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testSVGAnimatorViewTestCustomSpeed() {
    this.initTests();
    SVGView view = new SVGViewImpl(this.model, 10);
    try {
      Appendable appendable = new StringBuilder();
      view.outputSVG(appendable);

      String output = appendable.toString();
      String[] outputLines = output.split("\n");

      // Check that nothing else has changed
      assertEquals(5, output.split("animate", -1).length - 1);
      assertEquals("</svg>", outputLines[outputLines.length - 1]);
      assertEquals(2, output.split("rect", -1).length - 1);

      // Check that the durations match the 10 fps
      assertEquals(5, output.split("1000.0ms", -1).length - 1);
      assertEquals(2, output.split("2000.0ms", -1).length - 1);

    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testSVGAnimatorViewOutputOne() {
    this.initTests();
    SVGView view = new SVGViewImpl(this.smallModelManyAnimations);
    try {
      Appendable appendable = new StringBuilder();
      view.outputSVG(appendable);

      String expectedOutput = "<svg width=\"640\" height=\"400\" version=\"1.1\" " +
              "xmlns=\"http://www.w3.org/2000/svg\">\n" +
              "<rect id=\"c\" fill=\"rgb(377, 0, 0)\" visibility=\"visible\" x=\"100.0\" y=\"100" +
              ".0\" width=\"150.0\" height=\"150.0\">\n" +
              "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"20000.0ms\" " +
              "attributeName=\"x\" from=\"100.0\" to=\"120.0\" fill=\"freeze\" />\n" +
              "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"20000.0ms\" " +
              "attributeName=\"y\" from=\"100.0\" to=\"130.00\" fill=\"freeze\" />\n" +
              "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"20000.0ms\" " +
              "attributeName=\"width\" from=\"150.0\" to=\"100.0\" fill=\"freeze\" />\n" +
              "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"20000.0ms\" " +
              "attributeName=\"height\" from=\"150.0\" to=\"129.0\" fill=\"freeze\" />\n" +
              "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"20000.0ms\" " +
              "attributeName=\"fill\" from=\"rgb(377, 0, 0)\" to=\"rgb(0, 0, 377)\" " +
              "fill=\"freeze\" />\n" +
              "</rect>\n" +
              "</svg>\n";

      String output = appendable.toString();
      assertEquals(expectedOutput, output);

    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testSVGAnimatorViewOutputTwo() {
    this.initTests();
    SVGView view = new SVGViewImpl(this.smallModelManyShapes);
    try {
      Appendable appendable = new StringBuilder();
      view.outputSVG(appendable);

      String expectedOutput = "<svg width=\"640\" height=\"400\" version=\"1.1\" " +
              "xmlns=\"http://www.w3.org/2000/svg\">\n" +
              "<rect id=\"c\" fill=\"rgb(377, 0, 0)\" visibility=\"visible\" x=\"100.0\" y=\"100" +
              ".0\" width=\"150.0\" height=\"150.0\">\n" +
              "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"30000.0ms\" " +
              "attributeName=\"x\" from=\"100.0\" to=\"120.0\" fill=\"freeze\" />\n" +
              "</rect>\n" +
              "<rect id=\"z\" fill=\"rgb(0, 377, 0)\" visibility=\"visible\" x=\"150.0\" y=\"150" +
              ".0\" width=\"100.0\" height=\"150.0\">\n" +
              "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"50000.0ms\" " +
              "attributeName=\"fill\" from=\"rgb(0, 377, 0)\" to=\"rgb(377, 377, 0)\" " +
              "fill=\"freeze\" />\n" +
              "</rect>\n" +
              "<rect id=\"a\" fill=\"rgb(0, 377, 0)\" visibility=\"visible\" x=\"50.0\" y=\"10" +
              ".0\" width=\"211.0\" height=\"250.0\">\n" +
              "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"20000.0ms\" " +
              "attributeName=\"y\" from=\"10.0\" to=\"100.00\" fill=\"freeze\" />\n" +
              "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"20000.0ms\" " +
              "attributeName=\"height\" from=\"250.0\" to=\"350.0\" fill=\"freeze\" />\n" +
              "</rect>\n" +
              "</svg>\n";

      String output = appendable.toString();
      assertEquals(expectedOutput, output);

    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCustomOrigin() {
    AnimatorModel testModel =
            new AnimatorModelImpl.Builder().setBounds(20, 30, 640, 400).build();

    testModel.createKeyframe("dID", new Rectangle(new PosnCart(100, 100), 150, 150, Color.RED), 0);

    SVGView view = new SVGViewImpl(testModel);

    try {
      Appendable appendable = new StringBuilder();
      view.outputSVG(appendable);

      String expectedOutput = "<svg width=\"640\" height=\"400\" version=\"1.1\" " +
              "xmlns=\"http://www.w3.org/2000/svg\">\n" +
              "<rect id=\"dID\" fill=\"rgb(377, 0, 0)\" visibility=\"visible\" x=\"120.0\" " +
              "y=\"130.0\" width=\"150.0\" height=\"150.0\">\n" +
              "</rect>\n" +
              "</svg>\n";

      String output = appendable.toString();
      assertEquals(expectedOutput, output);

    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testCustomCanvasSize() {
    ReadOnlyAnimatorModel testModel =
            new AnimatorModelImpl.Builder().setBounds(0, 0, 548, 567).build();
    SVGView view = new SVGViewImpl(testModel);
    try {
      Appendable appendable = new StringBuilder();
      view.outputSVG(appendable);

      String output = appendable.toString();
      String[] outputLines = output.split("\n");

      // Check that the width and height shows up in the first line
      assertTrue(outputLines[0].indexOf("width=\"548\"") > 0);
      assertTrue(outputLines[0].indexOf("height=\"567\"") > 0);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testSVGAnimatorViewOutputCorrectOrder() {

    // tests that the d comes before the a before the c, in the correct order that they were
    // inputed in the model

    this.initTests();
    SVGView view = new SVGViewImpl(this.model2);
    try {
      Appendable appendable = new StringBuilder();
      view.outputSVG(appendable);

      String output = appendable.toString();

      assertTrue(output.indexOf("dID") > -1);

      while (output.indexOf("dID") > -1) {
        output = output.substring(output.indexOf("dID") + 4);
      }

      assertTrue(output.indexOf("aID") > -1);

      while (output.indexOf("aID") > -1) {
        output = output.substring(output.indexOf("aID") + 4);
      }

      assertTrue(output.indexOf("cID") > -1);
    } catch (Exception e) {
      fail();
    }
  }
}
