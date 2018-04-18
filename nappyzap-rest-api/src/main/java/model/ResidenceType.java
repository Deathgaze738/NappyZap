package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "residencetype")
public class ResidenceType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long residenceTypeId;

	@NotNull
	@Size(max = 45, message = "Residence Type name must be 45 characters maximum.")
	private String name;
	
	@NotNull
	@Size(max = 45, message = "Residence Type description must be 45 characters maximum.")
	private String description;
	
	public Long getResidenceTypeId() {
		return residenceTypeId;
	}

	public void setResidenceTypeId(Long residenceTypeId) {
		this.residenceTypeId = residenceTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
