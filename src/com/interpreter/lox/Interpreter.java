package com.interpreter.lox;

import com.interpreter.lox.Expr.Binary;
import com.interpreter.lox.Expr.Grouping;
import com.interpreter.lox.Expr.Literal;
import com.interpreter.lox.Expr.Unary;

public class Interpreter implements Expr.Visitor<Object> {

	@Override
	public Object visitBinaryExpr(Binary expr) {
		// TODO Auto-generated method stub
		Object left, right;
		left = evaluate(expr.left);
		right = evaluate(expr.right);
		
		switch(expr.operator.type) {
		case MINUS:
			return (double)left - (double)right;
		case SLASH:
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
		case GREATER:
			return (double)left > (double)right;
		case LESS:
			return (double)left < (double)right;
		case GREATER_EQUAL:
			return (double)left >= (double)right;
		case LESS_EQUAL:
			return (double)left <= (double)right;
		case EQUAL_EQUAL:
			return isEqual(left, right);
		case BANG_EQUAL:
			return !isEqual(left, right);
			
		}
		return null;
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

}
