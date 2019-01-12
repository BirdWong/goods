package cn.jijiking51.goods.order.domain;

import cn.jijiking51.goods.user.domain.User;

import java.util.List;

public class Order {
	//主键
	private String oid;
	//下单时间
	private String ordertime;
	//总计
	private double total;
	//订单状态 1.未付款	2.付款未发货	3.收到未确认	4.完成订单 		5.取消订单
	private int status;
	//收货地址
	private String address;
	//订单所有者
	private User user;
	
	private List<OrderItem> orderItemList;
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
