package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dao.SexRepository;
import model.Customer;
import model.Sex;

@RestController
@RequestMapping("/sexes")
public class SexController {
	@Autowired
	SexRepository sexRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Sex getCustomer(@PathVariable(value = "id") int sexId){
		return sexRepository.findOne(sexId);
	}
}
