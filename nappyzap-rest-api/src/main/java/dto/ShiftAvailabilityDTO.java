package dto;

import javax.validation.Valid;

import model.Shift;

public class ShiftAvailabilityDTO {
	@Valid
	private Shift shift;
	private boolean available;
	
	public Shift getShift() {
		return shift;
	}
	public void setShift(Shift shift) {
		this.shift = shift;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
}
