package SFC;

public class Variable {
	private String name;
	private boolean in;
	private boolean out;
	private Vartype type;
	
	public Variable(String name, boolean in, boolean out, Vartype type) {
		super();
		this.name = name;
		this.in = in;
		this.out = out;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIn() {
		return in;
	}

	public void setIn(boolean in) {
		this.in = in;
	}

	public boolean isOut() {
		return out;
	}

	public void setOut(boolean out) {
		this.out = out;
	}

	public Vartype getType() {
		return type;
	}

	public void setType(Vartype type) {
		this.type = type;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable)) return false;

        Variable that = (Variable) o;

        if (!(this.name.equals(that.getName()))) return false;
        if (!(this.type.equals(that.getType()))) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 37 * result + type.hashCode();
        return result;
    }
	
	
}
