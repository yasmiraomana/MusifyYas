package com.examen.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examen.springboot.app.models.dao.IPeopleDao;
import com.examen.springboot.app.models.entity.People;

@Service
public class PeopleServiceImpl implements IPeopleService{

	@Autowired
	IPeopleDao peopleDao;

	@Override
	@Transactional(readOnly = true)
	public List<People> findListPeople() {		
		return (List<People>) peopleDao.findAll();
	}
	
	@Override
	@Transactional
	public void savePeople(People ePeople) {
		peopleDao.save(ePeople);
		
	}

	@Override
	@Transactional(readOnly = true)
	public People findPeopleById(Long id) {		
		return peopleDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deletePeopleById(Long id) {
		peopleDao.deleteById(id);
		
	}

	@Override
	@Transactional
	public List<People> findAllExcluyente(Long idArtist) {
		return peopleDao.findAllExcluyente(idArtist);
	}
	
	
}
