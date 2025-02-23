package com.fullcycle.admin.catalogo.application.video.create;

import com.fullcycle.admin.catalogo.application.Fixture;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.MediaStatus;
import com.fullcycle.admin.catalogo.domain.video.Resource;
import com.fullcycle.admin.catalogo.domain.video.Resource.Type;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, castMemberGateway, categoryGateway, genreGateway, mediaResourceGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideo_shouldReturnVideoId() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.wesley().getId(),
                Fixture.CastMembers.wellington().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        mockImageMedia();
        mockAudioVideoMedia();

        when(videoGateway.create(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualResult = this.useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                        Objects.equals(expectedTitle, actualVideo.getTitle())
                                && Objects.equals(expectedDescription, actualVideo.getDescription())
                                && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                                && Objects.equals(expectedDuration, actualVideo.getDuration())
                                && Objects.equals(expectedOpened, actualVideo.getOpened())
                                && Objects.equals(expectedPublished, actualVideo.getPublished())
                                && Objects.equals(expectedRating, actualVideo.getRating())
                                && Objects.equals(expectedCategories, actualVideo.getCategories())
                                && Objects.equals(expectedGenres, actualVideo.getGenres())
                                && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                                && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
//                                && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
//                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
//                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
//                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
        ));
    }

    @Test
    public void givenAValidCommandWithoutCategories_whenCallsCreateVideo_shouldReturnVideoId() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.wesley().getId(),
                Fixture.CastMembers.wellington().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        mockImageMedia();
        mockAudioVideoMedia();

        when(videoGateway.create(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualResult = this.useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                        Objects.equals(expectedTitle, actualVideo.getTitle())
                                && Objects.equals(expectedDescription, actualVideo.getDescription())
                                && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                                && Objects.equals(expectedDuration, actualVideo.getDuration())
                                && Objects.equals(expectedOpened, actualVideo.getOpened())
                                && Objects.equals(expectedPublished, actualVideo.getPublished())
                                && Objects.equals(expectedRating, actualVideo.getRating())
                                && Objects.equals(expectedCategories, actualVideo.getCategories())
                                && Objects.equals(expectedGenres, actualVideo.getGenres())
                                && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                                && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
//                                && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
//                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
//                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
//                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
        ));
    }

    @Test
    public void givenAValidCommandWithoutGenres_whenCallsCreateVideo_shouldReturnVideoId() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.of(
                Fixture.CastMembers.wesley().getId(),
                Fixture.CastMembers.wellington().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        mockImageMedia();
        mockAudioVideoMedia();

        when(videoGateway.create(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualResult = this.useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                        Objects.equals(expectedTitle, actualVideo.getTitle())
                                && Objects.equals(expectedDescription, actualVideo.getDescription())
                                && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                                && Objects.equals(expectedDuration, actualVideo.getDuration())
                                && Objects.equals(expectedOpened, actualVideo.getOpened())
                                && Objects.equals(expectedPublished, actualVideo.getPublished())
                                && Objects.equals(expectedRating, actualVideo.getRating())
                                && Objects.equals(expectedCategories, actualVideo.getCategories())
                                && Objects.equals(expectedGenres, actualVideo.getGenres())
                                && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                                && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
//                                && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
//                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
//                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
//                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
        ));
    }

    @Test
    public void givenAValidCommandWithoutCastMembers_whenCallsCreateVideo_shouldReturnVideoId() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = Fixture.Videos.resource(Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        when(videoGateway.create(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualResult = this.useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                        Objects.equals(expectedTitle, actualVideo.getTitle())
                                && Objects.equals(expectedDescription, actualVideo.getDescription())
                                && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                                && Objects.equals(expectedDuration, actualVideo.getDuration())
                                && Objects.equals(expectedOpened, actualVideo.getOpened())
                                && Objects.equals(expectedPublished, actualVideo.getPublished())
                                && Objects.equals(expectedRating, actualVideo.getRating())
                                && Objects.equals(expectedCategories, actualVideo.getCategories())
                                && Objects.equals(expectedGenres, actualVideo.getGenres())
                                && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                                && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
//                                && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
//                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
//                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
//                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
        ));
    }

    @Test
    public void givenAValidCommandWhithoutResources_whenCallsCreateVideo_shouldReturnVideoId() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.wesley().getId(),
                Fixture.CastMembers.wellington().getId()
        );
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        when(videoGateway.create(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualResult = this.useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    public void givenANullTitle_whenCallsCreateVideo_shouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'title' should not be null";
        final var expectedErrorCount = 1;

        final String expectedTitle = null;
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        // when
        final var actualException = assertThrows(NotificationException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenAEmptyTitle_whenCallsCreateVideo_shouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        // when
        final var actualException = assertThrows(NotificationException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenANullRating_whenCallsCreateVideo_shouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = null;
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        // when
        final var actualException = assertThrows(NotificationException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidRating_whenCallsCreateVideo_shouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = "AJAJA";
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        // when
        final var actualException = assertThrows(NotificationException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenANullLaunchYear_whenCallsCreateVideo_shouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'launchedAt' should not be null";
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final Integer expectedLaunchYear = null;
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        // when
        final var actualException = assertThrows(NotificationException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        // given
        final var techId = Fixture.Categories.aulas().getId();

        final var expectedErrorMessage = "Some categories could not be found: %s".formatted(techId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(techId);
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.wesley().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));


        // when
        final var actualException = assertThrows(NotificationException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoAndSomeGenresDoesNotExists_shouldReturnDomainException() {
        // given
        final var aulasId = Fixture.Genres.tech().getId();

        final var expectedErrorMessage = "Some genres could not be found: %s".formatted(aulasId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(aulasId);
        final var expectedMembers = Set.of(Fixture.CastMembers.wesley().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));


        // when
        final var actualException = assertThrows(NotificationException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoAndSomeCastMembersDoesNotExists_shouldReturnDomainException() {
        // given
        final var wesleyId = Fixture.CastMembers.wesley().getId();

        final var expectedErrorMessage = "Some cast members could not be found: %s".formatted(wesleyId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(wesleyId);
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());


        // when
        final var actualException = assertThrows(NotificationException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoThrowsException_shouldCallClearResources() {
        // given
        final var expectedErrorMessage = "An error on create video was observed";

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.wesley().getId(),
                Fixture.CastMembers.wellington().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedBanner,
                expectedTrailer,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        mockImageMedia();
        mockAudioVideoMedia();

        when(videoGateway.create(any()))
                .thenThrow(new RuntimeException("Internal Server Error"));

        // when
        final var actualResult = assertThrows(InternalErrorException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        assertNotNull(actualResult);
        assertTrue(actualResult.getMessage().startsWith(expectedErrorMessage));

        verify(mediaResourceGateway).cleanResources(any());
    }

    private void mockImageMedia() {
        when(mediaResourceGateway.storeImage(any(), any()))
                .thenAnswer(t -> {
                    final var resource = t.getArgument(1, Resource.class);
                    return ImageMedia.with(IdUtils.uuid(), resource.name(), "/img");
                });
    }

    private void mockAudioVideoMedia() {
        when(mediaResourceGateway.storeAudioVideo(any(), any()))
                .thenAnswer(t -> {
                    final var resource = t.getArgument(1, Resource.class);
                    return AudioVideoMedia.with(IdUtils.uuid(), resource.name(), "/img", "", MediaStatus.PENDING);
                });
    }
}