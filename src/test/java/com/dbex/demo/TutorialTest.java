package com.dbex.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TutorialTest {

	@Test
	void testConstructors() {
		// Test no-args constructor
		Tutorial emptyTutorial = new Tutorial();
		assertNotNull(emptyTutorial);

		// Test parameterized constructor
		Tutorial newTutorial = new Tutorial("Java Basics", "Learn Java fundamentals", true);
		assertEquals("Java Basics", newTutorial.getTitle());
		assertEquals("Learn Java fundamentals", newTutorial.getDescription());
		assertTrue(newTutorial.isPublished());
	}

	@Test
	void testGettersSetters() {
		Tutorial tutorial = new Tutorial("Test Title", "Test Description", false);

		// Test ID getter/setter
		tutorial.setId(5L);
		assertEquals(5L, tutorial.getId());

		// Test title getter/setter
		tutorial.setTitle("Updated Title");
		assertEquals("Updated Title", tutorial.getTitle());

		// Test description getter/setter
		tutorial.setDescription("Updated Description");
		assertEquals("Updated Description", tutorial.getDescription());

		// Test published getter/setter
		assertFalse(tutorial.isPublished());
		tutorial.setPublished(true);
		assertTrue(tutorial.isPublished());
	}

	@Test
	void testEdgeCases() {
		Tutorial tutorial = new Tutorial();

		// Test null values
		tutorial.setTitle(null);
		tutorial.setDescription(null);
		assertNull(tutorial.getTitle());
		assertNull(tutorial.getDescription());

		// Test empty strings
		tutorial.setTitle("");
		tutorial.setDescription("");
		assertEquals("", tutorial.getTitle());
		assertEquals("", tutorial.getDescription());

		// Test long strings
		String longTitle = "A".repeat(1000);
		String longDescription = "B".repeat(5000);
		tutorial.setTitle(longTitle);
		tutorial.setDescription(longDescription);
		assertEquals(longTitle, tutorial.getTitle());
		assertEquals(longDescription, tutorial.getDescription());
	}

	@Test
	void testPublishedToggle() {
		Tutorial tutorial = new Tutorial();
		assertFalse(tutorial.isPublished());

		tutorial.setPublished(true);
		assertTrue(tutorial.isPublished());

		tutorial.setPublished(false);
		assertFalse(tutorial.isPublished());
	}

	@Test
	void testToString() {
		Tutorial tutorial = new Tutorial("Test Title", "Test Description", true);
		tutorial.setId(1L);
		String result = tutorial.toString();

		assertNotNull(result);
		assertTrue(result.contains("Tutorial"));
		assertTrue(result.contains("Test Title"));
		assertTrue(result.contains("Test Description"));
		assertTrue(result.contains("true"));
	}

	@Test
	void testIdBoundaries() {
		Tutorial tutorial = new Tutorial();

		// Test zero value
		tutorial.setId(0L);
		assertEquals(0L, tutorial.getId());

		// Test max value
		tutorial.setId(Long.MAX_VALUE);
		assertEquals(Long.MAX_VALUE, tutorial.getId());
	}

	@Test
	void testTutorialEquality() {
		Tutorial tutorial1 = new Tutorial("Title", "Description", true);
		tutorial1.setId(1L);

		Tutorial tutorial2 = new Tutorial("Title", "Description", true);
		tutorial2.setId(1L);

		// Note: Tutorial class doesn't override equals(), so this tests object identity
		assertNotEquals(tutorial1, tutorial2);
	}
}
