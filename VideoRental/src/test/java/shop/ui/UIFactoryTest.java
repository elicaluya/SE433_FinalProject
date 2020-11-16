package shop.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class UIFactoryTest {

    private UIFormTest stringTest = new UIFormTest() {
        public boolean run(String input) {
            return ! "".equals(input.trim());
        }
    };

    private final PrintStream standardOut = System.out;
    private final InputStream standardIn = System.in;

    @Test
    public void testTextDisplayMessage() {
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));

        UIFactory ui = new UIFactory("text");

        ui.displayMessage("This is a message!");

        Assertions.assertEquals("This is a message!", capturedOut.toString().trim());

        System.setOut(standardOut);
    }

    @Test
    public void testTextDisplayError() {
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));

        UIFactory ui = new UIFactory("text");

        ui.displayMessage("This is an error!");

        Assertions.assertEquals("This is an error!", capturedOut.toString().trim());

        System.setOut(standardOut);
    }

    @Test
    public void testTextProcessMenuInputClosed() {
        ByteArrayInputStream capturedIn = new ByteArrayInputStream("".getBytes());
        System.setIn(capturedIn);

        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));

        UIFormMenuBuilderFactory builder = new UIFormMenuBuilderFactory();

        builder.add("Test", stringTest);

        UIFormMenu menu = builder.toUIFormMenu("Heading");

        UIFactory ui = new UIFactory("text");

        Assertions.assertThrows(UIError.class, () -> ui.processMenu(menu), "Expected UIError");

        System.setIn(standardIn);
        System.setOut(standardOut);
    }

    @Test
    public void testTextProcessForm() {
        ByteArrayInputStream capturedIn = new ByteArrayInputStream("1".getBytes());
        System.setIn(capturedIn);

        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));

        UIFormMenuBuilderFactory builder = new UIFormMenuBuilderFactory();

        builder.add("Test", stringTest);

        UIFormMenu menu = builder.toUIFormMenu("Heading");

        UIFactory ui = new UIFactory("text");

        ui.processForm(menu);

        Assertions.assertEquals("Test", capturedOut.toString().trim());

        System.setIn(standardIn);
        System.setOut(standardOut);
    }

}
