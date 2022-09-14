package br.com.study.domain;

import static br.com.study.commons.PlanetConstants.INVALID_PLANET;
import static br.com.study.commons.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;;

/*
 * Scan the SpringBoot Application Context looking for beans marked in the main project
 * and let them available to be injected 
 * 
 * The property 'classes' limits this scan for beans to specifics dependencies, 
 * to enhance the testing bootstrap.
 * 
 * When we're performing unit tests, instead of using springboot with mockito, is a good practice 
 * use pure mockito because many resources are used to prepare the managed the testing beans 
 * through springboot and as we're just doing unit tests, the spring approach isn't needed.
*/
// @SpringBootTest(classes = PlanetService.class)

@ExtendWith(MockitoExtension.class)
class PlanetServiceTest {
	// @Autowired
	
	@InjectMocks
	private PlanetService planetService;
	
	// @MockBean
	@Mock
	private PlanetRepository planetRepository;
	
	// operation_state_return
	@Test
	void createPlanet_withValidData_returnsPlanet() {
		//Stub no Mockito:
		when(planetRepository.save(PLANET)).thenReturn(PLANET);
		
		//System Under Test - SUT
		Planet sut = planetService.create(PLANET);
		assertThat(sut).isEqualTo(PLANET);
	}
	
	@Test
	void createPlanet_WithInvalidData_ThrowsException() {
		when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);
		
		assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
	}
	
	@Test
	void getPlanet_ByExistingId_returnsPlanet() {
		when(planetRepository.findById(1L)).thenReturn(Optional.of(PLANET));
		Optional<Planet> sut = planetService.get(1L);
		
		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(PLANET);
	}
	@Test
	void getPlanet_ByUnexistingId_returnsEmpty() {
		when(planetRepository.findById(1L)).thenReturn(Optional.empty());
		Optional<Planet> sut = planetService.get(1L);
		
		assertThat(sut).isEmpty();
	}
	
	@Test
	void getPlanet_ByExistingName_returnsPlanet() {
		when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));
		Optional<Planet> sut = planetService.get(PLANET.getName());
		
		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(PLANET);
	}
	@Test
	void getPlanet_ByUnexistingName_returnsEmpty() {
		final String name = "UnexistingName";
		when(planetRepository.findByName(name)).thenReturn(Optional.empty());
		Optional<Planet> sut = planetService.get(name);
		
		assertThat(sut).isEmpty();
	}
	
	@Test
	void listPlanets_ReturnsAllPlanets() {
		final List<Planet> planets = new ArrayList<>();
		planets.add(PLANET);
		Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));

		when(planetRepository.findAll(query)).thenReturn(planets);
		
		List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());
		
		assertThat(sut).isNotEmpty().hasSize(1);
		assertThat(sut.get(0)).isEqualTo(PLANET);
	}

	@Test
	void listPlanets_ReturnsNoPlanets() {
		Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));
		when(planetRepository.findAll(query)).thenReturn(Collections.emptyList());
		
		List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());
		
		assertThat(sut).isEmpty();
	}
	
	@Test
	void removePlanet_WithExistingId_doesNotThrowAnyException() {
		assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
	}

	@Test
	void removePlanet_WithUnexistingId_doesNotThrowAnyException() {
		doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);
		
		assertThatThrownBy(() -> planetService.remove(99L)).isInstanceOf(RuntimeException.class);
	}

}
