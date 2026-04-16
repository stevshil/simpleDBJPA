package com.dbex.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TutorialRepositoryTest {

	@Mock
	private TutorialRepository tutorialRepository;

	private Tutorial tutorial1;
	private Tutorial tutorial2;
	private Tutorial tutorial3;

	@BeforeEach
	void setUp() {
		tutorial1 = new Tutorial("Spring Boot Tutorial", "Learn Spring Boot", true);
		tutorial1.setId(1L);

		tutorial2 = new Tutorial("Java Advanced", "Advanced Java concepts", false);
		tutorial2.setId(2L);

		tutorial3 = new Tutorial("Spring Security Guide", "Secure your applications", true);
		tutorial3.setId(3L);
	}

	// Tests for findAll
	@Test
	void testFindAll_Success() {
		List<Tutorial> tutorials = new ArrayList<>();
		tutorials.add(tutorial1);
		tutorials.add(tutorial2);
		tutorials.add(tutorial3);

		when(tutorialRepository.findAll()).thenReturn(tutorials);

		List<Tutorial> result = tutorialRepository.findAll();

		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals("Spring Boot Tutorial", result.get(0).getTitle());
		verify(tutorialRepository, times(1)).findAll();
	}

	@Test
	void testFindAll_Empty() {
		when(tutorialRepository.findAll()).thenReturn(new ArrayList<>());

		List<Tutorial> result = tutorialRepository.findAll();

		assertNotNull(result);
		assertEquals(0, result.size());
		verify(tutorialRepository, times(1)).findAll();
	}

	// Tests for findById
	@Test
	void testFindById_Success() {
		when(tutorialRepository.findById(1L)).thenReturn(Optional.of(tutorial1));

		Optional<Tutorial> result = tutorialRepository.findById(1L);

		assertTrue(result.isPresent());
		assertEquals("Spring Boot Tutorial", result.get().getTitle());
		assertTrue(result.get().isPublished());
		verify(tutorialRepository, times(1)).findById(1L);
	}

	@Test
	void testFindById_NotFound() {
		when(tutorialRepository.findById(999L)).thenReturn(Optional.empty());

		Optional<Tutorial> result = tutorialRepository.findById(999L);

		assertFalse(result.isPresent());
		verify(tutorialRepository, times(1)).findById(999L);
	}

	// Tests for findByPublished
	@Test
	void testFindByPublished_True() {
		List<Tutorial> publishedTutorials = new ArrayList<>();
		publishedTutorials.add(tutorial1);
		publishedTutorials.add(tutorial3);

		when(tutorialRepository.findByPublished(true)).thenReturn(publishedTutorials);

		List<Tutorial> result = tutorialRepository.findByPublished(true);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.stream().allMatch(Tutorial::isPublished));
		verify(tutorialRepository, times(1)).findByPublished(true);
	}

	@Test
	void testFindByPublished_False() {
		List<Tutorial> unpublishedTutorials = new ArrayList<>();
		unpublishedTutorials.add(tutorial2);

		when(tutorialRepository.findByPublished(false)).thenReturn(unpublishedTutorials);

		List<Tutorial> result = tutorialRepository.findByPublished(false);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertFalse(result.get(0).isPublished());
		verify(tutorialRepository, times(1)).findByPublished(false);
	}

	@Test
	void testFindByPublished_Empty() {
		when(tutorialRepository.findByPublished(false)).thenReturn(new ArrayList<>());

		List<Tutorial> result = tutorialRepository.findByPublished(false);

		assertNotNull(result);
		assertEquals(0, result.size());
		verify(tutorialRepository, times(1)).findByPublished(false);
	}

	// Tests for findByTitleContaining
	@Test
	void testFindByTitleContaining_SingleResult() {
		List<Tutorial> results = new ArrayList<>();
		results.add(tutorial1);

		when(tutorialRepository.findByTitleContaining("Spring Boot")).thenReturn(results);

		List<Tutorial> result = tutorialRepository.findByTitleContaining("Spring Boot");

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Spring Boot Tutorial", result.get(0).getTitle());
		verify(tutorialRepository, times(1)).findByTitleContaining("Spring Boot");
	}

	@Test
	void testFindByTitleContaining_MultipleResults() {
		List<Tutorial> results = new ArrayList<>();
		results.add(tutorial1);
		results.add(tutorial3);

		when(tutorialRepository.findByTitleContaining("Spring")).thenReturn(results);

		List<Tutorial> result = tutorialRepository.findByTitleContaining("Spring");

		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.stream().allMatch(t -> t.getTitle().contains("Spring")));
		verify(tutorialRepository, times(1)).findByTitleContaining("Spring");
	}

	@Test
	void testFindByTitleContaining_NoResults() {
		when(tutorialRepository.findByTitleContaining("Nonexistent")).thenReturn(new ArrayList<>());

		List<Tutorial> result = tutorialRepository.findByTitleContaining("Nonexistent");

		assertNotNull(result);
		assertEquals(0, result.size());
		verify(tutorialRepository, times(1)).findByTitleContaining("Nonexistent");
	}

	@Test
	void testFindByTitleContaining_CaseInsensitive() {
		List<Tutorial> results = new ArrayList<>();
		results.add(tutorial2);

		when(tutorialRepository.findByTitleContaining("java")).thenReturn(results);

		List<Tutorial> result = tutorialRepository.findByTitleContaining("java");

		assertNotNull(result);
		assertEquals(1, result.size());
		verify(tutorialRepository, times(1)).findByTitleContaining("java");
	}

	// Tests for save
	@Test
	void testSave_NewTutorial() {
		Tutorial newTutorial = new Tutorial("New Title", "New description", false);
		Tutorial savedTutorial = new Tutorial("New Title", "New description", false);
		savedTutorial.setId(4L);

		when(tutorialRepository.save(any(Tutorial.class))).thenReturn(savedTutorial);

		Tutorial result = tutorialRepository.save(newTutorial);

		assertNotNull(result);
		assertEquals(4L, result.getId());
		assertEquals("New Title", result.getTitle());
		verify(tutorialRepository, times(1)).save(any(Tutorial.class));
	}

	@Test
	void testSave_UpdateTutorial() {
		tutorial1.setTitle("Updated Title");

		when(tutorialRepository.save(any(Tutorial.class))).thenReturn(tutorial1);

		Tutorial result = tutorialRepository.save(tutorial1);

		assertNotNull(result);
		assertEquals("Updated Title", result.getTitle());
		assertEquals(1L, result.getId());
		verify(tutorialRepository, times(1)).save(any(Tutorial.class));
	}

	// Tests for deleteById
	@Test
	void testDeleteById_Success() {
		doNothing().when(tutorialRepository).deleteById(1L);

		tutorialRepository.deleteById(1L);

		verify(tutorialRepository, times(1)).deleteById(1L);
	}

	@Test
	void testDeleteById_MultipleDeletes() {
		doNothing().when(tutorialRepository).deleteById(anyLong());

		tutorialRepository.deleteById(1L);
		tutorialRepository.deleteById(2L);
		tutorialRepository.deleteById(3L);

		verify(tutorialRepository, times(3)).deleteById(anyLong());
	}

	// Tests for deleteAll
	@Test
	void testDeleteAll() {
		doNothing().when(tutorialRepository).deleteAll();

		tutorialRepository.deleteAll();

		verify(tutorialRepository, times(1)).deleteAll();
	}
}
