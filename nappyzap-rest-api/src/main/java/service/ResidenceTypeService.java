package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ResidenceTypeRepository;
import exception.NotFoundException;
import model.ResidenceType;

@Service
public class ResidenceTypeService {
	@Autowired
	private ResidenceTypeRepository residenceTypeRepo;
	@Autowired
	private EmployeeService employeeService;
	
	public ResidenceType getResidenceType(Long id){
		ResidenceType type = residenceTypeRepo.findOne(id);
		if(type == null){
			throw new NotFoundException("Residence Type with id '" + id + "' not found.");
		}
		return type;
	}
	
	public List<ResidenceType> getResidenceTypes(){
		List<ResidenceType> types = residenceTypeRepo.findAll();
		if(types.isEmpty()){
			throw new NotFoundException("There are no Residence Types available.");
		}
		return residenceTypeRepo.findAll();
	}
	
	public ResidenceType addResidenceType(String authorization, ResidenceType newType){
		employeeService.authorize(authorization, 0L);
		residenceTypeRepo.save(newType);
		return newType;
	}
	
	public ResidenceType updateResidenceType(String authorization, Long id, ResidenceType newType){
		employeeService.authorize(authorization, 0L);
		ResidenceType type = residenceTypeRepo.findOne(id);
		if(type == null){
			throw new NotFoundException("Residence Type with id '" + id + "' not found.");
		}
		type.setDescription(newType.getDescription());
		type.setName(newType.getName());
		residenceTypeRepo.save(type);
		return type;
	}
	
	public void deleteResidenceType(String authorization, Long id){
		employeeService.authorize(authorization, 0L);
		residenceTypeRepo.delete(id);
	}
}
