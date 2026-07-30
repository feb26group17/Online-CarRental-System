package com.example.projectservicetwo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectservicetwo.entity.User;
import com.example.projectservicetwo.service.UserService;

@RestController
public class UserController {
	@Autowired
	UserService userv;

	@GetMapping("/all")
	public List<User> getAll() {
		return userv.getAll();
	}
}
