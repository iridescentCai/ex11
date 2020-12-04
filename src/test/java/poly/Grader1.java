package poly;

import org.junit.Test;

import java.util.function.DoubleUnaryOperator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Grader1 {

    @Test
    public void testPolyString() {
        Poly p = PolyFactory.parse("X^10 - X ^ 2 + 100 * X + 10 * X + 1");
        assertEquals("1 * X ^ 0 + 110 * X ^ 1 + -1 * X ^ 2 + 1 * X ^ 10", p.toString());
    }

    @Test
    public void testPolyString1() {
        Poly p = PolyFactory.parse("X^3 + X^2 + X + 100");
        assertEquals("100 * X ^ 0 + 1 * X ^ 1 + 1 * X ^ 2 + 1 * X ^ 3", p.toString());
    }

    @Test
    public void testPolyString2() {
        Poly p = PolyFactory.parse("X^5 -7 * X^2 + 1");
        assertEquals("1 * X ^ 0 + -7 * X ^ 2 + 1 * X ^ 5", p.toString());
    }

    @Test
    public void testPolyString4() {
        Poly p = PolyFactory.parse("0 * X^1000 + 5 * X");
        String potentialOutput1 = "5 * X";
        String potentialOutput2 = "5 * X ^ 1";
        boolean correct = potentialOutput1.equals(p.toString()) ||
                potentialOutput2.equals(p.toString());

        assertTrue(correct);

    }

    @Test
    public void testPolyString5() {
        Poly p = PolyFactory.parse("X - X + 10000 * X^2 - 10000 * X^2");
        String potentialOutput1 = "0";
        String potentialOutput2 = "0 * X ^ 0";
        String potentialOutput3 = "";

        boolean correct = potentialOutput1.equals(p.toString()) ||
                potentialOutput2.equals(p.toString()) ||
                potentialOutput3.equals(p.toString());

        assertTrue(correct);
    }

    @Test
    public void testPolyString6() {
        Poly p = PolyFactory.parse("5 * X^2");
        assertEquals("5 * X ^ 2", p.toString());
    }

    @Test
    public void testPolyString7() {
        Poly p = PolyFactory.parse("100 * X^2 - 100 * X^3");
        assertEquals("100 * X ^ 2 + -100 * X ^ 3", p.toString());
    }
}
