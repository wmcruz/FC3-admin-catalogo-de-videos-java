package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class UpdateCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedId = aCategory.getId();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));
        Mockito.verify(categoryGateway, times(1)).update(argThat(
                aUpdateCategory ->
                        Objects.equals(expectedName, aUpdateCategory.getName())
                                && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                                && Objects.equals(expectedId, aUpdateCategory.getId())
                                && Objects.equals(aCategory.getCreatedAt(), aUpdateCategory.getCreatedAt())
                                && aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                                && Objects.isNull(aUpdateCategory.getDeletedAt())));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedId = aCategory.getId();
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(0)).update(any());
    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedId = aCategory.getId();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));
        Mockito.verify(categoryGateway, times(1)).update(argThat(
                aUpdateCategory ->
                        Objects.equals(expectedName, aUpdateCategory.getName())
                                && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                                && Objects.equals(expectedId, aUpdateCategory.getId())
                                && Objects.equals(aCategory.getCreatedAt(), aUpdateCategory.getCreatedAt())
                                && aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                                && Objects.nonNull(aUpdateCategory.getDeletedAt())));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedId = aCategory.getId();
        final var expectedName = "Filme";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(1)).update(argThat(
                aUpdateCategory ->
                        Objects.equals(expectedName, aUpdateCategory.getName())
                                && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                                && Objects.equals(expectedId, aUpdateCategory.getId())
                                && Objects.equals(aCategory.getCreatedAt(), aUpdateCategory.getCreatedAt())
                                && aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                                && Objects.isNull(aUpdateCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnINotFoundException() {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var aCommand = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)));
        Mockito.verify(categoryGateway, times(0)).update(any());
    }
}