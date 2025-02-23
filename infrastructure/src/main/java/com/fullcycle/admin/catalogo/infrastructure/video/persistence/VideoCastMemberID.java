package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoCastMemberID implements Serializable {

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "cast_member_id", nullable = false)
    private String castMemberId;

    public VideoCastMemberID() {
    }

    private VideoCastMemberID(final String videoId, final String castMemberId) {
        this.videoId = videoId;
        this.castMemberId = castMemberId;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        final VideoCastMemberID that = (VideoCastMemberID) object;
        return Objects.equals(videoId, that.videoId) && Objects.equals(castMemberId, that.castMemberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, castMemberId);
    }

    public static VideoCastMemberID from(final String videoId, final String castMemberId) {
        return new VideoCastMemberID(videoId, castMemberId);
    }

    public String getVideoId() {
        return videoId;
    }

    public VideoCastMemberID setVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }

    public String getCastMemberId() {
        return castMemberId;
    }

    public VideoCastMemberID setCastMemberId(String castMemberId) {
        this.castMemberId = castMemberId;
        return this;
    }
}