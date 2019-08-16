package com.examen.springboot.app.models.dao;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.examen.springboot.app.models.entity.Artist;
import com.examen.springboot.app.models.entity.People;
import com.examen.springboot.app.models.entity.REL_ARTIST_STYLE;
import com.examen.springboot.app.models.entity.Style;

public interface IArtistDao extends CrudRepository<Artist, Long>{

	@Query("select v from Artist v where id in (SELECT FK_ARTIST FROM REL_ARTIST_STYLE where FK_STYLE = ?1)")	
	public List<Artist> findAllExcluyente(Long idStyle);
	
}
