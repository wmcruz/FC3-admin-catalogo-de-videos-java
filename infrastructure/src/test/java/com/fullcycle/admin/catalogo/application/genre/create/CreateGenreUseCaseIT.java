package com.fullcycle.admin.catalogo.application.genre.create;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class CreateGenreUseCaseIT {

    @Autowired
    private CreateGenreUseCase useCase;

    @SpyBean
    private CategoryGateway categoryGateway;

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes.getId());

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(genreGateway, times(1))
                .create(Mockito.argThat(aGenre ->
                        Objects.equals(expectedName, aGenre.getName())
                                && Objects.equals(expectedIsActive, aGenre.isActive())
                                && Objects.nonNull(aGenre.getCreatedAt())
                                && Objects.nonNull(aGenre.getUpdatedAt())
                                && Objects.isNull(aGenre.getDeletedAt())
                ));

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size()
            && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());

    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories
                .stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
