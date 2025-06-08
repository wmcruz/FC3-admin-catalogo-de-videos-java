package com.fullcycle.admin.catalogo.infrastructure.video;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoID;
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
public class DefaultVideoGatewayTest {

    @Autowired
    private DefaultVideoGateway videoGateway;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private VideoRepository videoRepository;

    private CastMember wesley;
    private CastMember wellington;

    private Category aulas;
    private Category lives;

    private Genre tech;
    private Genre business;

    @BeforeEach
    public void setUp() {
        wesley = castMemberGateway.create(Fixture.CastMembers.wesley());
        wellington = castMemberGateway.create(Fixture.CastMembers.wellington());

        aulas = categoryGateway.create(Fixture.Categories.aulas());
        lives = categoryGateway.create(Fixture.Categories.lives());

        tech = genreGateway.create(Fixture.Genres.tech());
        business = genreGateway.create(Fixture.Genres.business());
    }

    @Test
    public void testInjection() {
        assertNotNull(videoGateway);
        assertNotNull(castMemberGateway);
        assertNotNull(categoryGateway);
        assertNotNull(genreGateway);
        assertNotNull(videoRepository);
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsCreate_shouldPersistIt() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(wesley.getId());

        final var expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final var expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final var expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final var expectedThumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final var expectedThumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers
                )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);

        // when
        final var actualVideo = videoGateway.create(aVideo);

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());

        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
        assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());

        final var persistedVideo = videoRepository.findById(actualVideo.getId().getValue()).get();

        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenresID());
        assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
        assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
    }

    @Test
    @Transactional
    public void givenAValidVideoWithoudRelations_whenCallsCreate_shouldPersistIt() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // when
        final var actualVideo = videoGateway.create(aVideo);

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());

        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        final var persistedVideo = videoRepository.findById(actualVideo.getId().getValue()).get();

        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenresID());
        assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        assertNull(persistedVideo.getVideo());
        assertNull(persistedVideo.getTrailer());
        assertNull(persistedVideo.getBanner());
        assertNull(persistedVideo.getThumbnail());
        assertNull(persistedVideo.getThumbnailHalf());
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsUpdate_shouldPersistIt() {
        // given
        final var aVideo = videoGateway.create(Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId());

        final var expectedMembers = Set.of(wesley.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final var expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final var expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final var expectedThumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final var expectedThumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

        final var updatedVideo = Video.with(aVideo)
                .update(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers
                )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);

        // when
        final var actualVideo = videoGateway.update(updatedVideo);

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());

        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
        assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());
        assertNotNull(actualVideo.getCreatedAt());
        assertTrue(actualVideo.getUpdatedAt().isAfter(aVideo.getUpdatedAt()));

        final var persistedVideo = videoRepository.findById(actualVideo.getId().getValue()).get();

        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenresID());
        assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
        assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
        assertNotNull(persistedVideo.getCreatedAt());
        assertTrue(persistedVideo.getUpdatedAt().isAfter(aVideo.getUpdatedAt()));
    }

    @Test
    public void givenAValidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var aVideo = videoGateway.create(Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        assertEquals(1, videoRepository.count());

        final var anId = aVideo.getId();

        // when
        videoGateway.deleteById(anId);

        // then
        assertEquals(0, videoRepository.count());
    }

    @Test
    public void givenAnInvalidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        videoGateway.create(Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        assertEquals(1, videoRepository.count());

        final var anId = VideoID.unique();

        // when
        videoGateway.deleteById(anId);

        // then
        assertEquals(1, videoRepository.count());
    }

    @Test
    public void givenAValidVideo_whenCallsFindById_shouldReturnIt() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(wesley.getId());

        final var expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final var expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final var expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final var expectedThumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final var expectedThumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

        final var aVideo = videoGateway.create(Video.newVideo(
                                expectedTitle,
                                expectedDescription,
                                expectedLaunchYear,
                                expectedDuration,
                                expectedOpened,
                                expectedPublished,
                                expectedRating,
                                expectedCategories,
                                expectedGenres,
                                expectedMembers
                        )
                        .setVideo(expectedVideo)
                        .setTrailer(expectedTrailer)
                        .setBanner(expectedBanner)
                        .setThumbnail(expectedThumb)
                        .setThumbnailHalf(expectedThumbHalf)
        );

        // when
        final var actualVideo = videoGateway.findById(aVideo.getId()).get();

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());

        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
        assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());
    }

    @Test
    public void givenAnInvalidVideoId_whenCallsFindById_shouldEmpty() {
        // given
        videoGateway.create(Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        final var anId = VideoID.unique();

        // when
        final var actualVideo = videoGateway.findById(anId);

        // then
        assertFalse(actualVideo.isPresent());
    }

    @Test
    public void givenEmptyParams_whenCallFindAll_shouldReturnAllList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 4;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @Test
    public void givenEmptyVideos_whenCallFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @Test
    public void givenAValidCategory_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(aulas.getId()),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());

        assertEquals("21.1 Implementação dos testes integrados do findAll", actualPage.items().get(0).title());
        assertEquals("Aula de empreendedorismo", actualPage.items().get(1).title());
    }

    @Test
    public void givenAValidCastMember_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(wesley.getId()),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());

        assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
        assertEquals("System Design no Mercado Livre na prática", actualPage.items().get(1).title());
    }

    @Test
    public void givenAValidGenre_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of(business.getId())
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());

        assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
    }

    @Test
    public void givenAllParameters_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "empreendedorismo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(wesley.getId()),
                Set.of(aulas.getId()),
                Set.of(business.getId())
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());

        assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,4,21.1 Implementação dos testes integrados do findAll;Aula de empreendedorismo",
            "1,2,2,4,Não cometa esses erros ao trabalhar com Microsserviços;System Design no Mercado Livre na prática",
    })
    public void givenAValidPaging_whenCallsFindAll_shouldReturnPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideos
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedTitle : expectedVideos.split(";")) {
            final var actualTitle = actualPage.items().get(index).title();
            assertEquals(expectedTitle, actualTitle);
            index++;
        }
    }

    @ParameterizedTest
    @CsvSource({
            "system,0,10,1,1,System Design no Mercado Livre na prática",
            "micross,0,10,1,1,Não cometa esses erros ao trabalhar com Microsserviços",
            "empreendedorismo,0,10,1,1,Aula de empreendedorismo",
            "21,0,10,1,1,21.1 Implementação dos testes integrados do findAll",
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideo
    ) {
        // given
        mockVideos();

        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedVideo, actualPage.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "title,asc,0,10,4,4,21.1 Implementação dos testes integrados do findAll",
            "title,desc,0,10,4,4,System Design no Mercado Livre na prática",
            "createdAt,asc,0,10,4,4,System Design no Mercado Livre na prática",
            "createdAt,desc,0,10,4,4,Aula de empreendedorismo",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideo
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";


        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        assertEquals(expectedVideo, actualPage.items().get(0).title());
    }

    private void mockVideos() {
        videoGateway.create(Video.newVideo(
                "System Design no Mercado Livre na prática",
                Fixture.Videos.description(),
                Year.of(2022),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(lives.getId()),
                Set.of(tech.getId()),
                Set.of(wesley.getId(), wellington.getId())
        ));

        videoGateway.create(Video.newVideo(
                "Não cometa esses erros ao trabalhar com Microsserviços",
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        videoGateway.create(Video.newVideo(
                "21.1 Implementação dos testes integrados do findAll",
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(aulas.getId()),
                Set.of(tech.getId()),
                Set.of(wellington.getId())
        ));

        videoGateway.create(Video.newVideo(
                "Aula de empreendedorismo",
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(aulas.getId()),
                Set.of(business.getId()),
                Set.of(wesley.getId())
        ));
    }
}