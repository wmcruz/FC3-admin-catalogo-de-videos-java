package com.fullcycle.admin.catalogo.application.castmember.retrieve.list;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class ListCastMemberUseCaseIT {

    @Autowired
    private ListCastMembersUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidQuery_whenCallsListCastMembers_shouldReturnAll() {
        // given
        final var members = List.of(
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()),
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())
        );

        this.castMemberRepository.saveAllAndFlush(
                members.stream()
                        .map(CastMemberJpaEntity::from)
                        .toList()
        );

        assertEquals(2, this.castMemberRepository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = members.stream()
                .map(CastMemberListOutput::from)
                .toList();

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertTrue(expectedItems.size() == actualOutput.items().size()
                && expectedItems.containsAll(actualOutput.items()));

        verify(castMemberGateway).findAll(any());
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndIsEmpty_shouldReturn() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        assertEquals(0, this.castMemberRepository.count());

        final var expectedItems = List.<CastMemberListOutput>of();

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(castMemberGateway).findAll(any());
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRamdomException_shouldException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(castMemberGateway).findAll(any());

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualException = assertThrows(IllegalStateException.class, () -> {
            useCase.execute(aQuery);
        });

        // then
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findAll(any());
    }
}