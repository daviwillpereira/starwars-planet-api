package br.com.study.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.study.domain.Planet;
import br.com.study.domain.PlanetService;

@RestController
@RequestMapping("/planets")
public class PlanetController {
	
	
	private final PlanetService planetService;

	
	public PlanetController(PlanetService planetService) {
		this.planetService = planetService;
	}


	@PostMapping
	public ResponseEntity<Planet> create(@RequestBody Planet planet) {
		Planet planetCreated = planetService.create(planet);
		return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
	}
}
