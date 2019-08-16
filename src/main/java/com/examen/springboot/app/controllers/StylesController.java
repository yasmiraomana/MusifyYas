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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.examen.springboot.app.models.entity.Style;
import com.examen.springboot.app.service.IStylesService;

@Controller
public class StylesController {
	
	@Autowired
	private IStylesService stylesService;
	
	@RequestMapping(value = "/pageStyles", method = RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo","Listado de Styles");
		model.addAttribute("styles", stylesService.findListStyles());
		return "pageStyles";
		
	}
	
	@RequestMapping(value="/editStyles")
	public String crear(Map<String, Object> model) {
		Style eStyles = new Style();
		model.put("eStyles", eStyles);
		model.put("titulo", "Formulario de Estilos");
		return "editStyles";
	}

	@RequestMapping(value="/editStyles", method = RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute("eStyles") Style eStyles, BindingResult result, Model model, RedirectAttributes flash,SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Estilos");
			return "editStyles";
		}
		stylesService.saveStyles(eStyles);
		status.setComplete();
		flash.addFlashAttribute("success", "Se ha guardado el Style");
		return "redirect:/pageStyles";
	}
	
	@RequestMapping(value = "/editStyles/{id}")
	public String find(@PathVariable(value = "id") Long id, Map<String, Object> model) {
		Style eStyles = null;
		
		if(id > 0) {
			eStyles = stylesService.findStylesById(id);
		}else {
			return "redirect:/pageStyles";
		}
		model.put("eStyles", eStyles);
		model.put("titulo", "Formulario de Styles");
		return "editStyles";
		
	}
	
	@RequestMapping(value = "/eliminar/{id}")
	public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {		
		if(id > 0) {
			stylesService.deleteStylesById(id);
			flash.addFlashAttribute("success", "Se ha eliminado el Style");
		}
		return "redirect:/pageStyles";
		
	}

}
