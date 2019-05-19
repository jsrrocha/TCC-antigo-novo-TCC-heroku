package com.tcc.CadeMeuBichinho.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.tcc.CadeMeuBichinho.model.Pet;
import com.tcc.CadeMeuBichinho.model.User;
import com.tcc.CadeMeuBichinho.model.Pet.FurColor;
import com.tcc.CadeMeuBichinho.model.Pet.LifeStage;
import com.tcc.CadeMeuBichinho.model.Pet.Sex;
import com.tcc.CadeMeuBichinho.model.Pet.Specie;
import com.tcc.CadeMeuBichinho.controller.PetController;

@Repository
public class PetRepositoryImpl implements PetRepositoryCustom{
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	UserRepository userRepository;

	
	@Override
	public List<Pet> findBySearchTerms(Map<String, String> petMap) throws ParseException {
		
	    CriteriaBuilder cb = em.getCriteriaBuilder(); 
	    CriteriaQuery<Pet> cq = cb.createQuery(Pet.class); 
	 
	    Root<Pet> pet = cq.from(Pet.class); 
	    List<Predicate> predicates = new ArrayList<>();
	    
	    if(petMap.get("specie") != null) {
	    	Integer index = Integer.parseInt(petMap.get("specie"));	    	
	    	Specie type = Specie.values()[index];  
	    	predicates.add(cb.equal(pet.get("specie"),type));	
	    }
	    
	    if(petMap.get("sex") != null) {
	    	Integer index = Integer.parseInt(petMap.get("sex"));	    	
	        Sex sex = Sex.values()[index];  
	    	predicates.add(cb.equal(pet.get("sex"),sex));
	    }
	   
	    if(petMap.get("furColor") != null) {
	    	Integer index = Integer.parseInt(petMap.get("furColor"));	    	
			FurColor fur = FurColor.values()[index];  
    		predicates.add(cb.equal(pet.get("furColor"),fur));	
	    }
	    
	    if(petMap.get("lifeStage") != null) {
	    	Integer index = Integer.parseInt(petMap.get("lifeStage"));	    	
			LifeStage lifeStage = LifeStage.values()[index];  
    		predicates.add(cb.equal(pet.get("lifeStage"),lifeStage));
	    }
	    
	    if(petMap.get("date") != null) { 
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		Date date = simpleFormat.parse(petMap.get("date"));
		Date finalDate = PetController.dateWithoutTime(date);
             
    		predicates.add(cb.equal(pet.get("date"),finalDate));
	    }
	    
	    if(petMap.get("description") != null) {
    		predicates.add(cb.like(pet.get("description"),"%" + petMap.get("description") + "%"));
	    }
	  
	    if(petMap.get("latitude") != null && petMap.get("longitude") != null) {
			Double latitude = Double.parseDouble(petMap.get("latitude"));
			Double longitude = Double.parseDouble(petMap.get("longitude"));
			
			Double latitudeMoreOneKM = latitude + 0.009044 ;
			Double latitudeLessOneKM = latitude - 0.009044 ;
			Double longitudeMoreOneKM = longitude + 0.0089831 ;
			Double longitudeLessOneKM = longitude - 0.0089831 ;
			
		    predicates.add(cb.between(pet.get("latitude"), latitudeLessOneKM, latitudeMoreOneKM));
		    predicates.add(cb.between(pet.get("longitude"), longitudeLessOneKM, longitudeMoreOneKM));
	    }
	    
	    if(petMap.get("lostPet") != null) {
	    	Boolean lostPet  = Boolean.valueOf(petMap.get("lostPet"));
    		predicates.add(cb.equal(pet.get("lostPet"),lostPet)); 
	    }
	    
	    if(petMap.get("userId") !=null) {
	    	Long userId = Long.parseLong(petMap.get("userId"));	 
	        Optional<User> optionalUser = userRepository.findById(userId);
			if (!optionalUser.isPresent()) {
				User user = optionalUser.get();
	    		predicates.add(cb.equal(pet.get("user"),user));
			}
	    }
	    
	    predicates.add(cb.equal(pet.get("remove"),false)); 
	    
	    cq.where(predicates.toArray(new Predicate[0]));  
	    return em.createQuery(cq).getResultList();
	}
	
	
	//https://www.baeldung.com/spring-data-criteria-queries
	
	/*Latitude and longitude
	public String calcutePetsInOneKM(Double latValue,Double longValue) {
		String select = 
		"SELECT * FROM Pet "
		+ "WHERE  ( latitude BETWEEN ( " + latValue +" - 0.009044) "
				+ "AND  ( " + latValue + "+0.009044)) "
		+ "AND (longtitude BETWEEN (" + longValue + "- 0.0089831) "
				+ "AND ("+ longValue +"+0.0089831))";
		return select;
	}
	*/
}
