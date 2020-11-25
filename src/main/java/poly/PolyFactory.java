package poly;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class PolyFactory {

    /**
     * @param string must contain a well-formed poly string
     * @return a Poly corresponding to the string in canonical form
     */
    public static Poly parse(String string) {
        // Create a stream of tokens using the lexer.
        CharStream stream = new ANTLRInputStream(string);
        PolyLexer lexer = new PolyLexer(stream);
        lexer.reportErrorsAsExceptions();
        TokenStream tokens = new CommonTokenStream(lexer);

        // Feed the tokens into the parser.
        PolyParser parser = new PolyParser(tokens);
        parser.reportErrorsAsExceptions();

        // Generate the parse tree using the starter rule.
        ParseTree tree = parser.poly(); // "root" is the starter rule.


        // debugging option #1: print the tree to the console
        System.err.println(tree.toStringTree(parser));


        //debugging option #3: walk the tree with a listener
        new ParseTreeWalker().walk(new PolyListener_PrintEverything(), tree);


        ParseTreeWalker walker = new ParseTreeWalker();
        PolyListener_PolyCreator listener = new PolyListener_PolyCreator();
        walker.walk(listener, tree);

        return listener.getPoly();
    }

    /**
     * Return a function representing the polynomial described by expr
     * @param expr is the string representation of the polynomial to process
     * @return a function representing the polynomial described by expr
     */
    public static DoubleUnaryOperator parseToFunction(String expr) {
        Poly poly = parse(expr);
        return poly.getFunction();
    }

    private static class PolyListener_PolyCreator extends PolyBaseListener {
        List<Term> terms = new ArrayList<>();

        @Override
        public void exitTerm(PolyParser.TermContext ctx) {
            int coeff = 1;
            int pow = 1;
            if (ctx.MUL() != null) {        // have a multiply sign - first two rules
                if (ctx.POW() != null) {
                    assert ctx.NUM().size() == 2;
                    coeff = Integer.parseInt(ctx.NUM().get(0).getText());
                    pow = Integer.parseInt(ctx.NUM().get(1).getText());
                } else {
                    assert ctx.NUM().size() == 1;
                    coeff = Integer.parseInt(ctx.NUM().get(0).getText());
                    pow = 1;
                }

            } else {                         // no multiply sign -> last three rules
                if (ctx.POW() != null) {     // but I have a power sign -> third rule
                    assert ctx.NUM().size() == 1;
                    coeff = 1;
                    pow = Integer.parseInt(ctx.NUM().get(0).getText());
                }                            // either X or a constant
                else {
                    if (ctx.VAR() != null) {
                        coeff = 1;
                        pow = 1;
                    } else {
                        assert ctx.NUM().size() == 1;
                        pow = 0;
                        coeff = Integer.parseInt(ctx.NUM().get(0).getText());
                    }
                }
            }
            terms.add(new Term(coeff, pow));
        }

        @Override
        public void exitSumterms(PolyParser.SumtermsContext ctx) {
            if (ctx.SUB() != null) {
                Term lastTerm = terms.remove(terms.size() - 1);
                terms.add(new Term (-lastTerm.getCoeff(), lastTerm.getPow()));
            }
        }

        @Override
        public void exitPoly(PolyParser.PolyContext ctx) {
            // nothing to do
            ctx.getText();
        }

        /**
         * Return a canonical version of the polynomial
         * @return a canonical version of the polynomial
         */
        public Poly getPoly() {
            return new Poly(terms);
        }
    }

    private static class PolyListener_PrintEverything extends PolyBaseListener {
        public void enterPoly(PolyParser.PolyContext ctx) {
            System.err.println("entering poly: " + ctx.getText());
        }

        public void exitPoly(PolyParser.PolyContext ctx) {
            System.err.println("exiting poly: " + ctx.getText());
        }

        public void enterSumterms(PolyParser.SumtermsContext ctx) {
            System.err.println("entering sumterms: " + ctx.getText());
        }

        public void exitSumterms(PolyParser.SumtermsContext ctx) {
            System.err.println("exiting sumterms: " + ctx.getText());
            System.err.println("    ADD: " + ctx.ADD());
            System.err.println("    SUB: " + ctx.SUB());
        }

        public void enterTerm(PolyParser.TermContext ctx) {
            System.err.println("entering term: " + ctx.getText());
        }

        public void exitTerm(PolyParser.TermContext ctx) {
            System.err.println("exiting term: " + ctx.getText());
            System.err.println("    NUM(size): " + ctx.NUM().size());
            for (int i = 0; i < ctx.NUM().size(); i++) {
                System.err.println("    NUM[" + i + "] = " + ctx.NUM().get(i));
            }
            System.err.println("    MUL: " + ctx.MUL());
            System.err.println("    VAR: " + ctx.VAR());
            System.err.println("    POW: " + ctx.POW());
        }
    }
}