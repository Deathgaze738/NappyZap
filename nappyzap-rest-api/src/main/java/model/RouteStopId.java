package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Embeddable
public class RouteStopId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 490382379414329321L;

	@ManyToOne
	@JoinColumn(name = "route_id")
	@NotNull
	private Route route;
	
	@NotNull
	@Column(name = "stop_num")
	private int stopNum;

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public int getStopNum() {
		return stopNum;
	}

	public void setStopNum(int stopNum) {
		this.stopNum = stopNum;
	}
	
	@Override
	public String toString(){
		return route.getId() + ":" + stopNum;
	}
}
