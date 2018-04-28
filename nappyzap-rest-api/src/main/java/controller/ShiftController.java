package controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import model.Shift;
import service.ShiftService;

@RestController
@RequestMapping("/shifts")
public class ShiftController {
	@Autowired
	ShiftService service;
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Shift getShift(@PathVariable("id") Long id){
		return service.getShift(id);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Shift> getShifts(){
		return service.getShifts();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Shift addShift(@RequestBody @Valid Shift shift,
			@RequestHeader("Authorization") String authorization){
		return service.addShift(authorization, shift);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteShift(@PathVariable("id") Long id,
			@RequestHeader("Authorization") String authorization){
		service.deleteShift(authorization, id);
	}
}
