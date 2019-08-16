package com.examen.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "REL_ARTIST_STYLE")
public class REL_ARTIST_STYLE implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private Long FK_ARTIST;
	
	private Long FK_STYLE;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFK_ARTIST() {
		return FK_ARTIST;
	}

	public void setFK_ARTIST(Long fK_ARTIST) {
		FK_ARTIST = fK_ARTIST;
	}

	public Long getFK_STYLE() {
		return FK_STYLE;
	}

	public void setFK_STYLE(Long fK_STYLE) {
		FK_STYLE = fK_STYLE;
	}
	
	
	
}
