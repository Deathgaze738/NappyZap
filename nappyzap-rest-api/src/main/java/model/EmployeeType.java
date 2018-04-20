package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "employeetype")
public class EmployeeType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "type_id")
	private Long typeId;
	
	@NotNull
	@Column(name = "role_name")
	private String roleName;

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
