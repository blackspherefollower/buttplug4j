package io.github.blackspherefollower.buttplug4j.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PairTest {

    @Test
    public void testPairCreation() {
        Pair<String, Integer> pair = new Pair<>("test", 42);
        
        assertEquals("test", pair.getLeft());
        assertEquals(42, pair.getRight());
    }

    @Test
    public void testPairWithNullValues() {
        Pair<String, Integer> pair = new Pair<>(null, null);
        
        assertNull(pair.getLeft());
        assertNull(pair.getRight());
    }

    @Test
    public void testPairSetters() {
        Pair<String, Integer> pair = new Pair<>("initial", 1);
        
        pair.setLeft("updated");
        pair.setRight(2);
        
        assertEquals("updated", pair.getLeft());
        assertEquals(2, pair.getRight());
    }

    @Test
    public void testPairEquality() {
        Pair<String, Integer> pair1 = new Pair<>("test", 42);
        Pair<String, Integer> pair2 = new Pair<>("test", 42);
        Pair<String, Integer> pair3 = new Pair<>("different", 42);
        
        assertEquals(pair1, pair2);
        assertNotEquals(pair1, pair3);
    }

    @Test
    public void testPairHashCode() {
        Pair<String, Integer> pair1 = new Pair<>("test", 42);
        Pair<String, Integer> pair2 = new Pair<>("test", 42);
        
        assertEquals(pair1.hashCode(), pair2.hashCode());
    }

    @Test
    public void testPairToString() {
        Pair<String, Integer> pair = new Pair<>("test", 42);
        String str = pair.toString();
        
        assertNotNull(str);
        assertTrue(str.contains("test"));
        assertTrue(str.contains("42"));
    }

    @Test
    public void testPairWithDifferentTypes() {
        Pair<Double, Boolean> pair = new Pair<>(3.14, true);
        
        assertEquals(3.14, pair.getLeft());
        assertEquals(true, pair.getRight());
    }
}
