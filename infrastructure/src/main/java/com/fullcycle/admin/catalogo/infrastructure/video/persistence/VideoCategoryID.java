package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoCategoryID implements Serializable {

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "category_id", nullable = false)
    private String categoryId;

    public VideoCategoryID() {
    }

    private VideoCategoryID(final String videoId, final String categoryId) {
        this.videoId = videoId;
        this.categoryId = categoryId;
    }

    public static VideoCategoryID from(final String videoId, final String categoryId) {
        return new VideoCategoryID(videoId, categoryId);
    }

    public String getVideoId() {
        return videoId;
    }

    public VideoCategoryID setVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public VideoCategoryID setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VideoCategoryID that = (VideoCategoryID) o;
        return Objects.equals(videoId, that.videoId) && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, categoryId);
    }
}