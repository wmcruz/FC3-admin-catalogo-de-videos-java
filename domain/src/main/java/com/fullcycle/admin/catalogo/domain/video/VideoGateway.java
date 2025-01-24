package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {

    Video create(Video video);

    void deleteById(VideoID anId);

    Optional<Video> findById(VideoID anInd);

    Video update(Video aVideo);

    Pagination<Video> findAll(VideoSearchQuery aQuery);
}