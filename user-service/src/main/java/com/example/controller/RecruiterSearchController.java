package com.example.controller;

import com.example.dto.SeekerMatchDto;
import com.example.service.RecruiterSearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruiter/search")
public class RecruiterSearchController {

    public static class SearchRequest {
        public List<Long> skillIds;
        public long minYearsExperience;
    }

    @Autowired
    RecruiterSearchService searchService;

    public RecruiterSearchController(RecruiterSearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<List<SeekerMatchDto>> search(@RequestBody SearchRequest request) {
        try {
            List<Long> skillIds = request.skillIds; // Keep null as null, don't convert to empty list
            long minYearsExperience = request.minYearsExperience;

            System.out.println(
                    "Recruiter search request - Skill IDs: " + skillIds + ", Min Experience: " + minYearsExperience);

            List<SeekerMatchDto> results = searchService.findSeekersBySkillsAndExperience(skillIds, minYearsExperience);

            System.out.println("Found " + results.size() + " matching seekers");

            return ResponseEntity.ok(results);
        } catch (Exception e) {
            System.err.println("Error in recruiter search: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
