package com.example.controller;

import com.example.dto.SeekerMatchDto;
import com.example.service.RecruiterSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruiter/search")
public class RecruiterSearchController {

    private static class SearchRequest {
        public List<Long> skillIds;
        public long minYearsExperience;
    }

    private final RecruiterSearchService searchService;

    public RecruiterSearchController(RecruiterSearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<List<SeekerMatchDto>> search(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(searchService.findSeekersBySkillsAndExperience(
                request.skillIds == null ? List.of() : request.skillIds,
                request.minYearsExperience));
    }
}
