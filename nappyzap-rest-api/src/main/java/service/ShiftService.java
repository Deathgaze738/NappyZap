package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ShiftRepository;
import exception.NotFoundException;
import model.Shift;

@Service
public class ShiftService {
	@Autowired
	ShiftRepository shiftRepo;
	@Autowired
	EmployeeService employeeService;
	
	public List<Shift> getShifts(){
		return shiftRepo.findAll();
	}
	
	public Shift getShift(Long id){
		return shiftRepo.findOne(id);
	}
	
	public Shift addShift(String authorization, Shift newShift){
		employeeService.authorize(authorization);
		shiftRepo.save(newShift);
		return newShift;
	}
	
	public void deleteShift(String authorization, Long id){
		employeeService.authorize(authorization);
		Shift shift = shiftRepo.findOne(id);
		if(shift == null){
			throw new NotFoundException("Shift with id '" + id + "' not found.");
		}
		shiftRepo.delete(shift);
	}
}
