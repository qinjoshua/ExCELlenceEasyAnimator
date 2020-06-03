package com.company.model.shape;

import com.company.model.shape.shapes.Ellipse;
import com.company.model.shape.shapes.Rectangle;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 * A class to test AShape and its implementations.
 */
public class AShapeTest {
  AShape rect1;
  AShape rect2;
  AShape ellipse1;
  AShape ellipse2;

  private void initTestData() {
    this.rect1 = new Rectangle(new PosnCart(3, 5), 5, 6, Color.BLUE);
    this.rect2 = new Rectangle(new PosnCart(17, 2), 4, 3, Color.GREEN);
    this.ellipse1 = new Ellipse(new PosnCart(10, 10), 5, 5, Color.RED);
    this.ellipse2 = new Ellipse(new PosnCart(20, 5), 7, 3, Color.MAGENTA);
  }

  @Test
  public void getPosn() {
    this.initTestData();
    assertEquals(new PosnCart(3, 5), rect1.getPosition());
    assertEquals(new PosnCart(17, 2), rect2.getPosition());
    assertEquals(new PosnCart(10, 10), ellipse1.getPosition());
    assertEquals(new PosnCart(20, 5), ellipse2.getPosition());
  }

  @Test
  public void getColor() {
    this.initTestData();
    assertEquals(Color.BLUE, rect1.getColor());
    assertEquals(Color.GREEN, rect2.getColor());
    assertEquals(Color.RED, ellipse1.getColor());
    assertEquals(Color.MAGENTA, ellipse2.getColor());
  }

  @Test
  public void getShapeType_customConstructors() {
    this.initTestData();
    assertEquals(ShapeType.Rectangle, rect1.getShapeType());
    assertEquals(ShapeType.Rectangle, rect2.getShapeType());
    assertEquals(ShapeType.Ellipse, ellipse1.getShapeType());
    assertEquals(ShapeType.Ellipse, ellipse2.getShapeType());
  }

  @Test
  public void getWidth() {
    this.initTestData();
    assertEquals(5, rect1.getWidth(), 0.01);
    assertEquals(4, rect2.getWidth(), 0.01);
    assertEquals(5, ellipse1.getWidth(), 0.01);
    assertEquals(7, ellipse2.getWidth(), 0.01);
  }

  @Test
  public void getHeight() {
    this.initTestData();
    assertEquals(6, rect1.getHeight(), 0.01);
    assertEquals(3, rect2.getHeight(), 0.01);
    assertEquals(5, ellipse1.getHeight(), 0.01);
    assertEquals(3, ellipse2.getHeight(), 0.01);
  }

  //TODO test equality
}