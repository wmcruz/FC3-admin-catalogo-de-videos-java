package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void givenAInvalidNullName_whenCallsNewMember_shouldReceiveANotification() {
        // given
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        // when
        final var actualException = assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsNewMember_shouldReceiveANotification() {
        // given
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualException = assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidNameWithLenghtMoreThan255_whenCallsNewMember_shouldReceiveANotification() {
        // given
        final var expectedName = "   Do mesmo modo, a mobilidade dos capitais internacionais garante a contribuição de um grupo importante na determinação do remanejamento dos quadros funcionais. " +
                "A certificação de metodologias que nos auxiliam a lidar com o desenvolvimento contínuo de distintas formas de atuação prepara-nos para enfrentar situações atípicas decorrentes dos procedimentos normalmente adotados. " +
                "As experiências acumuladas demonstram que a constante divulgação das informações ainda não demonstrou convincentemente que vai participar na mudança do sistema de formação de quadros que corresponde às necessidades.";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        // when
        final var actualException = assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidNullType_whenCallsNewMember_shouldReceiveANotification() {
        // given
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        // when
        final var actualException = assertThrows(
                NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}