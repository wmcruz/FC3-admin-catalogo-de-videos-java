package com.fullcycle.admin.catalogo.application.video.media.get;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class GetMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetMediaUseCase useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway);
    }

    @Test
    public void givenVideoIdAndType_whenIsValidCmd_shouldReturnResource() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.of(expectedResource));

        final var aCmd = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        // when
        final var actualResult = this.useCase.execute(aCmd);

        // then
        assertEquals(expectedResource.name(), actualResult.name());
        assertEquals(expectedResource.content(), actualResult.content());
        assertEquals(expectedResource.contentType(), actualResult.contentType());
    }

    @Test
    public void givenVideoIdAndType_whenIsNotFound_shouldReturnNotFoundException() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();

        when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.empty());

        final var aCmd = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        // when
        assertThrows(NotFoundException.class, () -> {
            this.useCase.execute(aCmd);
        });
    }

    @Test
    public void givenVideoIdAndType_whenTypeDoesntExists_shouldReturnNotFoundException() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Media type QUALQUER doesn't exists";

        final var aCmd = GetMediaCommand.with(expectedId.getValue(), "QUALQUER");

        // when
        final var actualException = assertThrows(NotFoundException.class, () -> {
            this.useCase.execute(aCmd);
        });

        // then
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
