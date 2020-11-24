package poly;

import org.junit.Test;
import java.util.function.DoubleUnaryOperator;
import static org.junit.Assert.assertEquals;

public class PolyFactoryTest {

    @Test
    public void testPolyString() {
        Poly p = PolyFactory.parse("X^10 - X ^ 2 + 100 * X + 10 * X + 1");
        assertEquals("1 * X ^ 0 + 110 * X ^ 1 + -1 * X ^ 2 + 1 * X ^ 10", p.toString());
    }

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

}
