package com.fullcycle.admin.catalogo.application.genre.update;

import java.util.List;

public record UpdateGenreCommand(
        String id,
        String name,
        boolean isActive,
        List<String> categories
) {
    public static UpdateGenreCommand with(
            final String anId,
            final String aName,
            final Boolean isActive,
            final List<String> categories
    ) {
        return new UpdateGenreCommand(anId, aName, isActive != null ? isActive : true, categories);
    }
}
