package com.project.nova.service;

import com.project.nova.dto.AggregatedReviewsResponse;
import com.project.nova.dto.ReviewRequest;
import com.project.nova.dto.ReviewResponse;
import com.project.nova.entity.BreakdownRating;
import com.project.nova.entity.HelpfulReview;
import com.project.nova.entity.Rating;
import com.project.nova.entity.Review;
import com.project.nova.exceptions.*;
import com.project.nova.repository.HelpfulReviewsRepository;
import com.project.nova.repository.RatingRepository;
import com.project.nova.repository.RatingRepositoryImpl;
import com.project.nova.repository.ReviewsRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.persistence.PessimisticLockException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReviewServiceImplTest {

    @Autowired
    private ReviewsService reviewsService;

    @MockBean
    private ReviewsRepository reviewsRepository;

    @MockBean
    private RatingRepository ratingRepository;

    @MockBean
    private RatingRepositoryImpl ratingRepositoryImpl;

    @MockBean
    private BindingResult bindingResult;

    @MockBean
    private HelpfulReviewsRepository helpfulReviewsRepository;

    Page<Review> reviewPage;
    Review review;
    ReviewRequest reviewRequest;
    Rating rating;
    AggregatedReviewsResponse aggregatedReviewsResponse;
    BreakdownRating breakdownRating;

    // Relace with JSON and map to objects
    @BeforeEach
    void init() {
        review = new Review();
        reviewRequest = new ReviewRequest();
        rating = new Rating();
        aggregatedReviewsResponse = new AggregatedReviewsResponse();
        aggregatedReviewsResponse.setRating(2.3);
        aggregatedReviewsResponse.setNumberOfReviews(9);
        breakdownRating = new BreakdownRating();
        breakdownRating.setProductId(UUID.randomUUID());
        breakdownRating.setRating_1(1);
        breakdownRating.setRating_2(2);
        breakdownRating.setRating_3(3);
        breakdownRating.setRating_4(4);
        breakdownRating.setRating_5(5);
        rating.setRating_1(1);
        rating.setRating_2(2);
        rating.setRating_3(3);
        rating.setRating_4(4);
        rating.setRating_5(5);
        rating.setWeightedSum(3.2);
        rating.setNumberOfReviews(8);
        rating.setProductId(UUID.randomUUID());
        reviewRequest.setRating(2);
        reviewRequest.setHeadline("product");
        reviewRequest.setReview("review");
        reviewRequest.setUserId(UUID.randomUUID());
        review.setUserId(UUID.randomUUID());
        review.setProductId(UUID.randomUUID());
        review.setReviewId(UUID.randomUUID());
        review.setReview("Good product");
        review.setRating(2);
        review.setHeadline("product details");
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review);
        reviewPage = new PageImpl<>(reviewList);
    }

    @Nested
    @DisplayName("Testing scenarios of get all reviews")
    class TestGetAllReview {
        @Test
        @DisplayName("Test get all reviews flow by productId in descending order with rating as null")
        void testGetAllReviewsForProductIdDescOrder() {
            Page<Review> page = Mockito.mock(Page.class);
            doReturn(reviewPage).when(reviewsRepository).getAllReviewsByProductId(any(), isA(Pageable.class));
            ReviewResponse reviewResponse = reviewsService.getAllReviews(UUID.randomUUID(), null, "createdAt", "desc", 0, 10);
            Assertions.assertEquals(1, reviewResponse.getReviews().size(), "get All reviews should return 1 review");
        }

        @Test
        @DisplayName("Test get all reviews flow by productId in ascending order with rating as null")
        void testGetAllReviewsForProductIdAscOrder() {
            Page<Review> page = Mockito.mock(Page.class);
            doReturn(reviewPage).when(reviewsRepository).getAllReviewsByProductId(any(), isA(Pageable.class));
            ReviewResponse reviewResponse = reviewsService.getAllReviews(UUID.randomUUID(), null, "createdAt", "asc", 0, 10);
            Assertions.assertEquals(1, reviewResponse.getReviews().size(), "get All reviews should return 1 review");
        }

        @Test
        @DisplayName("Test get all reviews flow by Ratings in ascending order with rating as not null")
        void testGetAllReviewsForRatingsAsc() {
            Page<Review> page = Mockito.mock(Page.class);
            doReturn(reviewPage).when(reviewsRepository).getReviewsByRatings(any(), anyInt(), isA(Pageable.class));
            ReviewResponse reviewResponse = reviewsService.getAllReviews(UUID.randomUUID(), 1, "createdAt", "asc", 0, 10);
            Assertions.assertEquals(1, reviewResponse.getReviews().size(), "get All reviews should return 1 review");
        }

        @Test
        @DisplayName("Test get all reviews flow by Ratings in descending order with rating as not null")
        void testGetAllReviewsForRatingDesc() {
            Page<Review> page = Mockito.mock(Page.class);
            doReturn(reviewPage).when(reviewsRepository).getReviewsByRatings(any(), anyInt(), isA(Pageable.class));
            ReviewResponse reviewResponse = reviewsService.getAllReviews(UUID.randomUUID(), 1, "createdAt", "desc", 0, 10);
            Assertions.assertEquals(1, reviewResponse.getReviews().size(), "get All reviews should return 1 review");
        }

        @Test
        @DisplayName("Test get all reviews flow by Ratings in descending order with rating as not null")
        void testGetAllReviewsByRatingsThrowingException() {
            Assertions.assertThrows(PersistenceException.class, ()->{
                doThrow(PessimisticLockException.class).when(reviewsRepository).getReviewsByRatings(any(), any(), isA(Pageable.class));
                reviewsService.getAllReviews(UUID.randomUUID(), 1, "createdAt", "desc", 0, 10);
            });
        }

        @Test
        @DisplayName("Test get all reviews flow by productId in descending order with rating as not null")
        void testGetAllReviewsByProductIdThrowingException() {
            Assertions.assertThrows(PersistenceException.class, ()->{
                doThrow(PessimisticLockException.class).when(reviewsRepository).getAllReviewsByProductId(any(), isA(Pageable.class));
                reviewsService.getAllReviews(UUID.randomUUID(), null, "createdAt", "desc", 0, 10);
            });
        }
    }

    @Nested
    @DisplayName("Testing scenarios of get one review")
    class TestGetOneReview {
        @Test
        @DisplayName("Test get one reviews flow by productId and reviewId")
        void testGetOneReview() {
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            Review review = reviewsService.getOneReview(UUID.randomUUID(), UUID.randomUUID());
            Assertions.assertEquals(Optional.of(2), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test get one reviews flow by productId and reviewId")
        void testGetOneReviewNotFoundException() {
            Assertions.assertThrows(NotFoundException.class, ()->{
                doThrow(NotFoundException.class).when(reviewsRepository).getReview(any(), any());
                reviewsService.getOneReview(UUID.randomUUID(), UUID.randomUUID());
            });
        }
    }

    @Nested
    @DisplayName("Testing scenarios of create review")
    class TestCreateReview {
        @Test
        @DisplayName("Test create review")
        void testCreateReview() throws Exception {
            doReturn(Optional.of(0)).when(reviewsRepository).checkReviewExists(any(), any());
            doReturn(review).when(reviewsRepository).save(any());
            doReturn(rating).when(ratingRepository).getRatingByProductId(any());

            Review review = reviewsService.createReview(UUID.randomUUID(), reviewRequest);
            Assertions.assertEquals(Optional.of(2), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test create review with rating count not null while updating")
        void testCreateReviewUpdateRatingFlow() throws Exception {
            doReturn(Optional.of(0)).when(reviewsRepository).checkReviewExists(any(), any());
            doReturn(rating).when(ratingRepository).getRatingByProductId(any());
            Review review = reviewsService.createReview(UUID.randomUUID(), reviewRequest);
            Assertions.assertEquals(Optional.of(2), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test create review with ReviewExistsException")
        void testCreateReviewNotFoundException() {
            Assertions.assertThrows(ReviewExistsException.class, ()->{
                doReturn(Optional.of(1)).when(reviewsRepository).checkReviewExists(any(), any());
                reviewsService.createReview(UUID.randomUUID(), reviewRequest);
            });
        }

        @Test
        @DisplayName("Test create review with PersistenceException")
        void testCreateReviewPersistenceException() {
            Assertions.assertThrows(PersistenceException.class, ()->{
                doReturn(Optional.of(0)).when(reviewsRepository).checkReviewExists(any(), any());
                doThrow(RuntimeException.class).when(reviewsRepository).save(any());
                reviewsService.createReview(UUID.randomUUID(), reviewRequest);
            });
        }
    }

    @Nested
    @DisplayName("Testing scenarios of update review")
    class TestUpdateReview {

        @Test
        @DisplayName("Test update review with rating 1")
        void testUpdateReviewWithRating1() throws Exception {
            review.setRating(1);
            reviewRequest.setRating(1);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(review).when(reviewsRepository).save(any());
            Review review = reviewsService.updateReview(UUID.randomUUID(), UUID.randomUUID(), reviewRequest);
            Assertions.assertEquals(Optional.of(1), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review with rating 3")
        void testUpdateReviewWithRating3() throws Exception {
            review.setRating(3);
            reviewRequest.setRating(3);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(review).when(reviewsRepository).save(any());
            Review review = reviewsService.updateReview(UUID.randomUUID(), UUID.randomUUID(), reviewRequest);
            Assertions.assertEquals(Optional.of(3), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review with rating 4")
        void testUpdateReviewWithRating4() throws Exception {
            review.setRating(4);
            reviewRequest.setRating(4);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(review).when(reviewsRepository).save(any());
            Review review = reviewsService.updateReview(UUID.randomUUID(), UUID.randomUUID(), reviewRequest);
            Assertions.assertEquals(Optional.of(4), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review with rating 5")
        void testUpdateReviewWithRating5() throws Exception {
            review.setRating(5);
            reviewRequest.setRating(5);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(review).when(reviewsRepository).save(any());
            Review review = reviewsService.updateReview(UUID.randomUUID(), UUID.randomUUID(), reviewRequest);
            Assertions.assertEquals(Optional.of(5), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review else part with no previous rating as 1")
        void testUpdateReviewWithRatingElse1() throws Exception {
            review.setRating(1);
            reviewRequest.setRating(1);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(null).when(ratingRepository).getRatingByProductId(any());
            doReturn(review).when(reviewsRepository).save(any());
            Review review = reviewsService.updateReview(UUID.randomUUID(), UUID.randomUUID(), reviewRequest);
            Assertions.assertEquals(Optional.of(1), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review else part with no previous rating as 5")
        void testUpdateReviewWithRatingElse5() throws Exception {
            review.setRating(5);
            reviewRequest.setRating(5);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(null).when(ratingRepository).getRatingByProductId(any());
            doReturn(review).when(reviewsRepository).save(any());
            Review review = reviewsService.updateReview(UUID.randomUUID(), UUID.randomUUID(), reviewRequest);
            Assertions.assertEquals(Optional.of(5), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review else part with no previous rating as 2")
        void testUpdateReviewWithRatingElse2() throws Exception {
            review.setRating(2);
            reviewRequest.setRating(2);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(null).when(ratingRepository).getRatingByProductId(any());
            doReturn(review).when(reviewsRepository).save(any());
            Review review = reviewsService.updateReview(UUID.randomUUID(), UUID.randomUUID(), reviewRequest);
            Assertions.assertEquals(Optional.of(2), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review else part with no previous rating as 3")
        void testUpdateReviewWithRatingElse3() throws Exception {
            review.setRating(3);
            reviewRequest.setRating(3);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(null).when(ratingRepository).getRatingByProductId(any());
            doReturn(review).when(reviewsRepository).save(any());
            Review review = reviewsService.updateReview(UUID.randomUUID(), UUID.randomUUID(), reviewRequest);
            Assertions.assertEquals(Optional.of(3), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review else part with no previous rating as 5")
        void testUpdateReviewWithRatingElse4() throws Exception {
            review.setRating(4);
            reviewRequest.setRating(4);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(null).when(ratingRepository).getRatingByProductId(any());
            doReturn(review).when(reviewsRepository).save(any());
            Review review = reviewsService.updateReview(UUID.randomUUID(), UUID.randomUUID(), reviewRequest);
            Assertions.assertEquals(Optional.of(4), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review Not found exception")
        void testUpdateReviewWithNotFoundException() throws Exception {
            Assertions.assertThrows(NotFoundException.class, ()->{
                doThrow(NotFoundException.class).when(reviewsRepository).getReview(any(), any());
                reviewsService.updateReview(UUID.randomUUID(), UUID.randomUUID(), reviewRequest);
            });
        }

        @Test
        @DisplayName("Test update review with PersistenceException")
        void testUpdateReviewPersistenceException() {
            Assertions.assertThrows(PersistenceException.class, ()->{
                doReturn(review).when(reviewsRepository).getReview(any(), any());
                doThrow(RuntimeException.class).when(reviewsRepository).save(any());
                reviewsService.updateReview(UUID.randomUUID(), UUID.randomUUID(), reviewRequest);
            });
        }
    }

    @Nested
    @DisplayName("Testing scenarios of delete review")
    class TestDeleteReview {

        @Test
        @DisplayName("Test delete review with rating 1")
        void testDeleteReviewWithRating1() throws Exception {
            review.setRating(1);
            reviewRequest.setRating(1);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(rating).when(ratingRepository).getRatingByProductId(any());
            doNothing().when(ratingRepositoryImpl).deleteRatingByProductId(any(), any(), any());
            doReturn(review).when(reviewsRepository).save(any());

            reviewsService.deleteReview(UUID.randomUUID(), UUID.randomUUID());
            Assertions.assertEquals(Optional.of(1), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test delete review with rating 3")
        void testDeleteReviewWithRating3() throws Exception {
            review.setRating(3);
            reviewRequest.setRating(3);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(rating).when(ratingRepository).getRatingByProductId(any());
            doNothing().when(ratingRepositoryImpl).deleteRatingByProductId(any(), any(), any());
            doReturn(review).when(reviewsRepository).save(any());

            reviewsService.deleteReview(UUID.randomUUID(), UUID.randomUUID());
            Assertions.assertEquals(Optional.of(3), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test delete review with rating 4")
        void testDeleteReviewWithRating4() throws Exception {
            review.setRating(4);
            reviewRequest.setRating(4);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(rating).when(ratingRepository).getRatingByProductId(any());
            doNothing().when(ratingRepositoryImpl).deleteRatingByProductId(any(), any(), any());
            doReturn(review).when(reviewsRepository).save(any());

            reviewsService.deleteReview(UUID.randomUUID(), UUID.randomUUID());
            Assertions.assertEquals(Optional.of(4), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review with rating 5")
        void testDeleteReviewWithRating5() throws Exception {
            review.setRating(5);
            reviewRequest.setRating(5);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(rating).when(ratingRepository).getRatingByProductId(any());
            doNothing().when(ratingRepositoryImpl).deleteRatingByProductId(any(), any(), any());
            doReturn(review).when(reviewsRepository).save(any());

            reviewsService.deleteReview(UUID.randomUUID(), UUID.randomUUID());
            Assertions.assertEquals(Optional.of(5), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review no previous rating 1")
        void testDeleteReviewWithRatingElseRating1() throws Exception {
            review.setRating(1);
            reviewRequest.setRating(1);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(null).when(ratingRepository).getRatingByProductId(any());
            doNothing().when(ratingRepositoryImpl).deleteRatingByProductId(any(), any(), any());
            doReturn(review).when(reviewsRepository).save(any());

            reviewsService.deleteReview(UUID.randomUUID(), UUID.randomUUID());
            Assertions.assertEquals(Optional.of(1), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review no previous rating 2")
        void testDeleteReviewWithRatingElseRating2() throws Exception {
            BindingResult bindingResult = mock(BindingResult.class);
            review.setRating(2);
            reviewRequest.setRating(2);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(null).when(ratingRepository).getRatingByProductId(any());
            doNothing().when(ratingRepositoryImpl).deleteRatingByProductId(any(), any(), any());
            doReturn(review).when(reviewsRepository).save(any());

            reviewsService.deleteReview(UUID.randomUUID(), UUID.randomUUID());
            Assertions.assertEquals(Optional.of(2), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review no previous rating 3")
        void testDeleteReviewWithRatingElseRating3() throws Exception {
            BindingResult bindingResult = mock(BindingResult.class);
            review.setRating(3);
            reviewRequest.setRating(3);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(null).when(ratingRepository).getRatingByProductId(any());
            doNothing().when(ratingRepositoryImpl).deleteRatingByProductId(any(), any(), any());
            doReturn(review).when(reviewsRepository).save(any());

            reviewsService.deleteReview(UUID.randomUUID(), UUID.randomUUID());
            Assertions.assertEquals(Optional.of(3), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review no previous rating 4")
        void testDeleteReviewWithRatingElseRating4() throws Exception {
            BindingResult bindingResult = mock(BindingResult.class);
            review.setRating(4);
            reviewRequest.setRating(4);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(null).when(ratingRepository).getRatingByProductId(any());
            doNothing().when(ratingRepositoryImpl).deleteRatingByProductId(any(), any(), any());
            doReturn(review).when(reviewsRepository).save(any());

            reviewsService.deleteReview(UUID.randomUUID(), UUID.randomUUID());
            Assertions.assertEquals(Optional.of(4), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test update review no previous rating 5")
        void testDeleteReviewWithRatingElseRating5() throws Exception {
            BindingResult bindingResult = mock(BindingResult.class);
            review.setRating(5);
            reviewRequest.setRating(5);
            doReturn(review).when(reviewsRepository).getReview(any(), any());
            doReturn(null).when(ratingRepository).getRatingByProductId(any());
            doNothing().when(ratingRepositoryImpl).deleteRatingByProductId(any(), any(), any());
            doReturn(review).when(reviewsRepository).save(any());

            reviewsService.deleteReview(UUID.randomUUID(), UUID.randomUUID());
            Assertions.assertEquals(Optional.of(5), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test delete review Not found exception")
        void testDeleteReviewWithNotFoundException() throws Exception {
            BindingResult bindingResult = mock(BindingResult.class);
            Assertions.assertThrows(NotFoundException.class, ()->{
                doThrow(NotFoundException.class).when(reviewsRepository).getReview(any(), any());
                reviewsService.deleteReview(UUID.randomUUID(), UUID.randomUUID());
            });
        }

        @Test
        @DisplayName("Test update review with PersistenceException")
        void testDeleteReviewPersistenceException() {
            BindingResult bindingResult = mock(BindingResult.class);
            Assertions.assertThrows(PersistenceException.class, ()->{
                doReturn(review).when(reviewsRepository).getReview(any(), any());
                doThrow(RuntimeException.class).when(reviewsRepository).save(any());
                reviewsService.deleteReview(UUID.randomUUID(), UUID.randomUUID());
            });
        }
    }

    @Nested
    @DisplayName("Testing scenarios of is review helpful")
    class TestIsReviewHelpful {

        @Test
        @DisplayName("Test is review helpful")
        void testIsHelpFull() throws Exception {
            HelpfulReview helpfulReview = new HelpfulReview();
            doReturn(0).when(helpfulReviewsRepository).hasUserMarkedHelpFull(any(), any(), any());
            doReturn(helpfulReview).when(helpfulReviewsRepository).save(any());
            doNothing().when(reviewsRepository).updateHelpfulInReviews(any(), any());

            reviewsService.isReviewHelpFull(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            Assertions.assertEquals(Optional.of(2), Optional.ofNullable(review.getRating()), "get one review should return 1 review");
        }

        @Test
        @DisplayName("Test is helpFull ReviewExistsException")
        void testIsHelpFullReviewExistsException() {
            Assertions.assertThrows(ReviewExistsException.class, ()->{
                doReturn(1).when(helpfulReviewsRepository).hasUserMarkedHelpFull(any(), any(), any());
                reviewsService.isReviewHelpFull(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            });
        }

        @Test
        @DisplayName("Test is helpFull PersistenceException")
        void testIsHelpFullPersistenceException() {
            Assertions.assertThrows(PersistenceException.class, ()->{
                doReturn(0).when(helpfulReviewsRepository).hasUserMarkedHelpFull(any(), any(), any());
                doThrow(RuntimeException.class).when(helpfulReviewsRepository).save(any());
                reviewsService.isReviewHelpFull(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            });
        }
    }

    @Nested
    @DisplayName("Testing scenarios of is aggregated Reviews By Rating")
    class TestAggregatedReviewsByRating {

        @Test
        @DisplayName("Test aggregated Reviews By Rating")
        void testGetAggregatedReviewsByRating() throws Exception {
            doReturn(rating).when(ratingRepository).getAggregatedReviewsByProductId(any());
            AggregatedReviewsResponse aggregatedReviewsResponse = reviewsService.getAggregatedReviewsByRating(UUID.randomUUID());
            Assertions.assertEquals(Optional.of(3.2), Optional.ofNullable(aggregatedReviewsResponse.getRating()), "get aggregate review");
        }
    }

    @Nested
    @DisplayName("Testing scenarios of is breakdown Reviews By Rating")
    class TestBreakdownReviewsByRating {

        @Test
        @DisplayName("Test breakdown Reviews By Rating")
        void testGetBreakdownReviewsByRating() throws Exception {
            doReturn(rating).when(ratingRepository).getRatingByProductId(any());
            BreakdownRating breakdownRating = reviewsService.getBreakDownReviewsByRating(UUID.randomUUID());
            Assertions.assertEquals(Optional.of(1), Optional.ofNullable(breakdownRating.getRating_1()), "get aggregate review");
        }

        @Test
        @DisplayName("Test breakdown Reviews By Rating NotFoundException")
        void testGetBreakdownReviewsByRatingNotFoundException() {
            Assertions.assertThrows(NotFoundException.class, ()->{
                doThrow(NotFoundException.class).when(ratingRepository).getRatingByProductId(any());
                reviewsService.getBreakDownReviewsByRating(UUID.randomUUID());
            });
        }
    }
}
