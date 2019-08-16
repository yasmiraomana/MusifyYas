package com.examen.springboot.app.dto;

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

public class ArtistDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;	
	private String name;	
	private Integer year;	
	private List<StyleDto> styles;		
	private List<PeopleDto> members;
	private List<ArtistDto> related;	
	private String listStyles;
	private String listMembers;
	private String listPeople;
	private String listArtistRelated;
	private Long buscar;
	
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
	public List<StyleDto> getStyles() {
		return styles;
	}
	public void setStyles(List<StyleDto> styles) {
		this.styles = styles;
	}
	public List<PeopleDto> getMembers() {
		return members;
	}
	public void setMembers(List<PeopleDto> members) {
		this.members = members;
	}
	public List<ArtistDto> getRelated() {
		return related;
	}
	public void setRelated(List<ArtistDto> related) {
		this.related = related;
	}
	public String getListStyles() {
		return listStyles;
	}
	public void setListStyles(String listStyles) {
		this.listStyles = listStyles;
	}
	public String getListMembers() {
		return listMembers;
	}
	public void setListMembers(String listMembers) {
		this.listMembers = listMembers;
	}
	public String getListPeople() {
		return listPeople;
	}
	public void setListPeople(String listPeople) {
		this.listPeople = listPeople;
	}
	public String getListArtistRelated() {
		return listArtistRelated;
	}
	public void setListArtistRelated(String listArtistRelated) {
		this.listArtistRelated = listArtistRelated;
	}
	public Long getBuscar() {
		return buscar;
	}
	public void setBuscar(Long buscar) {
		this.buscar = buscar;
	}
	

	
}
