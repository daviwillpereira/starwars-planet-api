package br.com.study.domain;

import static br.com.study.commons.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;;

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

}
