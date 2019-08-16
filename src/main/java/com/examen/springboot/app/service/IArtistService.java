package com.examen.springboot.app.service;

import java.util.List;

import com.examen.springboot.app.models.entity.Artist;
import com.examen.springboot.app.models.entity.REL_ARTIST_STYLE;

public interface IArtistService {

	public List<Artist> findListArtist();
	
	public void saveArtist(Artist eArtist);
	
	public Artist findArtistById(Long id);
	
	public void deleteArtistById(Long id);
	
	public List<Artist> findAllExcluyente(Long idStyle);
	
}
