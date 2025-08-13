package com.furkan.project.ai.dto;

public class AiCommentResponse {
    private Long movieId;
    private String username;
    private String comment;

    public AiCommentResponse() {}

    public AiCommentResponse(Long movieId, String username, String comment) {
        this.movieId = movieId;
        this.username = username;
        this.comment = comment;
    }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
