package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.SexRepository;
import exception.NotFoundException;
import model.Sex;

@Service
public class SexService {
	
	@Autowired
	SexRepository sexRepo;
	@Autowired
	EmployeeService empService;
	
	public List<Sex> getSexes(){
		List<Sex> sexes = sexRepo.findAll();
		if(sexes.isEmpty()){
			throw new NotFoundException("There are no sexes available.");
		}
		return sexes;
	}
	
	public Sex getSex(int id){
		Sex sex = sexRepo.findOne(id);
		if(sex == null){
			throw new NotFoundException("Sex with id '" + id + "' doesn't exist.");
		}
		return sex;
	}
	
	public Sex updateSex(String authorization, Sex newSex, int id){
		empService.authorize(authorization, 0L);
		Sex sex = sexRepo.findOne(id);
		if(sex == null){
			throw new NotFoundException("Sex with id '" + id + "' doesn't exist.");
		}
		sex.setName(newSex.getName());
		sexRepo.save(sex);
		return sex;
	}
	
	public Sex addSex(String authorization, Sex newSex){
		empService.authorize(authorization, 0L);
		sexRepo.save(newSex);
		return newSex;
	}
	
	public void deleteSex(String authorization, int sexId){
		empService.authorize(authorization, 0L);
		Sex sex = sexRepo.findOne(sexId);
		if(sex == null){
			throw new NotFoundException("Sex with id '" + sexId + "' doesn't exist.");
		}
		sexRepo.delete(sex);
	}
}
