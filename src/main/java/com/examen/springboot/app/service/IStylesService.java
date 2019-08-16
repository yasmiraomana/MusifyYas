package com.examen.springboot.app.service;

import java.util.List;
import com.examen.springboot.app.models.entity.Style;

public interface IStylesService {

	public List<Style> findListStyles();
	
	public void saveStyles(Style eStyles);
	
	public Style findStylesById(Long id);
	
	public void deleteStylesById(Long id);
	
	public List<Style> findAllExcluyenteStyle(Long idArtist);
	
}
