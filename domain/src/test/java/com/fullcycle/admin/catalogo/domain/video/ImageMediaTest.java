package com.fullcycle.admin.catalogo.domain.video;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ImageMediaTest {

    @Test
    public void givenValidParams_whenCallsNewImage_shouldReturnInstance() {
        // given
        final var expectedCheckSum = "abc";
        final var expectedName = "Banner.png";
        final var expectedLocation = "/image/ac";

        // when
        final var actualImage = ImageMedia.with(expectedCheckSum, expectedName, expectedLocation);

        // then
        assertNotNull(actualImage);
        assertEquals(expectedCheckSum, actualImage.checksum());
        assertEquals(expectedName, actualImage.name());
        assertEquals(expectedLocation, actualImage.location());
    }

    @Test
    public void givenTwoMedias_whenCallsEquals_shouldReturnTrue() {
        // given
        final var expectedCheckSum = "abc";
        final var expectedLocation = "/image/ac";

        // when
        final var img1 = ImageMedia.with(expectedCheckSum, "Random", expectedLocation);
        final var img2 = ImageMedia.with(expectedCheckSum, "Simple", expectedLocation);

        // then
        assertEquals(img1, img2);
        assertNotSame(img1, img2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shouldReturError() {
        // given when then
        assertThrows(NullPointerException.class, () -> ImageMedia.with(null, "Random", "/images"));
        assertThrows(NullPointerException.class, () -> ImageMedia.with("abc", null, "/images"));
        assertThrows(NullPointerException.class, () -> ImageMedia.with("abc", "Random", null));
    }
}