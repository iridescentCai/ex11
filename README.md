**CPEN 221 / Fall 2020 / Exercise 11**

# Parsing Polynomials

## Goals

This exercise offers a brief introduction to parsing and parser generators with ANTLR. You will also work with functional interfaces to return functions.

*This task does not require you to write a lot of code but does require some understanding of parsing and functional interfaces. You will also notice that may pieces of the skeleton code have been provided.*

## Reading Material

The [Java tutorial on lambdas](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html) is a good starting point. You will also find the Java documentation for the [`java.util.function`](http://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html)package useful.

## Grammars and Parsing using ANTLR

We want to parse and evaluate simple polynomials. Let us start with expressions which only contain polynomials in 'X' (uppercase or lowercase). For example:

```
2*X + 1
X^3 + X^2 + X + 100
x^5 - 7 * x^2 + 1
```

How do we parse these expressions? Remember that we divide the procedure into three steps:

- characters → **Lexer** → tokens
- tokens → **Parser** → concrete syntax tree
- concrete syntax tree → **AST creation** → abstract syntax tree

What will be the inputs and outputs of each of these blocks while parsing input string `"X^3 + X^2 + X + 100"`?

### Step 1. Design the Grammar for our Polynomial Expressions

Definitions in the grammar take the following form:

```
<name> : <definition>;
```

Take a look at the `Poly.g4` file containing a grammar that has been provided and can be used by ANTLR to generate a parser.

**Key points to notice**:

- How to tell parser generator to skip white spaces: `WHITESPACE : [ \\t\\r\\n]+ -> skip ;`
- Difference between lexical rules and parser rules: Lexical rules define the tokens while parser rules define the semantics.
- CAPITALIZED vs. lowercase names: CAPITALIZED names are used for lexical rules, lowercase names are used for parser rules, which refer to lexical terminals and other parser nonterminals.
- The root rule ends with EOF because we process the entire input sequence at once.
- The `.g4` file contains some boilerplate definitions (`@header` and `@members`) which you should not need to modify.

### Step 2. Generate Lexer and Parser using ANTLR

Add the following lines to your `build.gradle` file.

ANTLR is a tool for generating a parser. To use ANTLR we can either download the library and include it in the Java classpath manually or we can declare it as a dependency in our `build.gradle` file. We shall use the latter approach.

1. Under `plugins`, add `id 'antlr'`
2. Under `dependencies`, add 

    ```java
    antlr "org.antlr:antlr4:4.7.2"
    compile "org.antlr:antlr4-runtime:4.7.2"
    ```

3. You will have to re-import your Gradle configuration if you did not have *auto-import* enabled in IntelliJ IDEA. You can also open the Gradle menu in IntelliJ IDEA and refresh Gradle settings by clicking on the refresh icon.
4. You should see a Gradle task called **generateGrammarSource** in the IDE menu that you can invoke. Alternatively, from the terminal you can run `gradle generateGrammarSource`.
5. ANTLR will generate source code for parsing and normally you should find these `.java` files in the directory `build/generated-src/antlr`. You should not have to edit these files. ANTLR will overwrite these files every time you recompile your program.
6. What's the output from ANTLR for our Poly grammar?
    - `PolyLexer.java`
    - `PolyParser.java`
    - `PolyListener.java` and `PolyBaseListener.java`
    - `Poly.tokens` and `PolyLexer.tokens`

### Step 3. Use the ANTLR-generated Lexer and Parser to Construct a Concrete Syntax Tree

Let's look at `PolyFactory.java`, which already contains an implementation for `parse` method that takes a `String` as input and returns a `Poly`. Here are the processing steps (also highlighted as comments in the code):

- Create a character stream from the input;
- Push the stream through the lexer;
- Create a token stream;
- Push that token stream through the parser;
- Ask the parser for the root grammar production (the one containing EOF).

Note: you could also ask the parser to parse smaller fragments by calling the appropriate method, e.g. in this case `term()` or `sumterms()`.

At this point we have a concrete parse tree, and we need to create an abstract syntax tree (AST).

### Step 4: Debugging the Parse Tree

One may first need to understand/debug the parse tree. To make sure it has the structure we expect, we can either:

1. Print it to the console;
2. Inspect it with the GUI;
3. Walk down the tree with a listener.

ANTLR provides a way to do depth-first traversal of the parse tree in order to, for example, print out debugging info, or generate an AST.

- `PolyListener` is the interface we must implement in order to do the traversal;
- `PolyBaseListener` is a convenience class which provides empty implementations of all the *callback methods;*
- We can create a subclass of `PolyBaseListener` and override only the methods we care about with our own implementation.

Look at `PolyListener_PrintEverything`:

- Running the code, we see what we would expect from a depth-first traversal;
- Using the context object passed to the callback, we can learn about the contents of the tree.

*Callback methods* are called callbacks because we write some code, but do not call our method directly — instead, we pass our listener object to a `ParseTreeWalker`, and it "calls us back" when something we care about happens, e.g. it reaches a particular kind of node in the parse tree.

### Step 5. Build an AST

Since our grammar is fairly simple, rather than building an AST, the code (implemented in `PolyListener_PolyCreator` class) directly builds a representation for a polynomial.

- The callback `exitTerm`, called after parsing a Concrete Syntax Tree term, identifies a polynomial term and adds it internal list.
- The callback `exitSumterms` deals with the plus/minus operations between terms;
- The callback `exitPoly` does nothing;
- The `getPoly` method processes the internal data structure (a degenerated AST) that accumulates state during parsing, builds a `Poly` and returns it. The `Poly` datatype returned has `toString` and `eval` methods.

## TODO

1. Reduce the polynomial to a canonical form. For example, the parser should return the polynomial `3 * X + 1` for an input of `"X + 1 + 2X"`.
2. Rather than returning a `Poly` return a function that represents the Poly. For example, for a String input `"X^3 + 1"`, you will return a function like `(double x) -> x * x * x + 1`. 

To do this, you should implement the method `PolyFactory.parseToFunction(String expr)` method. (Read about the `DoubleUnaryOperator` functional interface.)

Tip: None of the coefficients in a polynomial should be 0 unless the polynomial itself represents the constant `0`.

## Other Things You Could Think About

**You do not have to implement any of these for the exercise.**

- Extend the grammar and the parser to handle a two variable polynomial. The variables can have any names.
- There is a bug in the grammar: It does not accept polynomial that starts with a first term with a negative coefficient. That is, it accepts `1 - X` but it does not accept `-X + 1`. Fix it!
- Can you update the grammar so that coefficients can be floating point numbers?