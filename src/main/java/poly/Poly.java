package poly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class Poly {

    private final List<Term> p;

    public Poly(List<Term> p) {
        this.p = new ArrayList<>(p);
    }

    public double eval(double x) {
        double value = 0;
        for (Term t : p) {
            value += t.eval(x);
        }
        return value;
    }

    public DoubleUnaryOperator getFunction() {
        return new PolynomialFunction(this);
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        for (Term t : p) {
            if (s.length() > 0) {
                s.append(" + ");
            }
            s.append(t.getCoeff() + " * X ^ " + t.getPow());
        }
        return s.toString();
    }

    private class PolynomialFunction implements DoubleUnaryOperator {
        private Poly p;

        public PolynomialFunction(Poly p) {
            this.p = p;
        }

        @Override
        public double applyAsDouble(double operand) {
            return p.eval(operand);
        }
    }
}
