package org.hxn.shopxx.util;

public class Condition {

	private String property;

	private Operator operator;

	private Object obj;

	public Condition(String property) {
		super();
		this.property = property;
	}

	public Condition(Operator operator, Object obj) {
		super();
		this.operator = operator;
		this.obj = obj;
	}

	public Condition(String property, Operator operator, Object obj) {
		super();
		this.property = property;
		this.operator = operator;
		this.obj = obj;
	}

	public String getProperty() {
		return property;
	}

	public Operator getOperator() {
		return operator;
	}

	public Object getObj() {
		return obj;
	}

}
