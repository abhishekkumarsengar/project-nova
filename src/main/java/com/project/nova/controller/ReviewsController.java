package com.project.nova.controller;

import com.project.nova.dto.AggregatedReviewsResponse;
import com.project.nova.dto.EntityResponse;
import com.project.nova.dto.ReviewRequest;
import com.project.nova.dto.ReviewResponse;
import com.project.nova.entity.BreakdownReviews;
import com.project.nova.entity.Review;
import com.project.nova.service.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
public class ReviewsController {

    private ReviewsService reviewsService;

    @Autowired
    private ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @GetMapping("/products/{productId}/reviews")
    @ResponseStatus(HttpStatus.OK)
    private ReviewResponse getAllReviews(@PathVariable(value = "productId") UUID productId,
                                         @RequestParam(value = "rating", required = false) Integer rating,
                                         @RequestParam(value = "order", required = false) String helpful,
                                         @RequestParam(value = "before", required = false) String before,
                                         @RequestParam(value = "after", required = false) String after,
                                         @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        // Sorting field - sort, order
        return reviewsService.getAllReviews(productId, rating, before, after, helpful, pageNumber, pageSize);
    }

    @GetMapping("/products/{productId}/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    private Review getOneReview(@PathVariable(value = "productId") UUID productId,
                                        @PathVariable(value = "reviewId") UUID reviewId) {
        return reviewsService.getOneReview(productId, reviewId);
    }

    @PostMapping("/products/{productId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    private Review createReview(@PathVariable UUID productId,
                                @Validated @RequestBody ReviewRequest reviewRequest,
                                BindingResult bindingResult, HttpServletRequest request) {
        return reviewsService.createReview(productId, reviewRequest, bindingResult);
    }

    @PutMapping("/products/{productId}/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    private Review updateReview(@PathVariable(value = "productId") UUID productId,
                                @PathVariable(value = "reviewId") UUID reviewId,
                                @Validated @RequestBody ReviewRequest reviewRequest, BindingResult bindingResult) {
        return reviewsService.updateReview(productId, reviewId, reviewRequest, bindingResult);
    }

    @DeleteMapping("/products/{productId}/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteReview(@PathVariable(value = "productId") UUID productId,
                              @PathVariable(value = "reviewId") UUID reviewId) {
        reviewsService.deleteReview(productId, reviewId);
    }

    @PutMapping("/products/{productId}/reviews/{reviewId}/helpful")
    @ResponseStatus(HttpStatus.OK)
    private void isHelpful(@PathVariable(value = "productId") UUID productId,
                             @PathVariable(value = "reviewId") UUID reviewId,
                             @RequestParam(value = "userId", required = true) UUID userId) {
        reviewsService.isReviewHelpFull(productId, reviewId, userId);
    }

    @GetMapping("/products/{productId}/reviews/aggregated")
    @ResponseStatus(HttpStatus.OK)
    private AggregatedReviewsResponse getAggregatedRatings(@PathVariable(value = "productId") UUID productId) {
        return reviewsService.getAggregatedReviewsByRating(productId);
    }

    @GetMapping("/products/{productId}/reviews/breakdown")
    @ResponseStatus(HttpStatus.OK)
    private BreakdownReviews getBreakDownRatings(@PathVariable(value = "productId") UUID productId) {
        return reviewsService.getBreakDownReviewsByRating(productId);
    }

}
