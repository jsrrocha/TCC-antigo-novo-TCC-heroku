package com.tcc.CadeMeuBichinho.controller;


import java.util.Date;
import java.util.Map;
import java.util.Optional;

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
import com.tcc.CadeMeuBichinho.Repository.PetRepository;
import com.tcc.CadeMeuBichinho.model.Pet;
import com.tcc.CadeMeuBichinho.model.Pet.RemovalReason;
import com.tcc.CadeMeuBichinho.model.Pet.FurColor;
import com.tcc.CadeMeuBichinho.model.Pet.LifeStages;
import com.tcc.CadeMeuBichinho.model.Pet.Sex;
import com.tcc.CadeMeuBichinho.model.Pet.Size;
import com.tcc.CadeMeuBichinho.model.Pet.Type;

@RestController
@RequestMapping("pet")
public class PetController { 

	@Autowired
	PetRepository petRepository;

	@PostMapping("")
	public ResponseEntity<?> addPet(@RequestBody Pet pet){
		try {
			//quando adicionar um pet confere se o 
			//usuário está logado para pegar o telefone
			//quando usuário logado vai tq add na lista de pets do user!
			// NO FRONT END
			
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
			if(petMap.get("type") != null) {
				Integer index = Integer.parseInt(petMap.get("type"));
				Type type = Type.values()[index];  
				editPet.setType(type); 
			}

			if(petMap.get("sex") != null) {
				Integer index = Integer.parseInt(petMap.get("sex"));
				Sex sex = Sex.values()[index];  
				editPet.setSex(sex);
			}

			if(petMap.get("size") != null) {
				Integer index = Integer.parseInt(petMap.get("size"));
				Size size = Size.values()[index];  
				editPet.setSize(size);
			}

			if(petMap.get("furColor") != null) {
				Integer index = Integer.parseInt(petMap.get("furColor"));
				FurColor fur = FurColor.values()[index];  
				editPet.setFurColor(fur);
			}

			if(petMap.get("lifeStages") != null) {
				Integer index = Integer.parseInt(petMap.get("lifeStages"));
				LifeStages lifeStage = LifeStages.values()[index];  
				editPet.setLifeStage(lifeStage);
			}


			//Testar quando o front tiver ok..
			if(petMap.get("photo") != null) {
				byte[] backToBytes = Base64.decodeBase64(petMap.get("photo"));
				editPet.setPhoto(backToBytes);
			}

			if(petMap.get("date") != null) {
				Date date = new Date(Long.parseLong(petMap.get("date")));
				editPet.setDate(date);
			}

			if(petMap.get("description") != null) {
				editPet.setDescription(petMap.get("description"));
			}

			if(petMap.get("latitude") != null) {
				Double latitude = Double.parseDouble(petMap.get("latitude"));
				editPet.setLatitude(latitude);
			}

			if(petMap.get("longitude") != null) {
				Double longitude = Double.parseDouble(petMap.get("longitude"));
				editPet.setLongitude(longitude);
			}

			if(petMap.get("phone") != null) {
				Integer phone = Integer.parseInt(petMap.get("phone"));
				editPet.setPhone(phone);
			}	

			if(petMap.get("phoneWithWhats") != null) {
				Boolean phoneWithWhats  = Boolean.valueOf(petMap.get("phoneWithWhats"));
				editPet.setPhoneWithWhats(phoneWithWhats);
			}

			if(petMap.get("lostPet") != null) {
				Boolean lostPet  = Boolean.valueOf(petMap.get("lostPet"));
				editPet.setLostPet(lostPet);
			}		

			if(petMap.get("remove") != null) {
				Boolean remove  = Boolean.valueOf(petMap.get("remove"));
				editPet.setRemove(remove);
			}
			
			if(editPet.getRemove() && petMap.get("removalReason") == null) {
				return new ResponseEntity<String>("Preencha campo motivo da remoção", HttpStatus.BAD_REQUEST); 
			}
			
			if(petMap.get("removalReason") != null) {
				Integer index = Integer.parseInt(petMap.get("removalReason"));
				RemovalReason removalReason = RemovalReason.values()[index];  
				editPet.setRemovalReason(removalReason);
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
	@PostMapping("/search")
	public ResponseEntity<?> petSearch(@RequestBody Map<String, String> pet){
		try {
			
			Iterable<Pet> list = petRepository.findBySearchTerms(pet);

			return new ResponseEntity<>(list, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

	@Transactional 
	@GetMapping("/all")
	public ResponseEntity<?> getAll(){
		try {
			Iterable<Pet> list = petRepository.findAll();
			return new ResponseEntity<>(list, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

	@Transactional 
	@GetMapping("/lost")
	public ResponseEntity<?> getLostPet(){
		try {
			Iterable<Pet> list = petRepository.findByLostPet(true);
			return new ResponseEntity<>(list, HttpStatus.OK); 
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}

	@Transactional 
	@GetMapping("/find")
	public ResponseEntity<?> getFindPet(){
		try {
			Iterable<Pet> list = petRepository.findByLostPet(false);
			return new ResponseEntity<Iterable<Pet>>(list, HttpStatus.OK); 
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
}
