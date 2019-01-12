package cn.jijiking51.goods.pager;


/**
 * @author  h4795
 */
public class Expression {
	/**
	 * 条件名称
	 */
	private String name ;
	/**
	 * 条件符号
	 */
	private String operator;
	/**
	 * 条件值
	 */
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Expression [name=" + name + ", operator=" + operator
				+ ", value=" + value + "]";
	}
	public Expression(String name, String operator, String value) {
		super();
		this.name = name;
		this.operator = operator;
		this.value = value;
	}
	public Expression() {
		super();
	}
	
}
