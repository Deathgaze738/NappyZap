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

import model.Sex;
import service.SexService;

@RestController
@RequestMapping("/sexes")
public class SexController {
	@Autowired
	SexService service;
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Sex getSex(@PathVariable(value = "id") int sexId){
		return service.getSex(sexId);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Sex> getSexes(){
		return service.getSexes();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public Sex updateSex(@PathVariable(value = "id") int sexId,
			@Valid @RequestBody Sex sex,
			@RequestHeader("Authorization") String authorization){
		return service.updateSex(authorization, sex, sexId);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Sex addSex(@Valid @RequestBody Sex sex,
			@RequestHeader("Authorization") String authorization){
		return service.addSex(authorization, sex);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteSex(@RequestHeader("Authorization") String authorization,
			@PathVariable("id") int id){
		service.deleteSex(authorization, id);
	}
}
