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
@Table(name = "artists")
public class Artist implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private Integer year;	
	
	@JoinTable(
			name = "rel_artist_style",
			joinColumns = @JoinColumn(name="FK_ARTIST" ,nullable = false),
			inverseJoinColumns = @JoinColumn(name = "FK_STYLE", nullable = false)
				)
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Style> styles;		
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "artist_id")
	private List<People> members;
	
	@JoinTable(
			name = "rel_artist_related",
			joinColumns = @JoinColumn(name="FK_ARTIST" ,nullable = false),
			inverseJoinColumns = @JoinColumn(name = "FK_ARTIST_RELATED", nullable = false)
				)
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Artist> related;	
	
	public void addStyles(Style styles) {
		if(this.styles == null) {
			this.styles = new ArrayList<Style>();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<Style> getStyles() {
		return styles;
	}

	public void setStyles(List<Style> styles) {
		this.styles = styles;
	}

	public List<People> getMembers() {
		return members;
	}

	public void setMembers(List<People> members) {
		this.members = members;
	}

	public List<Artist> getRelated() {
		return related;
	}

	public void setRelated(List<Artist> related) {
		this.related = related;
	}

	
}
