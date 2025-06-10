package com.fullcycle.admin.catalogo.infrastructure.services.local;

import com.fullcycle.admin.catalogo.domain.video.Resource;
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorageService implements StorageService {

    private final Map<String, Resource> storage;

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }

    public Map<String, Resource> storage() {
        return this.storage;
    }

    public void reset() {
        this.storage.clear();
    }

    @Override
    public void deleteAll(final Collection<String> names) {
        names.forEach(this.storage::remove);
    }

    @Override
    public Optional<Resource> get(final String name) {
        return Optional.ofNullable(this.storage.get(name));
    }

    @Override
    public List<String> list(final String prefix) {
        if (Objects.isNull(prefix)) return Collections.emptyList();

        return this.storage.keySet().stream()
                .filter(it -> it.startsWith(prefix))
                .toList();
    }

    @Override
    public void store(final String name, final Resource resource) {
        this.storage.put(name, resource);
    }
}
