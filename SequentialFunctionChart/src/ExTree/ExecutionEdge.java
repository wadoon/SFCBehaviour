package ExTree;

import SFC.Expression;

public class ExecutionEdge {
	private String sourcePath;
	private String destPath;
	private Expression guard;
	public ExecutionEdge(String sourcePath, String destPath, Expression guard) {
		super();
		this.sourcePath = sourcePath;
		this.destPath = destPath;
		this.guard = guard;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public String getDestPath() {
		return destPath;
	}
	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}
	public Expression getGuard() {
		return guard;
	}
	public void setGuard(Expression guard) {
		this.guard = guard;
	}
	
	

}
