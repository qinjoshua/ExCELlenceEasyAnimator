package com.company.model.shape;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A class to test {@link Posn}, specifically its constructor, equality, and hash code.
 */
public class PosnTest {

  /**
   * Check that referential equality isn't being used.
   */
  @Test
  public void equals_notReferential() {
    Posn posn1 = new Posn(3, 5);
    Posn posn2 = new Posn(3, 5);
    Posn posn3 = new Posn(5, 3);
    assertEquals(posn1, posn2);
    assertEquals(posn1, posn1);
    assertNotEquals(posn1, posn3);
  }
  /**
   * Check that referential equality isn't being used for hashes.
   */
  @Test
  public void hashCode_notReferential() {
    Posn posn1 = new Posn(3, 5);
    Posn posn2 = new Posn(3, 5);
    Posn posn3 = new Posn(15, 20);
    assertEquals(posn1.hashCode(), posn2.hashCode());
    assertEquals(posn1.hashCode(), posn1.hashCode());
    assertNotEquals(posn1.hashCode(), posn3.hashCode());
  }
}