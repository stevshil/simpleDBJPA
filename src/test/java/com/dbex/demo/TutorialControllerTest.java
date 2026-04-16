package com.dbex.demo;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class TutorialControllerTest {

	private MockMvc mockMvc;

	@Mock
	private TutorialRepository tutorialRepository;

	private TutorialController tutorialController;

	private ObjectMapper objectMapper;

	private Tutorial tutorial1;
	private Tutorial tutorial2;

	@BeforeEach
	void setUp() {
		tutorialController = new TutorialController();
		tutorialController.tutorialRepository = tutorialRepository;
		mockMvc = MockMvcBuilders.standaloneSetup(tutorialController).build();
		objectMapper = new ObjectMapper();

		tutorial1 = new Tutorial("Java Tutorial", "Learn Java basics", false);
		tutorial1.setId(1L);

		tutorial2 = new Tutorial("Spring Boot Guide", "Master Spring Boot", true);
		tutorial2.setId(2L);
	}

	@Test
	void testGetAllTutorials_Empty() throws Exception {
		when(tutorialRepository.findAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(get("/api/tutorials"))
			.andExpect(status().isNoContent());

		verify(tutorialRepository, times(1)).findAll();
	}

	@Test
	void testGetAllTutorials_WithTitleFilter() throws Exception {
		List<Tutorial> tutorials = new ArrayList<>();
		tutorials.add(tutorial1);

		when(tutorialRepository.findByTitleContaining("Java")).thenReturn(tutorials);

		mockMvc.perform(get("/api/tutorials").param("title", "Java"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].title").value("Java Tutorial"));

		verify(tutorialRepository, times(1)).findByTitleContaining("Java");
	}

	@Test
	void testGetAllTutorials_NoResultsForFilter() throws Exception {
		when(tutorialRepository.findByTitleContaining("Nonexistent")).thenReturn(new ArrayList<>());

		mockMvc.perform(get("/api/tutorials").param("title", "Nonexistent"))
			.andExpect(status().isNoContent());

		verify(tutorialRepository, times(1)).findByTitleContaining("Nonexistent");
	}

	@Test
	void testGetTutorialById_NotFound() throws Exception {
		when(tutorialRepository.findById(999L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/tutorials/999"))
			.andExpect(status().isNotFound());

		verify(tutorialRepository, times(1)).findById(999L);
	}

	@Test
	void testCreateTutorial_InvalidInput() throws Exception {
		when(tutorialRepository.save(any(Tutorial.class)))
			.thenThrow(new RuntimeException("Invalid tutorial data"));

		mockMvc.perform(post("/api/tutorials")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(new Tutorial())))
			.andExpect(status().isInternalServerError());

		verify(tutorialRepository, times(1)).save(any(Tutorial.class));
	}

	@Test
	void testUpdateTutorial_NotFound() throws Exception {
		Tutorial updatedData = new Tutorial("Updated Title", "Updated description", true);

		when(tutorialRepository.findById(999L)).thenReturn(Optional.empty());

		mockMvc.perform(put("/api/tutorials/999")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(updatedData)))
			.andExpect(status().isNotFound());

		verify(tutorialRepository, times(1)).findById(999L);
		verify(tutorialRepository, never()).save(any(Tutorial.class));
	}

	@Test
	void testDeleteTutorial_Error() throws Exception {
		doThrow(new RuntimeException("Database error")).when(tutorialRepository).deleteById(any());

		mockMvc.perform(delete("/api/tutorials/1"))
			.andExpect(status().isInternalServerError());

		verify(tutorialRepository, times(1)).deleteById(1L);
	}
}
