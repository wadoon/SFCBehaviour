package SFC;

public enum UnOperator {
	MINUS("-"), NEGATE("NOT");

    public final String symbol;

    UnOperator(String symbol) {
        this.symbol = symbol;
    }
}
