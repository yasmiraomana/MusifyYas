package com.examen.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.examen.springboot.app.models.entity.Style;

public interface IStylesDao extends CrudRepository<Style, Long>{

	@Query("select v from Style v where id not in (SELECT FK_STYLE FROM REL_ARTIST_STYLE where FK_ARTIST = ?1)")	
	public List<Style> findAllExcluyenteStyle(Long idArtist);
	
}
