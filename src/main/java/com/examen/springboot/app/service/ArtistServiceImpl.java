package com.examen.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examen.springboot.app.models.dao.IArtistDao;
import com.examen.springboot.app.models.entity.Artist;
import com.examen.springboot.app.models.entity.REL_ARTIST_STYLE;

@Service
public class ArtistServiceImpl implements IArtistService{

	@Autowired
	private IArtistDao artistDao;

	@Override
	@Transactional(readOnly = true)
	public List<Artist> findListArtist() {
		return (List<Artist>) artistDao.findAll();
	}

	@Override
	@Transactional
	public void saveArtist(Artist eArtist) {
		artistDao.save(eArtist);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Artist findArtistById(Long id) {
		return artistDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteArtistById(Long id) {
		artistDao.deleteById(id);
		
	}

	@Override
	@Transactional
	public List<Artist> findAllExcluyente(Long idStyle) {
		return artistDao.findAllExcluyente(idStyle);
	}
	
}
