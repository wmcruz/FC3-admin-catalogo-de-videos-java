package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AudioVideoMediaTest {

    @Test
    public void givenValidParams_whenCallsNewAudioVideo_shouldReturnInstance() {
        // given
        final var expectedId = IdUtils.uuid();
        final var expectedCheckSum = "abc";
        final var expectedName = "Banner.png";
        final var expectedRawLocation = "/image/ac";
        final var expectedEncodedLocation = "/image/ac-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        // when
        final var actualVideo = AudioVideoMedia.with(expectedId, expectedCheckSum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedStatus);

        // then
        assertNotNull(actualVideo);
        assertEquals(expectedId, actualVideo.id());
        assertEquals(expectedCheckSum, actualVideo.checksum());
        assertEquals(expectedName, actualVideo.name());
        assertEquals(expectedRawLocation, actualVideo.rawLocation());
        assertEquals(expectedEncodedLocation, actualVideo.encodedLocation());
        assertEquals(expectedStatus, actualVideo.status());
    }

    @Test
    public void givenTwoVideos_whenCallsEquals_shouldReturnTrue() {
        // given
        final var expectedCheckSum = "abc";
        final var expectedRawLocation = "/image/ac";

        // when
        final var video1 = AudioVideoMedia.with(expectedCheckSum, "Random", expectedRawLocation);
        final var video2 = AudioVideoMedia.with(expectedCheckSum, "Simple", expectedRawLocation);

        // then
        assertEquals(video1, video2);
        assertNotSame(video1, video2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shouldReturError() {
        // given when then
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with(null, null, "Random", "/videos", "/videos", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("id", "abc", null, "/videos", "/videos", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("id", "abc", "Random", null, "/videos", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("id", "abc", "Random", "/videos", null, MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("id", "abc", "Random", "/videos", "/videos", null));
    }
}