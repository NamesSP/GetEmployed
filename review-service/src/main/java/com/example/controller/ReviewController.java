package com.example.controller;

import com.example.entity.Review;
import com.example.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviewsByCompanyId(@RequestParam Long companyId) {
        return new ResponseEntity<>(reviewService.getReviewsByCompanyId(companyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createReview(@RequestBody Review review) {
        reviewService.createReview(review);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
