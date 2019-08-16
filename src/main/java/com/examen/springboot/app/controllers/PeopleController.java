package com.examen.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.examen.springboot.app.models.entity.People;
import com.examen.springboot.app.service.IPeopleService;

@Controller
public class PeopleController {

	@Autowired
	private IPeopleService peopleService;
	
	@RequestMapping(value = "/pagePeople", method = RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo","Lista de Gente");
		model.addAttribute("ePeople", peopleService.findListPeople());
		return "pagePeople";
		
	}
	
	@RequestMapping(value="/editPeople")
	public String crear(Map<String, Object> model) {
		People people = new People();
		model.put("ePeople", people);
		model.put("titulo", "Gente Nueva");
		model.put("editar", Long.valueOf(1));
		return "editPeople";
	}

	@RequestMapping(value="/editPeople", method = RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute("ePeople") People ePeople, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Gente Nueva");
			return "editPeople";
		}
		peopleService.savePeople(ePeople);
		return "redirect:/pagePeople";
	}
	
	@RequestMapping(value = "/editPeople/{id}")
	public String find(@PathVariable(value = "id") Long id, Map<String, Object> model) {
		People ePeople = null;
		
		if(id > 0) {
			ePeople = peopleService.findPeopleById(id);
		}else {
			return "redirect:/pagePeople";
		}
		model.put("ePeople", ePeople);
		model.put("editar", Long.valueOf(2));
		model.put("titulo", "Edición de Gente");
		return "editPeople";
		
	}
	
	@RequestMapping(value = "/deletePeople/{id}")
	public String delete(@PathVariable(value = "id") Long id, Map<String, Object> model) {		
		if(id > 0) {
			peopleService.deletePeopleById(id);
		}
		return "redirect:/pagePeople";
		
	}

	
}
