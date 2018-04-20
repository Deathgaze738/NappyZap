package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
	@Autowired
	AdminVerification verification;
	
	public boolean authorize(String adminKey){
		return verification.checkKey(adminKey);
	}
}
