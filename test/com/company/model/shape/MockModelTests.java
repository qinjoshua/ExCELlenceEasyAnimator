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

import org.junit.Test;

import java.awt.Color;

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
    assertEquals("5 Name: mock Shape: x: 0.000000 y: 0.000000 w: 0.000000 h: 0.000000 r: 255 g: 0" +
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
            "12 Name: mock2 Shape: x: 10.000000 y: 20.000000 w: 20.000000 h: 10.000000 r: 0 g: 0" +
                    " b: 255 Tick: 12 ", out.toString());
  }

  @Test
  public void testCreateNewShape() {
    new CreateNewShape("shape-name", 21, 52, 54, 78, Color.BLUE, ShapeType.Ellipse, 11).actOn(mock);
    assertEquals(
            "Name: shape-name Shape: x: 21.000000 y: 52.000000 w: 54.000000 h: 78.000000 r: 0 g:" +
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
}
