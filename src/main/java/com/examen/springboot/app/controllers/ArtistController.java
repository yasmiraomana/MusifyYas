package com.examen.springboot.app.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.examen.springboot.app.dto.ArtistDto;
import com.examen.springboot.app.dto.PeopleDto;
import com.examen.springboot.app.dto.StyleDto;
import com.examen.springboot.app.models.entity.Artist;
import com.examen.springboot.app.models.entity.People;
import com.examen.springboot.app.models.entity.Style;
import com.examen.springboot.app.service.IArtistService;
import com.examen.springboot.app.service.IPeopleService;
import com.examen.springboot.app.service.IStylesService;

@Controller
@SessionAttributes("listStyles listMembers listArtist")
public class ArtistController {

	@Autowired
	private IArtistService artistService;

	@Autowired
	private IStylesService stylesService;

	@Autowired
	private IPeopleService peopleService;

	/* Método que carga las primeras listas que requiere la aplicación */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String verPrincipal(Model model, HttpSession session) {
		List<Style> listStyle = stylesService.findListStyles();
		List<People> listMembers = peopleService.findListPeople();
		session.setAttribute("listStyles", listStyle);
		session.setAttribute("listMembers", listMembers);
		session.setAttribute("listArtist", artistService.findListArtist());
		return "index";
	}

	/* Método que pinta el listado de los artistas, se buscan todos sin condición */
	@RequestMapping(value = "/pageArtist", method = RequestMethod.GET)
	public String listar(Model model, HttpSession session) {
		model.addAttribute("titulo", "Listado de Artistas");
		List<Artist> artist = (List<Artist>) artistService.findListArtist();
		List<ArtistDto> listArtistDto = new ArrayList<ArtistDto>();
		for (Artist artistDao : artist) {
			ArtistDto artistDto = mappearArtistaDaoToArtistDto(artistDao);
			listArtistDto.add(artistDto);
		}

		model.addAttribute("eArtist", listArtistDto);
		return "pageArtist";

	}

	/*
	 * Método GET de la petición crearArtista aquí actualizó las variables de
	 * session de las listas de estilos y personas por si fueron modificadas
	 */
	@RequestMapping(value = "/crearArtist")
	public String getCrearArtist(Map<String, Object> model, HttpSession session) {
		ArtistDto artist = new ArtistDto();
		List<Artist> artists = (List<Artist>) artistService.findListArtist();
		List<ArtistDto> listArtistDto = new ArrayList<ArtistDto>();
		for (Artist artistDao : artists) {
			ArtistDto artistDto = mappearArtistaDaoToArtistDto(artistDao);
			listArtistDto.add(artistDto);
		}

		session.setAttribute("listStyles", listArtistDto);
		List<Style> listStyle = stylesService.findListStyles();
		List<People> listMembers = peopleService.findListPeople();
		session.setAttribute("listStyles", listStyle);
		session.setAttribute("listMembers", listMembers);
		session.setAttribute("listArtist", artistService.findListArtist());

		model.put("eArtist", artist);
		model.put("titulo", "Crear Nuevo Artista");
		return "crearArtist";
	}

