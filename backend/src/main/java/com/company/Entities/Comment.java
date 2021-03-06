package com.company.Entities;

public class Comment {
    Integer commentId, relatesToVideoId, likes, dislikes, responseToCommentId;
    String postedByUsername, content;
    Long timeOfPosting;

    public Comment(Integer commentId, Integer relatesToVideoId, String postedByUsername, String content, Integer likes, Integer dislikes, Integer responseToCommentId, Long timeOfPosting) {
        this.commentId = commentId;
        this.relatesToVideoId = relatesToVideoId;
        this.postedByUsername = postedByUsername;
        this.content = content;
        this.likes = likes;
        this.dislikes = dislikes;
        this.responseToCommentId = responseToCommentId;
        this.timeOfPosting = timeOfPosting;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getRelatesToVideoId() {
        return relatesToVideoId;
    }

    public void setRelatesToVideoId(Integer relatesToVideoId) {
        this.relatesToVideoId = relatesToVideoId;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public Integer getResponseToCommentId() {
        return responseToCommentId;
    }

    public void setResponseToCommentId(Integer responseToCommentId) {
        this.responseToCommentId = responseToCommentId;
    }

    public String getPostedByUsername() {
        return postedByUsername;
    }

    public void setPostedByUsername(String postedByUsername) {
        this.postedByUsername = postedByUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimeOfPosting() {
        return timeOfPosting;
    }

    public void setTimeOfPosting(Long timeOfPosting) {
        this.timeOfPosting = timeOfPosting;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", relatesToVideoId=" + relatesToVideoId +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", responseToCommentId=" + responseToCommentId +
                ", postedByUsername='" + postedByUsername + '\'' +
                ", content='" + content + '\'' +
                ", timeOfPosting=" + timeOfPosting +
                '}';
    }
}
