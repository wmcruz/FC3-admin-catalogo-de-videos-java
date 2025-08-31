package com.fullcycle.admin.catalogo.infrastructure.services.impl;

import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static com.google.cloud.storage.Storage.BlobListOption.prefix;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GCStorageServiceTest {

    private GCStorageService target;

    private Storage storage;

    private String bucket = "fc3_test";

    @BeforeEach
    public void setUp() {
        this.storage = Mockito.mock(Storage.class);
        this.target = new GCStorageService(this.bucket, this.storage);
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldPersistIt() {
        // given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(blob).when(storage).create(any(BlobInfo.class), any());

        // when
        this.target.store(expectedName, expectedResource);

        // then
        final var captor = ArgumentCaptor.forClass(BlobInfo.class);

        verify(storage, times(1)).create(captor.capture(), eq(expectedResource.content()));

        final var actualBlob = captor.getValue();
        assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        assertEquals(expectedName, actualBlob.getBlobId().getName());
        assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
        assertEquals(expectedResource.contentType(), actualBlob.getContentType());
    }

    @Test
    public void givenValidResource_whenCallsGet_shouldRetrieveIt() {
        // given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(blob).when(storage).get(anyString(), anyString());

        // when
        final var actualResource = this.target.get(expectedName).get();

        // then
        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldBeEmpty() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = expectedResource.name();

        doReturn(null).when(storage).get(eq(bucket), eq(expectedId));

        final var actualContent = target.get(expectedId);

        assertTrue(actualContent.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsList_shouldRetrieveAll() {
        // given
        final var expectedPrefix = "media_";

        final var expectedNameVideo = expectedPrefix + IdUtils.uuid();
        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedNameBanner = expectedPrefix + IdUtils.uuid();
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);

        final var expectedResources = List.of(expectedNameBanner, expectedNameVideo);

        final var blobVideo = mockBlob(expectedNameVideo, expectedVideo);
        final var blobBanner = mockBlob(expectedNameBanner, expectedBanner);

        final var page = Mockito.mock(Page.class);
        doReturn(List.of(blobVideo, blobBanner)).when(page).iterateAll();

        doReturn(page).when(storage).list(anyString(), any());

        // when
        final var actualResource = this.target.list(expectedPrefix);

        // then
        verify(storage, times(1)).list(eq(this.bucket), eq(prefix(expectedPrefix)));

        assertTrue(expectedResources.size() == actualResource.size()
                && expectedResources.containsAll(actualResource));
    }

    @Test
    public void givenValidNames_whenCallsDelete_shouldDeleteAll() {
        // given
        final var expectedPrefix = "media_";

        final var expectedNameVideo = expectedPrefix + IdUtils.uuid();
        final var expectedNameBanner = expectedPrefix + IdUtils.uuid();

        final var expectedResources = List.of(expectedNameBanner, expectedNameVideo);

        // when
        this.target.deleteAll(expectedResources);

        // then
        final var captor = ArgumentCaptor.forClass(List.class);

        verify(storage, times(1)).delete(captor.capture());

        final var actualResources = ((List<BlobId>) captor.getValue()).stream()
                .map(BlobId::getName)
                .toList();

        assertTrue(expectedResources.size() == actualResources.size()
                && expectedResources.containsAll(actualResources));
    }

    private Blob mockBlob(final String name, final Resource expectedResource) {
        final var blob = Mockito.mock(Blob.class);

        when(blob.getBlobId()).thenReturn(BlobId.of(this.bucket, name));
        when(blob.getCrc32cToHexString()).thenReturn(expectedResource.checksum());
        when(blob.getContent()).thenReturn(expectedResource.content());
        when(blob.getContentType()).thenReturn(expectedResource.contentType());
        when(blob.getName()).thenReturn(expectedResource.name());

        return blob;
    }
}