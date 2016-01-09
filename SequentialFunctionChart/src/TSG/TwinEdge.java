package TSG;

import SFC.Expression;

public class TwinEdge {
	private String sourceId;
	private String destId;
	private Expression guardleft;
	private Expression guardright;
	
	public TwinEdge(String sourceId, String destId, Expression guardleft, Expression guardright) {
		super();
		this.sourceId = sourceId;
		this.destId = destId;
		this.guardleft = guardleft;
		this.guardright = guardright;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getDestId() {
		return destId;
	}
	public void setDestId(String destId) {
		this.destId = destId;
	}
	public Expression getGuardleft() {
		return guardleft;
	}
	public void setGuardleft(Expression guardleft) {
		this.guardleft = guardleft;
	}
	public Expression getGuardright() {
		return guardright;
	}
	public void setGuardright(Expression guardright) {
		this.guardright = guardright;
	}


}
