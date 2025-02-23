package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class VideoCastMemberID implements Serializable {

    @Column(name = "video_id", nullable = false)
    private UUID videoId;

    @Column(name = "cast_member_id", nullable = false)
    private UUID castMemberId;

    public VideoCastMemberID() {
    }

    private VideoCastMemberID(final UUID videoId, final UUID castMemberId) {
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

    public static VideoCastMemberID from(final UUID videoId, final UUID castMemberId) {
        return new VideoCastMemberID(videoId, castMemberId);
    }

    public UUID getVideoId() {
        return videoId;
    }

    public VideoCastMemberID setVideoId(UUID videoId) {
        this.videoId = videoId;
        return this;
    }

    public UUID getCastMemberId() {
        return castMemberId;
    }

    public VideoCastMemberID setCastMemberId(UUID castMemberId) {
        this.castMemberId = castMemberId;
        return this;
    }
}