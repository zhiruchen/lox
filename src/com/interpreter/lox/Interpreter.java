package com.interpreter.lox;

import com.interpreter.lox.Expr.Binary;
import com.interpreter.lox.Expr.Grouping;
import com.interpreter.lox.Expr.Literal;
import com.interpreter.lox.Expr.Unary;

public class Interpreter implements Expr.Visitor<Object> {
	
	void interpret(Expr expression) {
		try {
			Object value = evaluate(expression);
			System.out.println(stringify(value));
		} catch(RuntimeError error) {
			Lox.runtimeError(error);
		}
	}
	
	
	@Override
	public Object visitBinaryExpr(Binary expr) {
		// TODO Auto-generated method stub
		Object left, right;
		left = evaluate(expr.left);
		right = evaluate(expr.right);
		
		switch(expr.operator.type) {
		case MINUS:
			checkNumberOperands(expr.operator, left, right);
			return (double)left - (double)right;
		case SLASH:
			checkNumberOperands(expr.operator, left, right);
			return (double)left / (double)right;
		case STAR:
			return (double)left * (double)right;
		case PLUS:
			if ((left instanceof Double) && (right instanceof Double)) {
				return (double)left + (double)right;
			}
			
			if ((left instanceof String) && (right instanceof String)) {
				return (String)left + (String)right;
			}
			throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings");
		case GREATER:
			checkNumberOperands(expr.operator, left, right);
			return (double)left > (double)right;
		case LESS:
			checkNumberOperands(expr.operator, left, right);
			return (double)left < (double)right;
		case GREATER_EQUAL:
			checkNumberOperands(expr.operator, left, right);
			return (double)left >= (double)right;
		case LESS_EQUAL:
			checkNumberOperands(expr.operator, left, right);
			return (double)left <= (double)right;
		case EQUAL_EQUAL:
			return isEqual(left, right);
		case BANG_EQUAL:
			return !isEqual(left, right);
			
		}
		return null;
	}
	
	private void checkNumberOperand(Token operator, Object operand) {
		if (operand instanceof Double) return;
		throw new RuntimeError(operator, "Operand must be a number.");
	}
	
	private void checkNumberOperands(Token operator, Object op1, Object op2) {
		if (op1 instanceof Double && op2 instanceof Double) return;
		throw new RuntimeError(operator, "Operands must be numbers!");
	}

	private boolean isEqual(Object left, Object right) {
		// TODO Auto-generated method stub
		if (left == null && right == null) return true;
		if (left == null) return false;
		
		return left.equals(right);
	}

	@Override
	public Object visitGroupingExpr(Grouping expr) {
		// TODO Auto-generated method stub
		return evaluate(expr.expression);
	}

	@Override
	public Object visitLiteralExpr(Literal expr) {
		// TODO Auto-generated method stub
		return expr.value;
	}

	@Override
	public Object visitUnaryExpr(Unary expr) {
		// TODO Auto-generated method stub
		Object right = evaluate(expr.right);

		switch (expr.operator.type) {
		case BANG:
			return !isTruthy(right);
		case MINUS:
			checkNumberOperand(expr.operator, right);
			return -(double) right;
		}
		return null;
	}

	// false, nil 为false, 其他为true
	private boolean isTruthy(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof Boolean)
			return (boolean) obj;
		return true;
	}

	private Object evaluate(Expr expr) {
		return expr.accept(this);
	}
	
	private String stringify(Object obj) {
		if (obj == null) return "nil";
		
		if (obj instanceof Double) {
			String text = obj.toString();
			if (text.endsWith(".0")) {
				text = text.substring(0, text.length()-2);
			}
			return text;
		}
		return obj.toString();
	}
}
