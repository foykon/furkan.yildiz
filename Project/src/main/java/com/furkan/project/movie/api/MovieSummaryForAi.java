package com.furkan.project.movie.api;

import java.math.BigDecimal;
import java.util.List;

public class MovieSummaryForAi {
    private Long id;
    private String title;
    private String releaseDate;
    private List<String> genres;
    private String directorName;
    private BigDecimal rating;
    private String description;

    public MovieSummaryForAi() {}

    public MovieSummaryForAi(Long id, String title, String releaseDate, List<String> genres,
                        String directorName, BigDecimal rating, String description) {
        this.id = id; this.title = title; this.releaseDate = releaseDate;
        this.genres = genres; this.directorName = directorName;
        this.rating = rating; this.description = description;
    }

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public String getReleaseDate() { return releaseDate; } public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public List<String> getGenres() { return genres; } public void setGenres(List<String> genres) { this.genres = genres; }
    public String getDirectorName() { return directorName; } public void setDirectorName(String directorName) { this.directorName = directorName; }
    public BigDecimal getRating() { return rating; } public void setRating(BigDecimal rating) { this.rating = rating; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
}

