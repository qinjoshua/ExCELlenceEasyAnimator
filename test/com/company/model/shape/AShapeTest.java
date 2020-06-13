package com.company.model.shape;

import com.company.model.shape.shapes.Ellipse;
import com.company.model.shape.shapes.Rectangle;

import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * A class to test AShape and its implementations.
 */
public class AShapeTest {
  AShape rect1;
  AShape rect2;
  AShape ellipse1;
  AShape ellipse2;

  private void initTestData() {
    this.rect1 = new Rectangle(new PosnImpl(3, 5), 5, 6, Color.BLUE);
    this.rect2 = new Rectangle(new PosnImpl(17, 2), 4, 3, Color.GREEN);
    this.ellipse1 = new Ellipse(new PosnImpl(10, 10), 5, 5, Color.RED);
    this.ellipse2 = new Ellipse(new PosnImpl(20, 5), 7, 3, Color.MAGENTA);
  }

  @Test
  public void getPosn() {
    this.initTestData();
    assertEquals(new PosnImpl(3, 5), rect1.getPosition());
    assertEquals(new PosnImpl(17, 2), rect2.getPosition());
    assertEquals(new PosnImpl(10, 10), ellipse1.getPosition());
    assertEquals(new PosnImpl(20, 5), ellipse2.getPosition());
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

  @Test
  public void equals_notReferential() {
    this.initTestData();
    Rectangle rectCopy1 = new Rectangle(new PosnImpl(3, 5), 5, 6, Color.BLUE);
    assertEquals(rectCopy1, this.rect1);
  }

  @Test
  public void equals_shapeTypeMatters() {
    this.initTestData();
    Ellipse ellipseRect1 = new Ellipse(new PosnImpl(3, 5), 5, 6, Color.BLUE);
    assertNotEquals(ellipseRect1, this.rect1);
  }

  @Test
  public void hashCode_notReferential() {
    this.initTestData();
    Rectangle rectCopy1 = new Rectangle(new PosnImpl(3, 5), 5, 6, Color.BLUE);
    assertEquals(rectCopy1.hashCode(), this.rect1.hashCode());
  }

  @Test
  public void hashCode_shapeTypeMatters() {
    this.initTestData();
    Ellipse ellipseRect1 = new Ellipse(new PosnImpl(3, 5), 5, 6, Color.BLUE);
    assertNotEquals(ellipseRect1.hashCode(), this.rect1.hashCode());
  }

  @Test
  public void interpolate_happyPath() {
    this.initTestData();
    Rectangle interpolated = new Rectangle(
        new PosnImpl(10, 3.5), 4.5, 4.5, new Color(0f, 0.5f, 0.5f));
    assertEquals(interpolated, rect1.interpolate(rect2, 0.5));
    assertEquals(interpolated, rect2.interpolate(rect1, 0.5));
    assertEquals(rect1, rect1.interpolate(rect2, 0));
    assertEquals(rect1, rect2.interpolate(rect1, 1));
    Ellipse interpolatedUneven = new Ellipse(
        new PosnImpl(12, 9), 5.4, 4.6, new Color(255, 0, 51));
    assertEquals(interpolatedUneven, ellipse1.interpolate(ellipse2, 0.2));
    assertEquals(interpolatedUneven, ellipse2.interpolate(ellipse1, 0.8));
  }

  @Test(expected = IllegalArgumentException.class)
  public void interpolate_tooSmallProgress() {
    this.initTestData();
    rect1.interpolate(rect2, -0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void interpolate_tooBigProgress() {
    this.initTestData();
    rect1.interpolate(rect2, 1.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void interpolate_wrongType() {
    this.initTestData();
    rect1.interpolate(ellipse1, 0.5);
  }
}