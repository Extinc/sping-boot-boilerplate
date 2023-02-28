package design.boilerplate.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<?> createCustomUser(@RequestBody User customUser) {
		String email = customUser.getEmail();
		String username = customUser.getUsername();

		// check email empty or not / usernamne empty or not
		if( email.isEmpty() || username.isEmpty()){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email / Username is empty");
		}

		// validation check on existing account exist
		if(userRepo.existsByEmail(email) || userRepo.existsByUsername(username) ){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with Username / Email already exist");
		}
		return ResponseEntity.ok().body(userRepo.save(customUser));
	}

	// Get the List of User
	@GetMapping("/api/user/list")
	public ResponseEntity<List<User>> getAllCustomUser() {
		return ResponseEntity.ok().body(userRepo.findAll());
	}

	// Get Specific user
	@GetMapping("/api/user/{username}")
	public ResponseEntity<?> getCustomUser(@PathVariable(value="username") String username) {

		if(!userRepo.existsByUsername(username) ){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
		}
		return ResponseEntity.ok().body(userRepo.findByUsername(username));
	}


	// Put Specific user
	@PutMapping("/api/user/{username}")
	public ResponseEntity<?> updateCustomUser(@RequestBody User customUser) {

		if(!userRepo.existsByUsername(customUser.getUsername())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
		}

		userRepo.findById(customUser.getId()).map(user -> {
			user.setUserRole(customUser.getUserRole());
			user.setName(customUser.getName());
			user.setEmail(customUser.getEmail());
			user.setUsername(customUser.getUsername());
			user.setPassword(customUser.getPassword());
			return ResponseEntity.ok().body(userRepo.save(user));
		}).orElseThrow(RuntimeException::new);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
	}


	// Put Specific user
	@DeleteMapping("/api/user/{username}")
	public ResponseEntity<String> updateCustomUser(@PathVariable(value="username") String username) {
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
