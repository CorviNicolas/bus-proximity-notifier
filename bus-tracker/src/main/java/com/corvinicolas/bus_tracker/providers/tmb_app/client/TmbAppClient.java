package com.corvinicolas.bus_tracker.providers.tmb_app.client;

import com.corvinicolas.bus_tracker.application.service.model.BusProximityModel;
import com.corvinicolas.bus_tracker.config.TmbCatProviderConfig;
import com.corvinicolas.bus_tracker.mappers.BusProximityMapper;
import com.corvinicolas.bus_tracker.providers.tmb_app.dto.IBusStopPrevisionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.Map;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class TmbAppClient {
    private final WebClient webClient;
    private final TmbCatProviderConfig tmbCatProviderConfig;
    private final BusProximityMapper mapper;

    public Mono<BusProximityModel> getStopPrevision() {
        String line = "V1";
        return webClient
                .get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/ibus/lines/{line}/stops/{stopCode}");
                    uriBuilder.queryParam(TmbCatProviderConfig.APP_ID_PARAM, tmbCatProviderConfig.getAppId());
                    uriBuilder.queryParam(TmbCatProviderConfig.APP_KEY_PARAM, tmbCatProviderConfig.getAppKey());
                    return uriBuilder.build(Map.of("line", line, "stopCode", "1775"));
                })
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(IBusStopPrevisionResponse.class))
                .handle(new BiConsumer<IBusStopPrevisionResponse, SynchronousSink<IBusStopPrevisionResponse>>() {
                    @Override
                    public void accept(IBusStopPrevisionResponse iBusStopPrevisionResponse, SynchronousSink<IBusStopPrevisionResponse> synchronousSink) {

                        synchronousSink.next(iBusStopPrevisionResponse);
                    }
                })
                .map(iBusStopPrevisionResponse -> mapper.iBusToServiceModel(iBusStopPrevisionResponse, line));
    }

}
