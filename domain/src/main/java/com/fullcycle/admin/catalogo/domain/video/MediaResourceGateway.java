package com.fullcycle.admin.catalogo.domain.video;

public interface MediaResourceGateway {

    AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource aResource);

    ImageMedia storeImage(VideoID anId, VideoResource aResource);

    void cleanResources(VideoID anId);
}