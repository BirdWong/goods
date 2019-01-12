package cn.jijiking51.goods.order.domain;


import cn.jijiking51.goods.book.domain.Book;

public class OrderItem {
	//主键
	private String orderItemId;
	//数量
	private int quantity;
	//总计
	private double subtotal;
	/**
	 * 所关联的书
	 */
	private Book book;
	/**
	 * 所属订单
	 */
	private Order order;
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	
	
}
