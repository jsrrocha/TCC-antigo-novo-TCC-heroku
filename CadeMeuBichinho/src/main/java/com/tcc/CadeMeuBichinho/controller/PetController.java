package com.tcc.CadeMeuBichinho.controller;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.text.SimpleDateFormat;

import org.apache.tomcat.util.codec.binary.Base64;
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

import com.tcc.CadeMeuBichinho.model.Pet;
import com.tcc.CadeMeuBichinho.model.User;
import com.tcc.CadeMeuBichinho.model.Pet.FurColor;
import com.tcc.CadeMeuBichinho.model.Pet.LifeStage;
import com.tcc.CadeMeuBichinho.model.Pet.RemovalReason;
import com.tcc.CadeMeuBichinho.model.Pet.Sex;
import com.tcc.CadeMeuBichinho.model.Pet.Specie;
import com.tcc.CadeMeuBichinho.repository.PetRepository;
import com.tcc.CadeMeuBichinho.repository.UserRepository;  

@RestController
@RequestMapping("pet")
public class PetController { 

	@Autowired
	PetRepository petRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Transactional
	@PostMapping("/add")
	public ResponseEntity<?> addPet(@RequestBody Map<String, String> petMap){
		try {
			
			if(petMap.get("name") == null) {
				return new ResponseEntity<String>("Preencha o nome do pet", HttpStatus.BAD_REQUEST); 
			}
			if(petMap.get("specie") == null) {
				return new ResponseEntity<String>("Preencha a espécie do pet", HttpStatus.BAD_REQUEST); 
			}

			if(petMap.get("sex") == null) {
				return new ResponseEntity<String>("Preencha o sexo do pet", HttpStatus.BAD_REQUEST); 
			}

			if(petMap.get("furColor") == null) {
				return new ResponseEntity<String>("Preencha a cor do pelo do pet", HttpStatus.BAD_REQUEST); 
			}

			if(petMap.get("lifeStage") == null) {
				return new ResponseEntity<String>("Preencha o estágio de vida do pet", HttpStatus.BAD_REQUEST); 
			}

			if(petMap.get("photo") == null) {
				return new ResponseEntity<String>("Insira uma foto do pet", HttpStatus.BAD_REQUEST); 

			}

			if(petMap.get("date") == null) {
				return new ResponseEntity<String>("Preencha a data do pet", HttpStatus.BAD_REQUEST); 
			}

			if(petMap.get("description") == null) {
				return new ResponseEntity<String>("Preencha uma descrição do pet", HttpStatus.BAD_REQUEST); 
			}

			if(petMap.get("latitude") == null) {
				return new ResponseEntity<String>("Preencha a latitude do pet", HttpStatus.BAD_REQUEST); 
			}

			if(petMap.get("longitude") == null) {
				return new ResponseEntity<String>("Preencha a longitude do pet", HttpStatus.BAD_REQUEST); 
			}

			if(petMap.get("phone") == null) {
				return new ResponseEntity<String>("Preencha o seu telefone", HttpStatus.BAD_REQUEST); 
			}	

			if(petMap.get("phoneWithWhats") == null) {
				return new ResponseEntity<String>("Prencha se o seu telefone tem whats", HttpStatus.BAD_REQUEST); 
			}

			if(petMap.get("lostPet") == null) {
				return new ResponseEntity<String>("Prencha se o pet está perdido ou foi encontrado", HttpStatus.BAD_REQUEST); 
			}	
			
			Pet pet = new Pet();
			pet.setName(petMap.get("name"));
			
			Integer indexSpecie = Integer.parseInt(petMap.get("specie"));
			Specie specie = Specie.values()[indexSpecie];  
			pet.setSpecie(specie); 
			
			Integer indexSex = Integer.parseInt(petMap.get("sex"));
			Sex sex = Sex.values()[indexSex];  
			pet.setSex(sex);
			
			Integer indexFurColor = Integer.parseInt(petMap.get("furColor"));
			FurColor fur = FurColor.values()[indexFurColor];  
			pet.setFurColor(fur);
			
			Integer index = Integer.parseInt(petMap.get("lifeStage"));
			LifeStage lifeStage = LifeStage.values()[index];  
			pet.setLifeStage(lifeStage);
			
			byte[] backToBytes = Base64.decodeBase64(petMap.get("photo"));
			pet.setPhoto(backToBytes);
			
			SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date datewithTime = simpleFormat.parse(petMap.get("date"));

			Date finalDate = dateWithoutTime(datewithTime);
			pet.setDate(finalDate);
			pet.setDescription(petMap.get("description"));
			
			Double latitude = Double.parseDouble(petMap.get("latitude"));
			pet.setLatitude(latitude);
			
			Double longitude = Double.parseDouble(petMap.get("longitude"));
			pet.setLongitude(longitude);
			
			Long phone = Long.parseLong(petMap.get("phone"));
			pet.setPhone(phone);
			
			Boolean phoneWithWhats  = Boolean.valueOf(petMap.get("phoneWithWhats"));
			pet.setPhoneWithWhats(phoneWithWhats);
			
			Boolean lostPet  = Boolean.valueOf(petMap.get("lostPet"));
			pet.setLostPet(lostPet);
			
			if(petMap.get("userId") !=null) {
				Optional<User> optionalUser = 
						userRepository.findById(Long.parseLong(petMap.get("userId")));
				if (!optionalUser.isPresent()) {
					return new ResponseEntity<String>("Usuário não existe", HttpStatus.BAD_REQUEST);
				}
				User user = optionalUser.get();
				pet.setUser(user);
			}	

			pet.setRemove(false);
			pet.setRemovalReason(null);
			petRepository.save(pet);
			
			return new ResponseEntity<Pet>(pet, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

	@PostMapping("/edit")
	public ResponseEntity<?> editPet(@RequestBody Map<String, String> petMap){
		try {
			
			if(petMap.get("id") ==null) {
				return new ResponseEntity<String>("Preencha o id do pet", HttpStatus.BAD_REQUEST); 
			}

			Optional<Pet> optionalPet = petRepository.findById(Long.parseLong(petMap.get("id")));
			if(!optionalPet.isPresent()) {
				return new ResponseEntity<String>("Pet não existe", HttpStatus.BAD_REQUEST); 
			}

			Pet editPet = optionalPet.get();
			if(petMap.get("name") != null) {
				editPet.setName(petMap.get("name"));
			}
			
			/*
			if(petMap.get("specie") != null) {
				Integer index = Integer.parseInt(petMap.get("type"));
				Specie type = Specie.values()[index];  
				editPet.setSpecie(type); 
			}

			if(petMap.get("sex") != null) {
				Integer index = Integer.parseInt(petMap.get("sex"));
				Sex sex = Sex.values()[index];  
				editPet.setSex(sex);
			}

			if(petMap.get("furColor") != null) {
				Integer index = Integer.parseInt(petMap.get("furColor"));
				FurColor fur = FurColor.values()[index];  
				editPet.setFurColor(fur);
			}

			if(petMap.get("lifeStages") != null) {
				Integer index = Integer.parseInt(petMap.get("lifeStages"));
				LifeStage lifeStage = LifeStage.values()[index];  
				editPet.setLifeStage(lifeStage);
			}
			
			if(petMap.get("latitude") != null) {
				Double latitude = Double.parseDouble(petMap.get("latitude"));
				editPet.setLatitude(latitude);
			}

			if(petMap.get("longitude") != null) {
				Double longitude = Double.parseDouble(petMap.get("longitude"));
				editPet.setLongitude(longitude);
			}
			
			if(petMap.get("date") != null) {
				Date date = new Date(Long.parseLong(petMap.get("date")));
				editPet.setDate(date);
			}
			
			if(petMap.get("lostPet") != null) {
				Boolean lostPet  = Boolean.valueOf(petMap.get("lostPet"));
				editPet.setLostPet(lostPet);
			}	
			
			*/

			if(petMap.get("photo") != null) {
				byte[] backToBytes = Base64.decodeBase64(petMap.get("photo"));
				editPet.setPhoto(backToBytes);
			}

			if(petMap.get("phone") != null) {
				Long phone = Long.parseLong(petMap.get("phone"));
				editPet.setPhone(phone);
			}	

			if(petMap.get("phoneWithWhats") != null) {
				Boolean phoneWithWhats  = Boolean.valueOf(petMap.get("phoneWithWhats"));
				editPet.setPhoneWithWhats(phoneWithWhats);
			}
			
			if(petMap.get("description") != null) {
				editPet.setDescription(petMap.get("description"));
			}

			petRepository.save(editPet);
			return new ResponseEntity<Pet>(editPet, HttpStatus.OK); 	

		} catch (ArrayIndexOutOfBoundsException array) {
			array.printStackTrace();
			return new ResponseEntity<>("Não existe este index no enum", HttpStatus.BAD_REQUEST); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST); 
		}

	}
	
	@PostMapping("/{petId}/add/user/{userId}")
	public ResponseEntity<?> addUserOfPet(@PathVariable Long petId, @PathVariable Long userId) {
		try {
			Optional<Pet> getPet = petRepository.findById(petId);
			if (!getPet.isPresent()) {
				return new ResponseEntity<String>("Pet não existe", HttpStatus.BAD_REQUEST);
			}
			Pet pet = getPet.get();
			if (pet.getUser() != null) {
				return new ResponseEntity<String>("Pet já possui usuário", HttpStatus.BAD_REQUEST);
			}

			Optional<User> optionalUser = userRepository.findById(userId);
			if (!optionalUser.isPresent()) {
				return new ResponseEntity<String>("User não existe", HttpStatus.BAD_REQUEST);
			}
			User user = optionalUser.get();
			pet.setUser(user);
			petRepository.save(pet);

			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getPet(@PathVariable Long id){
		try {
			Optional<Pet> getPet = petRepository.findById(id);
			return new ResponseEntity<Optional<Pet>>(getPet, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}
	
	@Transactional 
	@GetMapping("/all")
	public ResponseEntity<?> getAll(){
		try {
			List<Pet> pets = petRepository.findAll(); 
			
			List<Map<String, Object>> petsMap = new ArrayList<Map<String,Object>>();
			petsMap = buildPetsMap(pets);
			
			return new ResponseEntity<>(petsMap, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}
	
	@Transactional 
	@PostMapping("/search")
	public ResponseEntity<?> petSearch(@RequestBody Map<String, String> pet){
		try {
			
			List<Pet> pets = petRepository.findBySearchTerms(pet);
			
			List<Map<String, Object>> petsMap = new ArrayList<Map<String,Object>>();
			petsMap = buildPetsMap(pets);
			return new ResponseEntity<>(petsMap, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}
	
	@Transactional 
	@PostMapping("/search/local")
	public ResponseEntity<?> petSearchByLocal(@RequestBody Map<String, String> petMap){
		try {
			Long latitude = Long.parseLong(petMap.get("latitude"));
			Long longitude = Long.parseLong(petMap.get("longitude"));
			List<Pet> pets = petRepository.findByLatitudeAndLongitude(latitude, longitude);

			List<Map<String, Object>> petsMap = new ArrayList<Map<String,Object>>();
			petsMap = buildPetsMap(pets);
			
			return new ResponseEntity<>(petsMap, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}
	
	@Transactional 
	@GetMapping("/lost")
	public ResponseEntity<?> getLostPet(){
		try {
			List<Pet> pets = petRepository.findByLostPet(true);
			return new ResponseEntity<>(pets, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

	@Transactional 
	@GetMapping("/find")
	public ResponseEntity<?> getFindPet(){
		try {
			List<Pet> pets =  petRepository.findByLostPet(false);
			return new ResponseEntity<Iterable<Pet>>(pets, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

	@PostMapping("/remove/{id}/reason/{reason}")
	public ResponseEntity<?> removePet(@PathVariable Long id,@PathVariable Integer reason){
		try {
			Optional<Pet> pet = petRepository.findById(id);

			if(!pet.isPresent()) {
				return new ResponseEntity<String>("Pet não existe", HttpStatus.BAD_REQUEST); 
			}

			Pet removePet = pet.get();
			removePet.setRemove(true);
			RemovalReason removalReason = RemovalReason.values()[reason]; 
			removePet.setRemovalReason(removalReason);

			petRepository.save(removePet);

			return new ResponseEntity<String>("Pet removido com sucesso", HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}
	
	//Build return of gets pets
	public List<Map<String, Object>> buildPetsMap(List<Pet> pets){
		List<Map<String, Object>> petsMap = new ArrayList<Map<String,Object>>();
		
		for(Pet pet: pets) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id",pet.getId().toString());
			map.put("name",pet.getName());
			map.put("specie", pet.getSpecie().toString());
			map.put("sex", pet.getSex().toString());
			map.put("furColor", pet.getFurColor().toString());
			map.put("lifeStage", pet.getLifeStage().toString());
			map.put("date", pet.getDate().toString());
			map.put("phone", pet.getPhone().toString());
			map.put("phoneWithWhats",pet.getPhoneWithWhats().toString());
			String photoStr = Base64.encodeBase64String(pet.getPhoto()); 
			map.put("photo", photoStr);
			map.put("description", pet.getDescription());
			map.put("latitude", pet.getLatitude());
			map.put("longitude", pet.getLongitude());
			map.put("lostPet", pet.getLostPet().toString());
			
			if (pet.getUser() != null) {
				map.put("userId", pet.getUser().getId().toString());
				map.put("userName", pet.getUser().getName().toString()); //precisa?
			}

			petsMap.add(map);
		}
		return petsMap;
	}
	// Set time to zero
	public static Date dateWithoutTime(Date date) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.set(Calendar.MILLISECOND, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        return calendar.getTime();
	}
}
