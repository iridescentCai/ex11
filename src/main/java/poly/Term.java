package poly;

import java.lang.Math;

// immutable polynomial term
final public class Term {
    private final int coeff;
    private final int pow;

    public Term (int coeff, int pow) {
        this.coeff = coeff;
        this.pow = pow;
    }

    public int getPow () {
        return pow;
    }

    public int getCoeff () {
        return coeff;
    }

    public double eval (double x) {
        return coeff * Math.pow(x, pow);
    }

    @Override
    public String toString() {
        return "T[" + coeff + ", " + pow + "]";
    }
}
