package design.boilerplate.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import design.boilerplate.springboot.model.User;
import design.boilerplate.springboot.repository.UserRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;


@RestController
public class ApiController {
	@Autowired
	UserRepository userRepo;
	@GetMapping("/api/sayHello")
	public ResponseEntity<String> sayHello() {
		return ResponseEntity.ok("Hello Spring Boot Boilerplate");
	}

	@PostMapping("/api/user/create")
	public User createCustomUser(@RequestBody User customUser) {
		return userRepo.save(customUser);
	}

	// Get the List of User
	@GetMapping("/api/user/list")
	public List<User> getAllCustomUser() {
		return userRepo.findAll();
	}


}
