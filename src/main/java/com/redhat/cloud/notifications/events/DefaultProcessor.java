package com.redhat.cloud.notifications.events;

import com.redhat.cloud.notifications.db.EndpointResources;
import com.redhat.cloud.notifications.models.Endpoint;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.smallrye.mutiny.Multi;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DefaultProcessor {
    @Inject
    EndpointResources resources;

    @Inject
    MeterRegistry registry;

    private Counter processedItems;
    private Counter enrichedEndpoints;

    @PostConstruct
    void init() {
        processedItems = registry.counter("processor.default.processed");
        enrichedEndpoints = registry.counter("processor.default.enriched.endpoints");
    }

    public Multi<Endpoint> getDefaultEndpoints(Endpoint defaultEndpoint) {
        processedItems.increment();
        return resources.getDefaultEndpoints(defaultEndpoint.getAccountId())
                .transform().byFilteringItemsWith(Endpoint::isEnabled)
                .onItem().invoke(() -> enrichedEndpoints.increment());
    }
}
