package com.examen.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.examen.springboot.app.models.entity.People;

public interface IPeopleDao  extends CrudRepository<People, Long> {

	@Query("SELECT v FROM People v where artist_id is null or artist_id !=?1")
	public List<People> findAllExcluyente(Long idArtist);
	
}
