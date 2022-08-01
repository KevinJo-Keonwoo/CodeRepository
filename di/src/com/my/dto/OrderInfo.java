package com.my.dto;

import java.util.Date;
import java.util.List;

public class OrderInfo { 	//orderinfo와 orderline이 1대다 관계이기에 여러개 있음    //1대
	private int orderNo; //주문번호    
	private String orderId; //주문자아이디	
	private Date orderDt; //주문일자	
	private List<OrderLine> lines;   										   //다
	
	public OrderInfo() {
		super();
	}
	
	public OrderInfo(String orderId, List<OrderLine> lines) {
		super();
		this.orderId = orderId;
		this.lines = lines;
	}

	public OrderInfo(int orderNo, String orderId, Date orderDt, List<OrderLine> lines) {
		super();
		this.orderNo = orderNo;
		this.orderId = orderId;
		this.orderDt = orderDt;
		this.lines = lines;
	}	
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Date getOrderDt() {
		return orderDt;
	}
	public void setOrderDt(Date orderDt) {
		this.orderDt = orderDt;
	}
	public List<OrderLine> getLines() {
		return lines;
	}
	public void setLines(List<OrderLine> lines) {
		this.lines = lines;
	}

	@Override
	public String toString() {
		return "OrderInfo [orderNo=" + orderNo + ", orderId=" + orderId + ", orderDt=" + orderDt + ", lines=" + lines
				+ "]";
	}
	
}
