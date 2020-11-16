package shop.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UIFormMenuBuilderFactoryTest {

    @Test
    public void testNullHeading() {
        UIFormMenuBuilderFactory builder = new UIFormMenuBuilderFactory();

        Assertions.assertThrows(IllegalArgumentException.class, () -> builder.toUIFormMenu(null), "Expected IllegalArgumentException");
    }

    @Test
    public void testEmptyMenu() {
        UIFormMenuBuilderFactory builder = new UIFormMenuBuilderFactory();

        Assertions.assertThrows(IllegalStateException.class, () -> builder.toUIFormMenu("Heading"), "Expected IllegalStateException");
    }

    @Test
    public void testAddingToMenu() {
        UIFormMenuBuilderFactory builder = new UIFormMenuBuilderFactory();

        builder.add((Object) "P1", (Object) "T1");
        builder.add((Object) "P2", (Object) "T2");

        UIFormMenu menu = builder.toUIFormMenu("Heading");

        Assertions.assertEquals(2, menu.size());
    }

}
