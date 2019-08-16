package com.examen.springboot.app.service;

import java.util.List;

import com.examen.springboot.app.models.entity.People;
import com.examen.springboot.app.models.entity.Style;

public interface IPeopleService {

	public List<People> findListPeople();
	
	public void savePeople(People ePeople);
	
	public People findPeopleById(Long id);
	
	public void deletePeopleById(Long id);
	
	public List<People> findAllExcluyente(Long idArtist);
	
}
