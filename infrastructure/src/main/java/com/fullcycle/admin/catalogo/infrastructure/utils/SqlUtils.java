package com.fullcycle.admin.catalogo.infrastructure.utils;

public final class SqlUtils {

    private SqlUtils() {}

    public static String like(final String term) {
        if (term == null) return null;
        return "%" + term + "%";
    }
}