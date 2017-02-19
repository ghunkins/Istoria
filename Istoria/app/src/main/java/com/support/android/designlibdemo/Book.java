package com.support.android.designlibdemo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book {

    private String id;
    private String books_count;
    private String ratings_count;
    private String text_reviews_count;
    private String original_publication_year;
    private String original_publication_month;
    private String original_publication_day;
    private String average_rating;
    private String title;
    private String image_url;
    private String small_image_url;
    private String author_id;
    private String author_name;
    private List<List<String>> reviews = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBooksCount() {
        return books_count;
    }

    public void setBooksCount(String booksCount) {
        this.books_count = booksCount;
    }

    public String getRatingsCount() {
        return ratings_count;
    }

    public void setRatingsCount(String ratingsCount) {
        this.ratings_count = ratingsCount;
    }

    public String getTextReviewsCount() {
        return text_reviews_count;
    }

    public void setTextReviewsCount(String textReviewsCount) {
        this.text_reviews_count = textReviewsCount;
    }

    public String getOriginalPublicationYear() {
        return original_publication_year;
    }

    public void setOriginalPublicationYear(String originalPublicationYear) {
        this.original_publication_year = originalPublicationYear;
    }

    public String getOriginalPublicationMonth() {
        return original_publication_month;
    }

    public void setOriginalPublicationMonth(String originalPublicationMonth) {
        this.original_publication_month = originalPublicationMonth;
    }

    public String getOriginalPublicationDay() {
        return original_publication_day;
    }

    public void setOriginalPublicationDay(String originalPublicationDay) {
        this.original_publication_day = originalPublicationDay;
    }

    public String getAverageRating() {
        return average_rating;
    }

    public void setAverageRating(String averageRating) {
        this.average_rating = averageRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String imageUrl) {
        this.image_url = imageUrl;
    }

    public String getSmallImageUrl() {
        return small_image_url;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.small_image_url = smallImageUrl;
    }

    public String getAuthorId() {
        return author_id;
    }

    public void setAuthorId(String authorId) {
        this.author_id = authorId;
    }

    public String getAuthorName() {
        return author_name;
    }

    public void setAuthorName(String authorName) {
        this.author_name = authorName;
    }

    public List<List<String>> getReviews() {
        return reviews;
    }

    public void setReviews(List<List<String>> reviews) {
        this.reviews = reviews;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}