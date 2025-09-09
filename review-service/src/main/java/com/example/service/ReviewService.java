package com.example.service;

import com.example.entity.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewsByCompanyId(Long companyId);
    void createReview(Review review);
}
