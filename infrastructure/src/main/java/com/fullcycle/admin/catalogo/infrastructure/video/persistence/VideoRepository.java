package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.video.VideoPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface VideoRepository extends JpaRepository<VideoJpaEntity, String> {

    @Query("""
            SELECT NEW com.fullcycle.admin.catalogo.domain.video.VideoPreview(
                v.id AS id,
                v.title AS title,
                v.description AS description,
                v.createdAt AS createdAt,
                v.updatedAt AS updatedAt
            )
            FROM Video v
                LEFT JOIN v.castMembers members
                LEFT JOIN v.categories categories
                LEFT JOIN v.genres genres
            WHERE
                (:terms IS NULL OR UPPER(v.title) LIKE :terms)
            AND
                (:castMembers IS NULL OR members.id.castMemberId IN :castMembers)
            AND
                (:categories IS NULL OR categories.id.categoryId IN :categories)
            AND
                (:genres IS NULL OR genres.id.genreId IN :genres)
            """)
    Page<VideoPreview> findAll(
            @Param("terms") String terms,
            @Param("castMembers") Set<String> castMembers,
            @Param("categories") Set<String> categories,
            @Param("genres") Set<String> genres,
            Pageable page);
}