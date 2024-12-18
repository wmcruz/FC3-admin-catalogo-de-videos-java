package com.fullcycle.admin.catalogo.application.genre.delete;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        // given
        final var aGenre = genreGateway.create(Genre.newGenre("Ação", true));

        final var expectedId = aGenre.getId();

        assertEquals(1, genreRepository.count());

        // when
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        // given
        genreGateway.create(Genre.newGenre("Ação", true));
        final var expectedId = GenreID.from("123");

        assertEquals(1, genreRepository.count());

        // when
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        assertEquals(1, genreRepository.count());
    }
}