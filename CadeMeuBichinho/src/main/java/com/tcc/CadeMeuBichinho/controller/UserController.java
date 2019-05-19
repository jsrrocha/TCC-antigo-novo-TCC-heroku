package com.tcc.CadeMeuBichinho.controller;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.CadeMeuBichinho.model.User;
import com.tcc.CadeMeuBichinho.repository.UserRepository;

@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	PetController petController;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping("")
	public Principal getCurrentLoggedInUser(Principal user) {
		return user;
	}

	@Transactional
	@PostMapping("/add")
	public ResponseEntity<?> adduser(@RequestBody Map<String, String> userMap) {
		try {
			if (userMap.get("email") == null) {
				return new ResponseEntity<>("Preencha o email", HttpStatus.BAD_REQUEST);
			}

			if (userMap.get("password") == null) {
				return new ResponseEntity<>("Preencha a senha", HttpStatus.BAD_REQUEST);
			}

			User userExist = userRepository.findByEmail(userMap.get("email"));
			if (userExist != null) {
				return new ResponseEntity<>("Usuário com este email já existe", HttpStatus.BAD_REQUEST);
			}

			Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
			Matcher matcher = pattern.matcher(userMap.get("email"));
			if (!matcher.matches()) {
				return new ResponseEntity<>("Preencha um email válido", HttpStatus.BAD_REQUEST);
			}

			User user = new User();
			user.setName(userMap.get("name"));
			user.setEmail(userMap.get("email"));

			String pass = passwordEncoder.encode(userMap.get("password"));
			user.setPassword(pass);
			user.setPhone(Integer.parseInt(userMap.get("phone")));
			user.setPhoneWithWhats(Boolean.valueOf(userMap.get("phoneWithWhats")));
			user.setActive(true);

			user = userRepository.save(user);
		    Map<String, Object> newUserMap = buildUserMap(user); 

			return new ResponseEntity<>(newUserMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/edit")
	public ResponseEntity<?> editUser(@RequestBody User user) {
		try {
			if (user.getId() == null) {
				return new ResponseEntity<>("Preencha o id do usuário", HttpStatus.BAD_REQUEST);
			}

			Optional<User> optionalUser = userRepository.findById(user.getId());
			if (!optionalUser.isPresent()) {
				return new ResponseEntity<String>("Usuário não existe", HttpStatus.BAD_REQUEST);
			}

			User editUser = optionalUser.get();
			if (user.getName() != null) {
				editUser.setName(user.getName());
			}

			if (user.getEmail() != null) {
				User userExist = userRepository.findByEmail(user.getEmail());
				if (userExist != null) {
					return new ResponseEntity<>("Usuário com este email já existe", HttpStatus.BAD_REQUEST);
				}

				Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
				Matcher matcher = pattern.matcher(user.getEmail());
				if (!matcher.matches()) {
					return new ResponseEntity<>("Preencha um email válido", HttpStatus.BAD_REQUEST);
				}
				editUser.setEmail(user.getEmail());
			}

			if (user.getPhone() != null) {
				editUser.setPhone(user.getPhone());
			}

			if (user.getPhoneWithWhats() != null) {
				editUser.setPhoneWithWhats(user.getPhoneWithWhats());
			}

			editUser = userRepository.save(editUser);
		    Map<String, Object> userMap = buildUserMap(editUser); 

			return new ResponseEntity<>(userMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST);
		}
	}

	

	@Transactional
	@PostMapping("/add/password/new")
	public ResponseEntity<?> addNewPassword(@RequestBody Map<String, String> userMap) {
		try {
			if (userMap.get("email") == null) {
				return new ResponseEntity<>("Preencha o email", HttpStatus.BAD_REQUEST);
			}

			if (userMap.get("phone") == null) {
				return new ResponseEntity<>("Preencha o telefone", HttpStatus.BAD_REQUEST);
			}

			if (userMap.get("newPassword") == null) {
				return new ResponseEntity<>("Preencha a nova senha", HttpStatus.BAD_REQUEST);
			}

			if (userMap.get("confirmNewPassword") == null) {
				return new ResponseEntity<>("Preencha a confirmação da nova senha", HttpStatus.BAD_REQUEST);
			}

			Integer phone = Integer.parseInt(userMap.get("phone"));
			User userExist = userRepository.findByEmailAndPhone(userMap.get("email"), phone);
			if (userExist == null) {
				return new ResponseEntity<>("Usuário com este email e telefone não existe", HttpStatus.BAD_REQUEST);
			}

			if (!userMap.get("newPassword").equals(userMap.get("confirmNewPassword"))) {
				return new ResponseEntity<>("Senhas não conferem", HttpStatus.BAD_REQUEST);
			}

			String pass = passwordEncoder.encode(userMap.get("newPassword"));
			userExist.setPassword(pass);

			userRepository.save(userExist);

			Map<String, String> msg = new HashMap<String, String>();
			msg.put("msg", "Nova senha salva com sucesso");

			return new ResponseEntity<>(msg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/loggedIn")
	public ResponseEntity<?> getLogged(){
		try {
		    Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		
		    if(loggedInUser.getName().equals("anonymousUser")) {
		    	return new ResponseEntity<>(null, HttpStatus.OK);
		    }
		    User user = userRepository.findByEmail(loggedInUser.getName());
		    Map<String, Object> userMap = buildUserMap(user);

			return new ResponseEntity<>(userMap, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST); 
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> get(@PathVariable Long id) {
		try {

			Optional<User> optionalUser = userRepository.findById(id);
			if (!optionalUser.isPresent()) {
				return new ResponseEntity<String>("User não existe", HttpStatus.BAD_REQUEST);
			}
			
			User user = optionalUser.get();
		    Map<String, Object> userMap = buildUserMap(user); 

			return new ResponseEntity<>(userMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAll() {
		try {
			Iterable<User> list = userRepository.findAll();
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/desactive/{id}")
	public ResponseEntity<?> desactiveUser(@PathVariable Long id) {
		try {
			Optional<User> user = userRepository.findById(id);
			if (!user.isPresent()) {
				return new ResponseEntity<String>("Usuário não existe", HttpStatus.BAD_REQUEST);
			}
			User desactiveUser = user.get();
			desactiveUser.setActive(false);
			userRepository.save(desactiveUser);

			Map<String, String> msg = new HashMap<String, String>();
			msg.put("msg", "Usuário removido com sucesso");

			return new ResponseEntity<>(msg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST);
		}
	}

	// Build return of gets user
	public Map<String, Object> buildUserMap(User user) {
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("id", user.getId().toString());
		userMap.put("name", user.getName());
		userMap.put("phone", user.getPhone().toString());
		userMap.put("phoneWithWhats", user.getPhoneWithWhats().toString());
		return userMap;
	}
	/*
	if (userMap.get("idPet") != null) {
		ResponseEntity<?> response = petController.addUserOfPet(user.getId(), Long.parseLong(userMap.get("idPet")));
		if (response.getStatusCode() != HttpStatus.OK) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return response;
		}
	} */
}
