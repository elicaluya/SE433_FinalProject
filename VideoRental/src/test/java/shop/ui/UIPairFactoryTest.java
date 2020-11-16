package shop.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UIPairFactoryTest {

    @Test
    public void testPairP() {
        UIPairFactory pair = new UIPairFactory("P", "T");

        Assertions.assertEquals("P", pair.getP());
    }

    @Test
    public void testPairT() {
        UIPairFactory pair = new UIPairFactory("P", "T");

        Assertions.assertEquals("T", pair.getT());
    }

}
