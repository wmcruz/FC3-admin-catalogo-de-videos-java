package com.fullcycle.admin.catalogo.infrastructure.castmember;

import com.fullcycle.admin.catalogo.Fixture;
import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void testDependencies() {
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(castMemberRepository);
    }

    @Test
    public void givenAValidCastMember_whenCallCreate_shouldPersistIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId();

        assertEquals(0, castMemberRepository.count());

        // when
        final var actualMember = castMemberGateway.create(CastMember.with(aMember));

        // then
        assertEquals(1, castMemberRepository.count());

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());

        final var persistMember = castMemberRepository.findById(expectedId.getValue()).get();
        assertEquals(expectedId.getValue(), persistMember.getId());
        assertEquals(expectedName, persistMember.getName());
        assertEquals(expectedType, persistMember.getType());
        assertEquals(aMember.getCreatedAt(), persistMember.getCreatedAt());
        assertEquals(aMember.getUpdatedAt(), persistMember.getUpdatedAt());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdate_shouldRefreshIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var aMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();

        final var currentMember = castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
        assertEquals(1, castMemberRepository.count());
        assertEquals("vind", currentMember.getName());
        assertEquals(CastMemberType.DIRECTOR, currentMember.getType());

        // when
        final var actualMember = castMemberGateway.update(
                CastMember.with(aMember).update(expectedName, expectedType)
        );

        // then
        assertEquals(1, castMemberRepository.count());

        assertEquals(expectedId, actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        assertTrue(aMember.getUpdatedAt().isBefore(actualMember.getUpdatedAt()));

        final var persistMember = castMemberRepository.findById(expectedId.getValue()).get();
        assertEquals(expectedId.getValue(), persistMember.getId());
        assertEquals(expectedName, persistMember.getName());
        assertEquals(expectedType, persistMember.getType());
        assertEquals(aMember.getCreatedAt(), persistMember.getCreatedAt());
        assertTrue(aMember.getUpdatedAt().isBefore(persistMember.getUpdatedAt()));
    }
}