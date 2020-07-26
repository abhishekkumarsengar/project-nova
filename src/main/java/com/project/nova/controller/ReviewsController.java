package com.project.nova.controller;

import com.project.nova.dto.AggregatedReviewsResponse;
import com.project.nova.dto.ReviewRequest;
import com.project.nova.dto.ReviewResponse;
import com.project.nova.entity.BreakdownRating;
import com.project.nova.entity.Rating;
import com.project.nova.entity.Review;
import com.project.nova.service.ReviewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
public class ReviewsController {

    private ReviewsService reviewsService;

    @Autowired
    private ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @ApiOperation(value = "Create a policy", notes = "Creates a new policy")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 400, message = "Array of bad fields")
    })
    @ResponseBody
    @GetMapping("/products/{productId}/reviews")
    @ResponseStatus(HttpStatus.OK)
    private ReviewResponse getAllReviews(@PathVariable(value = "productId") UUID productId,
                                         @RequestParam(value = "rating", required = false) Integer rating,
                                         @RequestParam(value = "sort", required = false) String sort,
                                         @RequestParam(value = "order", required = false) String order,
                                         @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return reviewsService.getAllReviews(productId, rating, sort, order, pageNumber, pageSize);
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
                                BindingResult bindingResult) throws Exception {
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
    private BreakdownRating getBreakDownRatings(@PathVariable(value = "productId") UUID productId) {
        return reviewsService.getBreakDownReviewsByRating(productId);
    }

}
