package com.tcc.CadeMeuBichinho.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import com.tcc.CadeMeuBichinho.model.Pet;
import com.tcc.CadeMeuBichinho.model.Pet.FurColor;
import com.tcc.CadeMeuBichinho.model.Pet.LifeStage;
import com.tcc.CadeMeuBichinho.model.Pet.Sex;
import com.tcc.CadeMeuBichinho.model.Pet.Specie;

@Repository
public class PetRepositoryImpl implements PetRepositoryCustom{
	@PersistenceContext
	EntityManager em;
	
	@Override
	public List<Pet> findBySearchTerms(Map<String, String> petMap) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
	    CriteriaQuery<Pet> cq = cb.createQuery(Pet.class); 
	 
	    Root<Pet> pet = cq.from(Pet.class); 
	    List<Predicate> predicates = new ArrayList<>();
	    
	    if(petMap.get("type") != null) {
	    	Integer index = Integer.parseInt(petMap.get("type"));	    	
	    	Specie type = Specie.values()[index];  
	    	predicates.add(cb.equal(pet.get("type"),type));	
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
	    
	    if(petMap.get("lifeStages") != null) {
	    	Integer index = Integer.parseInt(petMap.get("lifeStages"));	    	
			LifeStage lifeStage = LifeStage.values()[index];  
    		predicates.add(cb.equal(pet.get("lifeStages"),lifeStage));
	    }
	    	    
	    predicates.add(cb.equal(pet.get("remove"),false)); 
	    
	    if(petMap.get("date") != null) {
		    Date date = new Date(Long.parseLong(petMap.get("date")));
    		predicates.add(cb.equal(pet.get("date"),date));
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
	    
	    /* Não faz sentido!
	    if(petMap.get("phone") != null) {
	    	Integer phone = Integer.parseInt(petMap.get("phone"));
    		predicates.add(cb.equal(pet.get("phone"),phone)); 
	    }
	    
	    if(petMap.get("phoneWithWhats") != null) {
	    	Boolean phoneWithWhats  = Boolean.valueOf(petMap.get("phoneWithWhats"));
    		predicates.add(cb.equal(pet.get("phoneWithWhats"),phoneWithWhats)); 
	    }
	    */
	    
	    if(petMap.get("lostPet") != null) {
	    	Boolean lostPet  = Boolean.valueOf(petMap.get("lostPet"));
    		predicates.add(cb.equal(pet.get("lostPet"),lostPet)); 
	    }
	    
	    cq.where(predicates.toArray(new Predicate[0]));  
	    return em.createQuery(cq).getResultList();
	}
	
	
	//https://www.baeldung.com/spring-data-criteria-queries
	//https://bitbucket.org/mwolfart/sistema-patas-dadas
	
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
