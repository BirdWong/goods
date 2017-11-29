package cn.itcast.goods.cart.domain;

import java.math.BigDecimal;

import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.user.domain.User;

public class CartItem {
	private String cartItemId;//主键
	private int quantity;//对应数量
	private Book book;//书
	private User user;//用户
	
	/*
	 * 添加小计
	 */
	public double getSubtotal(){
		BigDecimal bigDecimal1 = new BigDecimal(book.getCurrPrice()+"");
		BigDecimal bigDecimal2 = new BigDecimal(quantity+"");
		BigDecimal bigDecimal3 = bigDecimal1.multiply(bigDecimal2);	
		return bigDecimal3.doubleValue();
	}
	public String getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
