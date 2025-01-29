package com.fullcycle.admin.catalogo.application.video.delete;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class DeleteVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideo_shouldDeleteIt() {
        // given
        final var expectedVideoId = VideoID.unique();

        doNothing()
                .when(videoGateway).deleteById(any());

        // when
        assertDoesNotThrow(() -> this.useCase.execute(expectedVideoId.getValue()));

        // then
        verify(videoGateway).deleteById(expectedVideoId);
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteVideo_shouldBeOk() {
        // given
        final var expectedVideoId = VideoID.from("123");

        doNothing()
                .when(videoGateway).deleteById(any());

        // when
        assertDoesNotThrow(() -> this.useCase.execute(expectedVideoId.getValue()));

        // then
        verify(videoGateway).deleteById(expectedVideoId);
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideoAndGatewayThrowsException_shouldReceiveException() {
        // given
        final var expectedVideoId = VideoID.from("123");

        doThrow(InternalErrorException.with("Error on delete video", new RuntimeException()))
                .when(videoGateway).deleteById(any());

        // when
        assertThrows(
                InternalErrorException.class,
                () -> this.useCase.execute(expectedVideoId.getValue())
        );

        // then
        verify(videoGateway).deleteById(expectedVideoId);
    }
}