	/*
	 * Post de la petición crear artista, valido si tiene error y de ser así retorno
	 * a la pantalla nuevamente sino mappeo el ArtitaDto a la entidad que voy a usar
	 * para crear el nuevo artista, valido que los string que se cargan de los
	 * select multiple para estilos, personas o artistas relacionados vengan con
	 * valor y así poder recorrerlos y usando el método converterStringToListStyle,
	 * converterStringToListPeople, converterStringToListArtist, convertir dicha
	 * cadena en la lista que necesito
	 */
	@RequestMapping(value = "/crearArtist", method = RequestMethod.POST)
	public String postCrearArtist(@Valid @ModelAttribute("eArtist") ArtistDto eArtist, BindingResult result,
			Model model, SessionStatus sts, RedirectAttributes flas) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Nuevo Artista");
			return "crearArtist";
		}

		Artist artist = mappearArtistaDtoToArtist(eArtist);

		if (eArtist.getListStyles() != null) {
			artist.setStyles(converterStringToListStyle(eArtist.getListStyles()));
		}

		if (eArtist.getListMembers() != null) {
			artist.setMembers(converterStringToListPeople(eArtist.getListMembers()));
		}

		if (eArtist.getListArtistRelated() != null) {
			artist.setRelated(converterStringToListArtist(eArtist.getListArtistRelated()));
		}

		artistService.saveArtist(artist);
		sts.setComplete();
		flas.addFlashAttribute("success", "Artista Creado Exitosamente");
		return "redirect:/pageArtist";
	}

	/*
	 * Petición GET de la edición de un artista, le paso como parámetros el id de la
	 * entidad a editar, isRelated para saber si vengo de la pantalla de artistas
	 * relacionados, ya que si viene de allí solo debo cargar la lista de los
	 * artistas relacionados, y el parámetro isEditar me permitirá activar en la
	 * pantalla ciertos campos o no, que dependerá si el usuario selecciona la
	 * opción de ver detalle del artista o si lo quiere editar
	 */
	@RequestMapping(value = "/editArtist/{id}/{isRelated}/{isEditar}")
	public String getEditArtists(@PathVariable(value = "id") Long id,
			@PathVariable(value = "isRelated", required = false) Long isRelated,
			@PathVariable(value = "isEditar", required = false) Long isEditar, Map<String, Object> model,
			HttpSession session) {
		ArtistDto eArtist = null;

		if (id > 0) {

			List<Style> listStyle = stylesService.findAllExcluyenteStyle(id);
			List<People> listMembers = peopleService.findAllExcluyente(id);
			session.setAttribute("listStyles", listStyle);
			session.setAttribute("listMembers", listMembers);
			session.setAttribute("listArtist", artistService.findListArtist());

			Artist eArtista = artistService.findArtistById(id);
			eArtist = mappearArtistaDaoToArtistDto(eArtista);
			if (eArtist.getStyles() != null && isRelated != 1) {
				model.put("eStyles", eArtist.getStyles());
			}
			if (eArtist.getMembers() != null && isRelated != 1) {
				model.put("ePeople", eArtist.getMembers());
			}
			if (eArtist.getRelated() != null) {
				model.put("eRelated", eArtist.getRelated());
			}
		} else {
			return "redirect:/pageArtist";
		}

		model.put("isRelated", isRelated);
		model.put("isEditar", isEditar);
		model.put("eArtist", eArtist);
		if(isEditar==1)
			model.put("titulo", "Detalle de Artistas");
		if(isEditar==2)
			model.put("titulo", "Edición de Artistas");
		return "editArtist";

	}

	/*
	 * Post de la solicitud de Edición, aquí recibo tres parámetros ids -> si estoy
	 * en la pantalla de edición del artista, me dirá estilos que fueron
	 * seleccionados para eliminar, idPeoples -> me dirá las personas que fueron
	 * seleccionadas para eliminar idRelated -> Me dirá los artistas relacionados
	 * que fueron seleccionados para eliminar artistRelated-> me dice de que
	 * pantalla vengo para saber a donde redireccionar
	 */
	@RequestMapping(value = "/editArtist", method = RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute("eArtist") ArtistDto eArtist, BindingResult result,
			@RequestParam(value = "idChecked", required = false) List<String> ids,
			@RequestParam(value = "idCheckedPeople", required = false) List<String> idPeoples,
			@RequestParam(value = "idCheckedRelated", required = false) List<String> idRelated,
			@RequestParam(value = "artistRelated", required = false) Long artistRelated, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Edición de Artistas");
			return "editArtist";
		} else {

			Artist oldArtist = null;
			oldArtist = artistService.findArtistById(eArtist.getId());
			oldArtist.setName(eArtist.getName());
			oldArtist.setYear(eArtist.getYear());

			if (ids != null) {
				for (int j = 0; j < oldArtist.getStyles().size(); j++) {
					for (int i = 0; i < ids.size(); i++) {
						if (oldArtist.getStyles().get(j).getId() == Long.valueOf(ids.get(i))) {
							oldArtist.getStyles().remove(j);
							break;
						}
					}
				}
			}
			if (idPeoples != null) {
				for (int j = 0; j < oldArtist.getMembers().size(); j++) {
					for (int i = 0; i < idPeoples.size(); i++) {
						if (oldArtist.getMembers().get(j).getId() == Long.valueOf(idPeoples.get(i))) {
							oldArtist.getMembers().remove(j);
							break;
						}
					}
				}
			}
			if (idRelated != null) {
				for (int j = 0; j < oldArtist.getRelated().size(); j++) {
					for (int i = 0; i < idRelated.size(); i++) {
						if (oldArtist.getRelated().get(j).getId() == Long.valueOf(idRelated.get(i))) {
							oldArtist.getRelated().remove(j);
							break;
						}
					}
				}
			}

			if (eArtist.getListStyles() != null) {
				String[] listStyles = eArtist.getListStyles().split(",");
				for (int i = 0; i < listStyles.length; i++) {
					if (oldArtist.getStyles() != null && !oldArtist.getStyles().isEmpty()) {
						oldArtist.getStyles().add(mappearStyleDtoToStyle(
								mappearStyleToStyleDto(stylesService.findStylesById(Long.valueOf(listStyles[i])))));
					}
				}
				if (oldArtist.getStyles() == null || oldArtist.getStyles().isEmpty()) {
					oldArtist.setStyles(converterStringToListStyle(eArtist.getListStyles()));
				}
			}

			if (eArtist.getListMembers() != null) {
				String[] listPeople = eArtist.getListMembers().split(",");
				for (int i = 0; i < listPeople.length; i++) {
					if (oldArtist.getMembers() != null && !oldArtist.getMembers().isEmpty()) {
						oldArtist.getMembers().add(mappearPeopleDtoToPeople(
								mappearPeopleToPeopleDto(peopleService.findPeopleById(Long.valueOf(listPeople[i])))));
					}
				}
				if (oldArtist.getMembers() == null || oldArtist.getMembers().isEmpty()) {
					oldArtist.setMembers(converterStringToListPeople(eArtist.getListMembers()));
				}
			}

			if (eArtist.getListArtistRelated() != null) {
				String[] listRelated = eArtist.getListArtistRelated().split(",");
				for (int i = 0; i < listRelated.length; i++) {
					if (oldArtist.getRelated() != null && !oldArtist.getRelated().isEmpty()) {
						oldArtist.getRelated().add(mappearArtistaDtoToArtist(mappearArtistaDaoToArtistDto(
								artistService.findArtistById(Long.valueOf(listRelated[i])))));
					}
				}
				if (oldArtist.getRelated() == null || oldArtist.getRelated().isEmpty()) {
					oldArtist.setRelated(converterStringToListArtist(eArtist.getListArtistRelated()));
				}
			}

			artistService.saveArtist(oldArtist);
		}

		if (artistRelated == 1) {
			return "redirect:/pageArtistRelated";
		} else {
			return "redirect:/pageArtist";
		}

	}

	/*
	 * Método con petición GET a la pantalla de asignar gente a los artistas, por
	 * defecto busco las personas del primer artista
	 */
	@RequestMapping(value = "/addPeople/{id}")
	public String getAddPersonas(@Valid @ModelAttribute("eArtist") ArtistDto eArtist, 
			@PathVariable(value = "id") Long id, Map<String, Object> model,
			HttpSession session) {
		model.put("eArtist", eArtist);
		model.put("titulo", "Asignar Gente");
		session.setAttribute("listMembers", peopleService.findAllExcluyente(id));
		session.setAttribute("buscaPersonas", 1);
		session.setAttribute("listArtist", artistService.findListArtist());
		return "addPeople";
	}

	/*
	 * Post de la petición de crear artista, valido que el string que contiene el
	 * valor traido de select multiple de personas este lleno y uso de nuevo el
	 * metodo converterStringToListPeople para pasar el string a una lista
	 */
	@RequestMapping(value = "/addPeople", method = RequestMethod.POST)
	public String postAddPersonas(@Valid @ModelAttribute("eArtist") ArtistDto eArtist, Model model, HttpSession session,
			SessionStatus sts, RedirectAttributes flas) {

		Artist artist = artistService.findArtistById(eArtist.getId());

		if (eArtist.getBuscar() == null) {
			session.setAttribute("listMembers", peopleService.findAllExcluyente(eArtist.getId()));
			session.setAttribute("buscaPersonas", 1);

			return "redirect:/addPeople/"+eArtist.getId();
		} else if (eArtist.getListMembers() != null && eArtist.getBuscar() != null) {
			if (artist.getMembers() != null && !artist.getMembers().isEmpty()) {
				List<People> listPeople = converterStringToListPeople(eArtist.getListMembers());
				for (int j = 0; j < listPeople.size(); j++) {
					for (int i = 0; i < artist.getMembers().size(); i++) {
						if (artist.getMembers().get(i).getId() != listPeople.get(j).getId()) {
							artist.getMembers().add(listPeople.get(j));
							break;
						}
					}
				}
			} else {
				artist.setMembers(converterStringToListPeople(eArtist.getListMembers()));
			}
			artistService.saveArtist(artist);
			sts.setComplete();
			flas.addFlashAttribute("success", "Artista Creado Exitosamente");

		}

		return "redirect:/pageArtist";

	}

	/*
	 * GET para relacionar un artista con otro actualizó de nuevo las variables en
	 * session por si han ido cambiando
	 */
	@RequestMapping(value = "/relatedArtist")
	public String getRelacionarArtistas(Map<String, Object> model, HttpSession session) {
		ArtistDto artist = new ArtistDto();

		List<Style> listStyle = stylesService.findListStyles();
		List<People> listMembers = peopleService.findListPeople();
		session.setAttribute("listStyles", listStyle);
		session.setAttribute("listMembers", listMembers);
		session.setAttribute("listArtist", artistService.findListArtist());

		model.put("eArtist", artist);
		model.put("titulo", "Relacionar Artistas");

		return "artistRelated";
	}

	/*
	 * Post de la petición de relacionar los artistas, validó que el string con los
	 * artistas que fueron seleccionados venga con datos y los convierto a la lista
	 * que se mapeara en la entidad
	 */
	@RequestMapping(value = "/relatedArtist", method = RequestMethod.POST)
	public String postRelacionarArtistas(@Valid @ModelAttribute("eArtist") ArtistDto eArtist,
			Map<String, Object> model) {
		if (eArtist.getId() != null) {
			Artist artist = artistService.findArtistById(eArtist.getId());
			List<Artist> listArtist = new ArrayList<Artist>();
			if (eArtist.getListArtistRelated() != null) {
				if (artist.getRelated() != null && !artist.getRelated().isEmpty()) {

					if (eArtist.getListArtistRelated() != null) {
						if (artist.getRelated() != null && !artist.getRelated().isEmpty()) {
							List<Artist> listRelated = converterStringToListArtist(eArtist.getListArtistRelated());
							for (int j = 0; j < listRelated.size(); j++) {
								for (int i = 0; i < artist.getRelated().size(); i++) {
									if (artist.getRelated().get(i).getId() != listRelated.get(j).getId()) {
										artist.getRelated().add(listRelated.get(j));
										break;
									}
								}
							}
						} else {
							artist.setRelated(converterStringToListArtist(eArtist.getListMembers()));
						}
					}
				} else {
					listArtist = converterStringToListArtist(eArtist.getListArtistRelated());
					artist.setRelated(listArtist);
				}

				artistService.saveArtist(artist);
			}

		}

		return "redirect:/pageArtistRelated";
	}

	/*
	 * GET para ver el listado de los artistas relacionados, consulto el listado de
	 * artistas y veo cuales tienen su lista de artistas relacionados diferente de
	 * null la recorro y armo 2 string uno con los estilos del artista, y otro con
	 * los nombres de los artistas a los que está relacionado y se pasan estos datos
	 * a la vista.
	 */
	@RequestMapping(value = "/pageArtistRelated")
	public String getVerArtistRelated(Map<String, Object> model, HttpSession session) {
		List<Style> listStyle = stylesService.findListStyles();
		List<People> listMembers = peopleService.findListPeople();
		session.setAttribute("listStyles", listStyle);
		session.setAttribute("listMembers", listMembers);
		session.setAttribute("listArtist", artistService.findListArtist());

		List<ArtistDto> artist = new ArrayList<ArtistDto>();
		List<Artist> artRelated = artistService.findListArtist();
		StringBuffer strBuffer = new StringBuffer();
		StringBuffer strBufferRelated = new StringBuffer();
		for (Artist artistRelated : artRelated) {
			if (artistRelated.getRelated() != null && !artistRelated.getRelated().isEmpty()) {
				strBuffer = new StringBuffer();
				strBufferRelated = new StringBuffer();
				ArtistDto artRelateds = new ArtistDto();
				if (artistRelated.getRelated() != null && !artistRelated.getRelated().isEmpty()) {
					if (artistRelated.getStyles() != null && !artistRelated.getStyles().isEmpty()) {
						for (Style style : artistRelated.getStyles()) {
							strBuffer.append("/ " + style.getName() + " ");
						}

					}

					for (Artist artists : artistRelated.getRelated()) {
						strBufferRelated.append("/ " + artists.getName() + " ");
						artRelateds = mappearArtistaDaoToArtistDto(artistRelated);
						artRelateds.setListArtistRelated(strBufferRelated.toString());
						artRelateds.setListStyles(strBuffer.toString());

					}
					artist.add(artRelateds);
				}

			}
		}

		model.put("eArtist", artist);
		model.put("titulo", "Ver Artistas Relacionados");

		return "pageArtistRelated";

	}

	/*
	 * GET para buscar artistas por estilo, cargó la lista de los estilos existentes
	 * y los muestro en el select correspondiente
	 */
	@RequestMapping(value = "/findArtist")
	public String getFindArtist(Map<String, Object> model, HttpSession session) {
		ArtistDto artist = new ArtistDto();
		List<Artist> artists = (List<Artist>) artistService.findListArtist();

		List<ArtistDto> listArtistDto = new ArrayList<ArtistDto>();
		for (Artist artistDao : artists) {
			ArtistDto artistDto = mappearArtistaDaoToArtistDto(artistDao);
			listArtistDto.add(artistDto);
		}

		List<Style> listStyle = stylesService.findListStyles();
		session.setAttribute("listStyles", listStyle);
		session.setAttribute("listArtist", session.getAttribute("listArtistStyle"));

		model.put("eArtist", artist);
		model.put("titulo", "Buscar Artista Por Estilo");
		return "buscarArtistas";
	}

	/*
	 * POST de la petición buscar artista por estilo, aquí recibo el id del estilo
	 * por el que voy a realizar la búsqueda, y busco en la entidad que tiene la
	 * relación entre ambas tablas que artista tiene dicho estilo, y hago un
	 * redirect de nuevo al GET de la página para continuar con la búsqueda.
	 */
	@RequestMapping(value = "/findArtist", method = RequestMethod.POST)
	public String postFindArtist(@Valid @ModelAttribute("eArtist") ArtistDto eArtist, Map<String, Object> model,
			HttpSession session) {
		ArtistDto artist = new ArtistDto();
		System.out.println(artist);
		session.setAttribute("listArtistStyle", artistService.findAllExcluyente(eArtist.getId()));
		return "redirect:/findArtist";
	}

	/*
	 * Método que me permite pasar la entidad o DAO de Artsita a un objeto DTO para
	 * así poder pasarlo a la vista y no usar la entidad directamente
	 */
	private ArtistDto mappearArtistaDaoToArtistDto(Artist artistDao) {
		ArtistDto artistDto = new ArtistDto();
		artistDto.setId(artistDao.getId());
		artistDto.setName(artistDao.getName());
		artistDto.setYear(artistDao.getYear());
		List<StyleDto> listStyleDto = new ArrayList<>();
		if (artistDao.getStyles() != null) {
			for (Style styleDao : artistDao.getStyles()) {
				StyleDto styleDto = new StyleDto();
				styleDto.setId(styleDao.getId());
				styleDto.setName(styleDao.getName());
				listStyleDto.add(styleDto);
			}
			artistDto.setStyles(listStyleDto);
		}

		List<PeopleDto> listPeopleDto = new ArrayList<PeopleDto>();
		if (artistDao.getMembers() != null) {
			for (People peopleDao : artistDao.getMembers()) {
				PeopleDto peopleDto = new PeopleDto();
				peopleDto.setId(peopleDao.getId());
				peopleDto.setName(peopleDao.getName());
				peopleDto.setYears(peopleDao.getYears());
				listPeopleDto.add(peopleDto);
			}
			artistDto.setMembers(listPeopleDto);
		}

		List<ArtistDto> artistRelated = new ArrayList<ArtistDto>();
		if (artistDao.getRelated() != null) {
			for (Artist artistDaos : artistDao.getRelated()) {
				ArtistDto artistRelatedDto = new ArtistDto();
				artistRelatedDto.setId(artistDaos.getId());
				artistRelatedDto.setName(artistDaos.getName());
				artistRelatedDto.setYear(artistDaos.getYear());
				artistRelated.add(artistRelatedDto);
			}
			artistDto.setRelated(artistRelated);
		}
		return artistDto;
	}

	/*Método que me permite pasar el DTO a la entidad o DAO de Artsita para así poder pasarlo 
	 * al repositorio*/
	private Artist mappearArtistaDtoToArtist(ArtistDto artistDto) {
		Artist artistDao = new Artist();
		artistDao.setId(artistDto.getId());
		artistDao.setName(artistDto.getName());
		artistDao.setYear(artistDto.getYear());
		List<Style> listStyle = new ArrayList<Style>();
		if (artistDto.getStyles() != null) {
			for (StyleDto styleDao : artistDto.getStyles()) {
				Style style = new Style();
				style.setId(styleDao.getId());
				style.setName(styleDao.getName());
				listStyle.add(style);
			}
			artistDao.setStyles(listStyle);
		}

		List<People> listPeople = new ArrayList<People>();
		if (artistDto.getMembers() != null) {
			for (PeopleDto peopleDto : artistDto.getMembers()) {
				People people = new People();
				people.setId(peopleDto.getId());
				people.setName(peopleDto.getName());
				people.setYears(peopleDto.getYears());
				listPeople.add(people);
			}
			artistDao.setMembers(listPeople);
		}

		List<Artist> artistRelateds = new ArrayList<Artist>();
		if (artistDto.getRelated() != null) {
			for (ArtistDto artistDtos : artistDto.getRelated()) {
				Artist artistRelated = new Artist();
				artistRelated.setId(artistDtos.getId());
				artistRelated.setName(artistDtos.getName());
				artistRelated.setYear(artistDtos.getYear());
				artistRelateds.add(artistRelated);
			}
			artistDao.setRelated(artistRelateds);
		}
		return artistDao;
	}

	/*Método que me permite pasar la entidad o DAO de Estilos a un objeto DTO para así poder pasarlo 
	 * a la vista y no usar la entidad directamente*/
	private StyleDto mappearStyleToStyleDto(Style styleDao) {
		StyleDto styleDto = new StyleDto();

		styleDto.setId(styleDao.getId());
		styleDto.setName(styleDao.getName());

		return styleDto;

	}

	/*Método que me permite pasar el DTO a la entidad o DAO de Estilos para así poder pasarlo 
	 * al repositorio*/
	private Style mappearStyleDtoToStyle(StyleDto styleDto) {
		Style style = new Style();

		style.setId(styleDto.getId());
		style.setName(styleDto.getName());

		return style;
	}

	/*Método que me permite pasar la entidad o DAO de Personas a un objeto DTO para así poder pasarlo 
	 * a la vista y no usar la entidad directamente*/
	private PeopleDto mappearPeopleToPeopleDto(People peopleDao) {
		PeopleDto peopleDto = new PeopleDto();

		peopleDto.setId(peopleDao.getId());
		peopleDto.setName(peopleDao.getName());
		peopleDto.setYears(peopleDao.getYears());

		return peopleDto;

	}

	/*Método que me permite pasar el DTO a la entidad o DAO de Personas para así poder pasarlo 
	 * al repositorio*/
	private People mappearPeopleDtoToPeople(PeopleDto peopleDto) {
		People people = new People();

		people.setId(peopleDto.getId());
		people.setName(peopleDto.getName());
		people.setYears(peopleDto.getYears());

		return people;
	}

	/*Método que me permite convertir el String con el listado de estilos, que viene del select multiple
	 * convertilo a la lista de estilos requerida para pasarla luego al repositorio*/
	private List<Style> converterStringToListStyle(String listStyles) {
		String[] listStyle = listStyles.split(",");
		List<Style> styles = new ArrayList<Style>();
		for (int i = 0; i < listStyle.length; i++) {
			Style style = stylesService.findStylesById(Long.valueOf(listStyle[i]));
			styles.add(style);
		}
		return styles;
	}

	/*Método que me permite convertir el String con el listado de personas, que viene del select multiple
	 * convertilo a la lista de personas requerida para pasarla luego al repositorio*/
	private List<People> converterStringToListPeople(String listPeople) {
		String[] listPeoples = listPeople.split(",");
		List<People> peoples = new ArrayList<People>();
		for (int i = 0; i < listPeoples.length; i++) {
			People people = peopleService.findPeopleById(Long.valueOf(listPeoples[i]));
			peoples.add(people);
		}
		return peoples;
	}

	/*Método que me permite convertir el String con el listado de artistas relacionados, que viene del select multiple
	 * convertilo a la lista de artistas requerida para pasarla luego al repositorio*/
	private List<Artist> converterStringToListArtist(String listArtist) {
		String[] listArtists = listArtist.split(",");
		List<Artist> artists = new ArrayList<Artist>();
		for (int i = 0; i < listArtists.length; i++) {
			Artist artist = artistService.findArtistById(Long.valueOf(listArtists[i]));
			artists.add(artist);
		}
		return artists;
	}

}
