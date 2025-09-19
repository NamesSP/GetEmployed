package com.example.controller;

import com.example.controller.RecruiterSearchController.SearchRequest;
import com.example.dto.SeekerMatchDto;
import com.example.service.RecruiterSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

public class RecruiterSearchControllerTest {

    private RecruiterSearchService searchService;
    private RecruiterSearchController controller;

    @BeforeEach
    void setUp() {
        searchService = mock(RecruiterSearchService.class);
        controller = new RecruiterSearchController(searchService);
    }

    // ✅ Positive Test: Valid search request returns results
    @Test
    void testSearchSuccess() {
        try {
            SearchRequest request = new SearchRequest();
            request.skillIds = List.of(1L, 2L);
            request.minYearsExperience = 3;

            SeekerMatchDto match = new SeekerMatchDto(); // Add fields if needed
            when(searchService.findSeekersBySkillsAndExperience(request.skillIds, request.minYearsExperience))
                    .thenReturn(List.of(match));

            ResponseEntity<List<SeekerMatchDto>> response = controller.search(request);
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertFalse(response.getBody().isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ❌ Negative Test: Search returns empty list
    @Test
    void testSearchReturnsEmptyList() {
        try {
            SearchRequest request = new SearchRequest();
            request.skillIds = List.of(99L);
            request.minYearsExperience = 10;

            when(searchService.findSeekersBySkillsAndExperience(request.skillIds, request.minYearsExperience))
                    .thenReturn(Collections.emptyList());

            ResponseEntity<List<SeekerMatchDto>> response = controller.search(request);
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ❌ Negative Test: Service throws exception
    @Test
    void testSearchThrowsException() {
        try {
            SearchRequest request = new SearchRequest();
            request.skillIds = List.of(1L);
            request.minYearsExperience = 5;

            when(searchService.findSeekersBySkillsAndExperience(any(), anyLong()))
                    .thenThrow(new RuntimeException("Database error"));

            ResponseEntity<List<SeekerMatchDto>> response = controller.search(request);
            assertEquals(500, response.getStatusCodeValue());
            assertNull(response.getBody());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ❌ Negative Test: Null skillIds
    @Test
    void testSearchWithNullSkillIds() {
        try {
            SearchRequest request = new SearchRequest();
            request.skillIds = null;
            request.minYearsExperience = 2;

            when(searchService.findSeekersBySkillsAndExperience(null, 2L))
                    .thenReturn(Collections.emptyList());

            ResponseEntity<List<SeekerMatchDto>> response = controller.search(request);
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
