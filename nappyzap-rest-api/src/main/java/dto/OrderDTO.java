package dto;

import java.sql.Date;

import model.Visit;
import model.Shift;

public class OrderDTO {
	private Visit order;
	private Shift shift;
	private Date date;
	
	public Visit getOrder() {
		return order;
	}
	public void setOrder(Visit order) {
		this.order = order;
	}
	public Shift getShift() {
		return shift;
	}
	public void setShift(Shift shift) {
		this.shift = shift;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
