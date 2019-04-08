package com.tcc.CadeMeuBichinho.controller;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.CadeMeuBichinho.Repository.PetRepository;
import com.tcc.CadeMeuBichinho.Repository.UserRepository;
import com.tcc.CadeMeuBichinho.model.Pet;
import com.tcc.CadeMeuBichinho.model.User;

@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	PetRepository petRepository;
	
	@Transactional 
	@PostMapping("")
	public ResponseEntity<?> adduser(@RequestBody Map<String, String> userMap){
		try {
			if(userMap.get("email") == null) {
				return new ResponseEntity<>("Preencha o email", HttpStatus.BAD_REQUEST); 
			}
			
			if(userMap.get("password") == null) {
				return new ResponseEntity<>("Preencha a senha", HttpStatus.BAD_REQUEST); 
			}
			
			User userExist = userRepository.findByEmail(userMap.get("email"));
			if(userExist != null) {
				return new ResponseEntity<>("Usuário com este email já existe", HttpStatus.BAD_REQUEST); 
			}
			
			Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
			Matcher matcher = pattern.matcher(userMap.get("email")); 
			if(!matcher.matches()) {
				return new ResponseEntity<>("Preencha um email válido", HttpStatus.BAD_REQUEST); 
			}
			
			User user = new User();
			user.setName(userMap.get("name"));
			user.setEmail(userMap.get("email"));
			user.setPassword(userMap.get("password"));
			user.setPhone(Integer.parseInt(userMap.get("phone")));
			user.setPhoneWithWhats(Boolean.valueOf(userMap.get("phoneWithWhats")));


			user = userRepository.save(user);
			if(userMap.get("idPet") != null) {
				ResponseEntity<?> response = 
						addUserOfPet(user.getId(),Long.parseLong(userMap.get("idPet")));	
				return response;
			}

			return new ResponseEntity<User>(user, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST); 
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editUser(@RequestBody User user){
		try {
			if(user.getId() == null) {
				return new ResponseEntity<>("Preencha o id do usuário", HttpStatus.BAD_REQUEST); 
			}

			Optional<User> optionalUser = userRepository.findById(user.getId());
			if(!optionalUser.isPresent()) {
				return new ResponseEntity<String>("User não existe", HttpStatus.BAD_REQUEST); 
			}

			User editUser = optionalUser.get();
			if(user.getName() !=null) {
				editUser.setName(user.getName());	
			}
			if(user.getEmail() !=null) {
				User userExist = userRepository.findByEmail(user.getEmail());
				if(userExist != null) {
					return new ResponseEntity<>("Usuário com este email já existe", HttpStatus.BAD_REQUEST); 
				}
				
				Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
				Matcher matcher = pattern.matcher(user.getEmail());
				if(!matcher.matches()) {
					return new ResponseEntity<>("Preencha um email válido", HttpStatus.BAD_REQUEST); 
				}	
				editUser.setEmail(user.getEmail());
			}

			if(user.getPassword() != null) {
				editUser.setName(user.getPassword());
			}

			if(user.getPhone() != null) {
				editUser.setPhone(user.getPhone());
			}

			if(user.getPhoneWithWhats() != null) {
				editUser.setPhoneWithWhats(user.getPhoneWithWhats());
			}
			
			userRepository.save(editUser);
			return new ResponseEntity<User>(user, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST); 
		}
	}

	@PostMapping("/{idUser}/add/pet/{idPet}")
	public ResponseEntity<?> addUserOfPet(@PathVariable Long idUser,@PathVariable Long idPet){
		try {
			Optional<Pet> getPet = petRepository.findById(idPet);
			if(!getPet.isPresent()) {
				return new ResponseEntity<String>("Pet não existe", HttpStatus.BAD_REQUEST); 
			}			
			Pet pet = getPet.get(); 
			if(pet.getUser() !=null) {
				return new ResponseEntity<String>("Pet já possui usuário", HttpStatus.BAD_REQUEST); 
			} 
			
			Optional<User> optionalUser = userRepository.findById(idUser);
			if(!optionalUser.isPresent()) {
				return new ResponseEntity<String>("User não existe", HttpStatus.BAD_REQUEST); 
			}
			User editUser = optionalUser.get();	
			pet.setUser(editUser);
			petRepository.save(pet);
			

			return new ResponseEntity<User>(editUser, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST); 
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User user){
		try {
			
			if(user.getEmail() == null) {
				return new ResponseEntity<>("Preencha o email", HttpStatus.BAD_REQUEST); 
			}
			
			if(user.getPassword() == null) {
				return new ResponseEntity<>("Preencha a senha", HttpStatus.BAD_REQUEST); 
			}

			Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
			Matcher matcher = pattern.matcher(user.getEmail());
			if(!matcher.matches()) {
				return new ResponseEntity<>("Preencha um email válido", HttpStatus.BAD_REQUEST); 
			}
			
			User userToLogin = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
			if(userToLogin == null || !userToLogin.getActive()) {
				return new ResponseEntity<>("Usuário não encontrado", HttpStatus.BAD_REQUEST); 
			} 
		    
			return new ResponseEntity<User>(userToLogin, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST); 
		}
	}


	@GetMapping("/{id}")
	public ResponseEntity<?> get(@PathVariable Long id){
		try {

			Optional<User> optionalUser = userRepository.findById(id);
			if(!optionalUser.isPresent()) {
				return new ResponseEntity<String>("User não existe", HttpStatus.BAD_REQUEST); 
			}
			
			return new ResponseEntity<Optional<User>>(optionalUser, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST); 
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAll(){
		try {
			Iterable<User> list = userRepository.findAll();
			return new ResponseEntity<>(list, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST); 
		}
	}

	@PostMapping("/desactive/{id}")
	public ResponseEntity<?> desactiveUser(@PathVariable Long id){
		try {
			Optional<User> user = userRepository.findById(id);
			if(!user.isPresent()) {
				return new ResponseEntity<String>("Usuário não existe", HttpStatus.BAD_REQUEST); 	
			}
			User desactiveUser = user.get();
			desactiveUser.setActive(false);
			userRepository.save(desactiveUser);

			return new ResponseEntity<String>("Usuário removido com sucesso", HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST); 
		}
	}
}
