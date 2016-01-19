package SFC;

public class Transition {
	private String source;
	private Expression guard;
	private String destination;
	
	public Transition() {
		super();
	}
	
	public Transition(String source, Expression guard, String destination) {
		super();
		this.source = source;
		this.guard = guard;
		this.destination = destination;
	}
	
	public String getSource() {
		return source;
	}



	public void setSource(String source) {
		this.source = source;
	}



	public Expression getGuard() {
		return guard;
	}
	public void setGuard(Expression guard) {
		this.guard = guard;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	
}
