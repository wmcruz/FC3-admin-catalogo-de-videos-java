package com.fullcycle.admin.catalogo.infrastructure.video;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoID;
import com.fullcycle.admin.catalogo.domain.video.VideoPreview;
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.utils.SqlUtils;
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository videoRepository;

    public DefaultVideoGateway(final VideoRepository videoRepository) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
    }

    @Override
    @Transactional
    public Video create(final Video video) {
        return this.save(video);
    }

    @Override
    public void deleteById(final VideoID anId) {
        final var aVideoId = anId.getValue();
        if (this.videoRepository.existsById(aVideoId))
            this.videoRepository.deleteById(aVideoId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(final VideoID anInd) {
        return this.videoRepository.findById(anInd.getValue())
                .map(VideoJpaEntity::toAggregate);
    }

    @Override
    @Transactional
    public Video update(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualVideo = this.videoRepository.findAll(
                SqlUtils.like(aQuery.terms()),
                toString(aQuery.castMembers()),
                toString(aQuery.categories()),
                toString(aQuery.genres()),
                page
        );

        return new Pagination<>(
                actualVideo.getNumber(),
                actualVideo.getSize(),
                actualVideo.getTotalElements(),
                actualVideo.toList()
        );
    }

    private static Set<String> toString(final Set<? extends Identifier> ids) {
        if (ids == null || ids.isEmpty())
            return null;

        return ids.parallelStream()
                .map(Identifier::getValue)
                .collect(Collectors.toSet());
    }

    private Video save(final Video video) {
        return this.videoRepository.save(VideoJpaEntity.from(video))
                .toAggregate();
    }
}