package poly;

import org.junit.Test;

import java.util.function.DoubleUnaryOperator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Grader2 {

    @Test
    public void testPolyFunction1() {
        DoubleUnaryOperator polyFunction = PolyFactory.parseToFunction("X^2 + X^2 + 3 * X + 7");
        assertEquals(21, polyFunction.applyAsDouble(2), 0.00001);
    }

    @Test
    public void testPolyFunction2() {
        DoubleUnaryOperator polyFunction = PolyFactory.parseToFunction("X^2 + X^2 + 3 * X - 7");
        assertEquals(7, polyFunction.applyAsDouble(2), 0.00001);
    }

    @Test
    public void testPolyFunction3() {
        DoubleUnaryOperator polyFunction = PolyFactory.parseToFunction("X^2 + 100 * X^2 - 50 + 3 * X - 7");
        assertEquals(353, polyFunction.applyAsDouble(2), 0.00001);
    }

    @Test
    public void testPolyFunction4() {
        DoubleUnaryOperator polyFunction = PolyFactory.parseToFunction("X^2 + 100 * X^2 - 50 + 3 * X^0 - 7");
        assertEquals(350, polyFunction.applyAsDouble(2), 0.00001);
    }

    @Test
    public void testPolyFunction5() {
        DoubleUnaryOperator polyFunction = PolyFactory.parseToFunction("0");
        assertEquals(0, polyFunction.applyAsDouble(2), 0.00001);
    }

    @Test
    public void testPolyFunction6() {
        DoubleUnaryOperator polyFunction = PolyFactory.parseToFunction("0 * X^1000");
        assertEquals(0, polyFunction.applyAsDouble(2), 0.00001);
    }

    @Test
    public void testPolyFunction7() {
        DoubleUnaryOperator polyFunction = PolyFactory.parseToFunction("0 * X^1000 + 0 * X^2");
        assertEquals(0, polyFunction.applyAsDouble(2), 0.00001);
    }

    @Test
    public void testPolyFunction8() {
        DoubleUnaryOperator polyFunction = PolyFactory.parseToFunction("X - X + 10000 * X^2 - 10000 * X^2");
        assertEquals(0, polyFunction.applyAsDouble(0), 0.00001);
    }
}
