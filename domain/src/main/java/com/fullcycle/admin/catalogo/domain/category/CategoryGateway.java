package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {

    Category create(Category aCategory);
    Category update(Category aCategory);
    Optional<Category> findById(CategoryID anId);
    Pagination<Category> findAll(SearchQuery aQuery);
    void deleteById(CategoryID anId);
}