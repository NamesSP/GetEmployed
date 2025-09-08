package com.example.client;

import com.example.dto.ExperienceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "experience-service", path = "/experiences")
public interface ExperienceServiceClient {

    @GetMapping
    List<ExperienceDto> getAllExperiences();

    @GetMapping("/{id}")
    ExperienceDto getExperienceById(@PathVariable("id") Long id);

    @PostMapping
    ExperienceDto addExperience(@RequestBody ExperienceDto experience);

    @PutMapping("/{id}")
    ExperienceDto updateExperience(@PathVariable("id") Long id, @RequestBody ExperienceDto experience);

    @DeleteMapping("/{id}")
    void deleteExperience(@PathVariable("id") Long id);

    @GetMapping("/user/{userId}")
    List<ExperienceDto> getExperiencesByUser(@PathVariable("userId") Long userId);
}
