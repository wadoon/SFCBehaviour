package SFC;

public enum BinOperator {
    //arithmetic
    ADD("+"),
    MULT("*"),
    SUB("-"),
    DIV("/"),
    MOD("MOD"),

    //logical
    AND("AND"),
    OR("OR"),
    XOR("XOR"),

    //comparison
    EQUALS("="),
    NOT_EQUALS("<>"),

    POWER("**"),

    LESS_THAN("<"),
    GREATER_THAN(">"),
    GREATER_EQUALS(">="),
    LESS_EQUALS("<=");

    public final String symbol;

    BinOperator(String t) {
        symbol = t;
    }
    
    
}
