package com.examen.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examen.springboot.app.models.dao.IStylesDao;
import com.examen.springboot.app.models.entity.Style;

@Service
public class StylesServiceImpl implements IStylesService{

	@Autowired
	private IStylesDao stylesDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Style> findListStyles() {		
		return (List<Style>) stylesDao.findAll();
	}

	@Override
	@Transactional
	public void saveStyles(Style eStyles) {
		stylesDao.save(eStyles);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Style findStylesById(Long id) {		
		return stylesDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteStylesById(Long id) {
		stylesDao.deleteById(id);
		
	}
	
	@Override
	public List<Style> findAllExcluyenteStyle(Long idArtist) {		
		return stylesDao.findAllExcluyenteStyle(idArtist);
	}

}
