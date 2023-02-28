package design.boilerplate.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import design.boilerplate.springboot.model.User;
import design.boilerplate.springboot.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@RestController
public class ApiController {

	@Autowired
	UserRepository userRepo;
	@GetMapping("/api/sayHello")
	public ResponseEntity<String> sayHello() {
		return ResponseEntity.ok("Hello Spring Boot Boilerplate");
	}

	// Create user
	@PostMapping(value ="/api/user/create",  consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> createCustomUser(@RequestBody User customUser) {


		// check email empty or not / usernamne empty or not
		if(customUser.getEmail() == null || customUser.getEmail().isEmpty() || customUser.getUsername().isEmpty()){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email / Username is empty");
		}

		// validation check on existing account exist
		if(userRepo.existsByEmail(customUser.getEmail()) || userRepo.existsByUsername(customUser.getUsername()) ){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with Username / Email already exist");
		}
		return ResponseEntity.ok().body(userRepo.save(customUser));
	}

	// Get the List of User
	@GetMapping("/api/user/all")
	public ResponseEntity<List<User>> getAllCustomUser() {
		return ResponseEntity.ok().body(userRepo.findAll());
	}

	// Get Specific user
	@GetMapping("/api/user/{username}/retrieve")
	public ResponseEntity<?> getCustomUser(@PathVariable(value="username") String username) {
		if(!userRepo.existsByUsername(username) ){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
		}
		return ResponseEntity.ok().body(userRepo.findByUsername(username));
	}


	// Put Specific user
	@PutMapping("/api/user/update")
	public ResponseEntity<?> updateCustomUser(@RequestBody User customUser) {

		if(!userRepo.existsByUsername(customUser.getUsername())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
		}

		// Find the user and update the user data
		// Also to throw a exception if any thing else
		userRepo.findById(customUser.getId()).map(user -> {
			user.setUserRole(customUser.getUserRole());
			user.setName(customUser.getName());
			user.setEmail(customUser.getEmail());
			user.setUsername(customUser.getUsername());
			user.setPassword(customUser.getPassword());
			return ResponseEntity.ok().body(userRepo.save(user));
		});

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating user");
	}


	// Delete Specific user
	@DeleteMapping("/api/user/{username}/delete")
	public ResponseEntity<String> deleteCustomUser(@PathVariable(value="username") String username) {
		// Check if user exist if it does not return error
		if(!userRepo.existsByUsername(username) ){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
		}

		User user = userRepo.findByUsername(username);

		try {
			userRepo.deleteById(user.getId());
			return ResponseEntity.ok().body("Successfully deleted account");
		}catch (Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting account");
		}
	}
}
