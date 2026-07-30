package com.example.projectservicetwo.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectservicetwo.entity.User;
import com.example.projectservicetwo.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository urepo;
	
	public List<User> getAll(){
		return urepo.findAll();
		
	}
	
}
