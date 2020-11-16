package shop.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UIFormMenuFactoryTest {

    private UIFormTest stringTest = new UIFormTest() {
        public boolean run(String input) {
            return ! "".equals(input.trim());
        }
    };

    @Test
    public void testSize() {
        UIPair[] array = new UIPair[2];

        array[0] = new UIPairFactory("P1", "T1");
        array[1] = new UIPairFactory("P2", "T2");

        UIFormMenuFactory menu = new UIFormMenuFactory("Heading", array);

        Assertions.assertEquals(2, menu.size());
    }

    @Test
    public void testHeading() {
        UIPair[] array = new UIPair[0];

        UIFormMenuFactory menu = new UIFormMenuFactory("Heading", array);

        Assertions.assertEquals("Heading", menu.getHeading());
    }

    @Test
    public void testP() {
        UIPair[] array = new UIPair[1];

        array[0] = new UIPairFactory("P1", "T1");

        UIFormMenuFactory menu = new UIFormMenuFactory("Heading", array);

        Assertions.assertEquals("P1", menu.getP(0));
    }

    @Test
    public void testT() {
        UIPair[] array = new UIPair[1];

        array[0] = new UIPairFactory("P1", "T1");

        UIFormMenuFactory menu = new UIFormMenuFactory("Heading", array);

        Assertions.assertEquals("T1", menu.getT(0));
    }

    @Test
    public void testCheckInputNull() {
        UIPair[] array = new UIPair[1];

        array[0] = null;

        UIFormMenuFactory menu = new UIFormMenuFactory("Heading", array);

        Assertions.assertTrue(menu.checkInput(0, "test", (UIFormTest) null));
    }

    @Test
    public void testCheckInput() {
        UIPair[] array = new UIPair[1];

        array[0] = new UIPairFactory("Test", stringTest);

        UIFormMenuFactory menu = new UIFormMenuFactory("Heading", array);

        Assertions.assertTrue(menu.checkInput(0, "test", (UIFormTest) menu.getT(0)));
    }

    @Test
    public void testRunAction() {
        UIPair[] array = new UIPair[1];

        array[0] = new UIPairFactory("", stringTest);

        UIFormMenuFactory menu = new UIFormMenuFactory("Heading", array);
    }

}
