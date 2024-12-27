package com.fullcycle.admin.catalogo.domain.castmember;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CastMemberTest {

    @Test
    public void givenAValidParams_whenCallsNewMember_thenInstantiateACastMember() {
        // given
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        // when
        final var actualMember = CastMember.newMember(expectedName, expectedType);

        // then
        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertNotNull(actualMember.getCreatedAt());
        assertNotNull(actualMember.getUpdatedAt());
        assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
    }
}