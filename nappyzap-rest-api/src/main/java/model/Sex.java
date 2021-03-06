package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="sex")
public class Sex {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sex_id;
	@NotNull
	private String name;
	
	public int getId(){
		return sex_id;
	}
	public void setId(int sex_id){
		this.sex_id = sex_id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
