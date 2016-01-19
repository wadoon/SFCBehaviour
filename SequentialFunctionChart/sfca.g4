grammar sfca;

@header {
import java.util.*;

import SFC.*;
import SFC.Transition;
}

import declaration;

start_sfc locals [ SFCA ast = new SFCA(), Map<String,Variable> ids = new HashMap<String,Variable>() ]
:
	SFC name = IDENTIFIER
	{$ast.setName($name.text);
	}
		
	(
		var_declarations [$ast.getVariables(),$ids]
	)*
	(
		goto_declaration [$ast.getTrans(),$ids]
		| step_declaration [$ast.getSteps(),$ids]
	)* 
	END_SFC
	;
	
var_declarations [List<Variable> var, Map<String,Variable> ids]
:
	input_variable_int[var,ids]
	|output_variable_int[var,ids]
	|other_variable_int[var,ids]
	|input_variable_bool[var,ids]
	|output_variable_bool[var,ids]
	|other_variable_bool[var,ids]
;

input_variable_int[List<Variable> var,Map<String,Variable> ids]
:
VAR_INPUT name=IDENTIFIER ':' INT ';' END_VAR
{
	Variable x = new Variable($name.text,true,false,Vartype.INT);
	var.add(x);
	ids.put($name.text,x);
}
;

output_variable_int[List<Variable> var,Map<String,Variable> ids]
:
VAR_OUTPUT name=IDENTIFIER ':' INT ';' END_VAR
{
	Variable x = new Variable($name.text,false,true,Vartype.INT);
	var.add(x);
	ids.put($name.text,x);
}
;

other_variable_int[List<Variable> var,Map<String,Variable> ids]
:
VAR name=IDENTIFIER ':' INT ';' END_VAR
{
	Variable x = new Variable($name.text,false,false,Vartype.INT);
	var.add(x);
	ids.put($name.text,x);
}
;

input_variable_bool[List<Variable> var,Map<String,Variable> ids]
:
VAR_INPUT name=IDENTIFIER ':' BOOL ';' END_VAR
{
	Variable x = new Variable($name.text,true,false,Vartype.BOOL);
	var.add(x);
	ids.put($name.text,x);
}
;

output_variable_bool[List<Variable> var,Map<String,Variable> ids]
:
VAR_OUTPUT name=IDENTIFIER ':' BOOL ';' END_VAR
{
	Variable x = new Variable($name.text,false,true,Vartype.BOOL);
	var.add(x);
	ids.put($name.text,x);
}
;

other_variable_bool[List<Variable> var,Map<String,Variable> ids]
:
VAR name=IDENTIFIER ':' BOOL ';' END_VAR
{
	Variable x = new Variable($name.text,false,false,Vartype.BOOL);
	var.add(x);
	ids.put($name.text,x);
}
;


goto_declaration [List<Transition> transitions, Map<String,Variable> ids]
:
	GOTO guard=expression[ids] '::' from=IDENTIFIER '->' to=IDENTIFIER
	{
		Transition t = new Transition();
		t.setSource($from.text);
		t.setDestination($to.text);
		t.setGuard($guard.ctx.ast);
		
		transitions.add(t);
	}
;


step_declaration [List<Step> steps,Map<String,Variable> ids]
locals [Step s = new Step(),List<Assignment> aslist = new ArrayList<Assignment>()]
:
	STEP name = IDENTIFIER
	(	
		ON 'active' ACTION 
		(
			assignment[ids]
			{$aslist.add($assignment.ctx.ast);}
		)*
		END_ACTION
	) END_STEP
	{
		$s.setName($name.text);
		$s.setActions($aslist);		
		$steps.add($s);			
	}

;

assignment[Map<String,Variable> ids]
locals[Assignment ast]
:
name=IDENTIFIER ':=' expr = expression[ids]
{$ast = new Assignment(ids.get($name.text),$expression.ctx.ast);}
;

expression [Map<String,Variable> ids]
locals[Expression ast]
:
binexpr[ids]
{$ast = $binexpr.ctx.ast;}
;

binexpr[Map<String,Variable> ids]
locals[Expression ast]
:
left = unexpr[ids]
{$ast = $left.ctx.ast;}
(op =binary_operator right=expression[ids]
	{$ast = new BinOpExpr($left.ctx.ast,$op.ctx.ast,$right.ctx.ast);}
)*

;

unexpr[Map<String,Variable> ids]
locals[Expression ast]
:
primary_expression[ids]
{$ast = $primary_expression.ctx.ast;}
|unary_expression[ids]
{$ast =$unary_expression.ctx.ast;}
|'(' expression[ids] ')'
{$ast =$expression.ctx.ast;}
;

primary_expression[Map<String,Variable> ids]
locals [ Expression ast]
:
	literal
	{ $ast = $literal.ctx.expr; }
	| variable[ids]
	{ $ast = $variable.ctx.expr; }


;

literal locals [LiteralExpr expr]
:
	intlit
	{$expr = new LiteralExpr($intlit.ctx.intliteral,Vartype.INT);}
	|boollit
	{$expr = new LiteralExpr($boollit.ctx.boolliteral,Vartype.BOOL);}
;

intlit locals [String intliteral]
:
lit = INTEGER_LITERAL
{$intliteral = $lit.text;}
;

boollit locals [String boolliteral]
:
'TRUE'
{$boolliteral = "TRUE";}
|'FALSE'
{$boolliteral = "FALSE";}
;

variable[Map<String,Variable> ids]
locals [VariableExpr expr]
:
varidentifier[ids]
{$expr = new VariableExpr($varidentifier.ctx.ast);}
;

varidentifier[Map<String,Variable> ids]
locals[Variable ast]
:
name=IDENTIFIER
{$ast= ids.get($name.text);}
;

unary_expression[Map<String,Variable> ids]
locals[UnOpExpr ast]
:
MINUS expr=expression[ids]
{$ast = new UnOpExpr($expr.ctx.ast,UnOperator.MINUS);}
|NOT expr=expression[ids]
{$ast = new UnOpExpr($expr.ctx.ast,UnOperator.NEGATE);}
;

binary_operator
locals[BinOperator ast]
:
PLUS
{$ast = BinOperator.ADD;}
|MINUS
{$ast = BinOperator.SUB;}
|DIV
{$ast = BinOperator.DIV;}
|MOD
{$ast = BinOperator.MOD;}
|MULT
{$ast = BinOperator.MULT;}
|AND
{$ast = BinOperator.AND;}
|OR
{$ast = BinOperator.OR;}
|XOR
{$ast = BinOperator.XOR;}
|EQUALS
{$ast = BinOperator.EQUALS;}
|NOT_EQUALS
{$ast = BinOperator.NOT_EQUALS;}
|POWER
{$ast = BinOperator.POWER;}
|LESS_THAN
{$ast = BinOperator.LESS_THAN;}
|GREATER_THAN
{$ast = BinOperator.GREATER_THAN;}
|GREATER_EQUALS
{$ast = BinOperator.GREATER_EQUALS;}
|LESS_EQUALS
{$ast = BinOperator.LESS_EQUALS;}
;
ON : 	O N ;

STEP : 	S T E P ;

END_STEP : 	E N D '_' S T E P;

COLON : ':' ;

ACTION : 	A C T I O N ;

END_ACTION : E N D '_' A C T I O N ;

SFC : S F C;

END_SFC : E N D '_' S F C ;

GOTO : G O T O ;