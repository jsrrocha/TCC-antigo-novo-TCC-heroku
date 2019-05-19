package com.tcc.CadeMeuBichinho.repository;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.tcc.CadeMeuBichinho.model.Pet;

public interface PetRepositoryCustom {
	public List<Pet> findBySearchTerms(Map<String, String> criteria_list) throws ParseException;
}
