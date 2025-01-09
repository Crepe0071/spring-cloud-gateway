package com.example.apigateway.config;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

	private final WasStateManager wasStateManager;

	private static final List<String> WAS_LIST = List.of("localhost:8081", "localhost:8082");

	@Override
	public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {

		WAS_LIST.parallelStream()
			.forEach(was ->
						 WebClient.builder()
							 .baseUrl(String.format("http://%s/tpms/health", was))
							 .build()
							 .get()
							 .retrieve()
							 .bodyToMono(byte[].class)
							 .onErrorResume(v -> markDownWAS(was, v))
							 .subscribe(v -> markupWASIfUp(was, v))
			);
	}

	private <T> Mono<T> markDownWAS(String was, Throwable v) {
		wasStateManager.markDown(was);
		log.error("{}{}downed WAS: {}{}total down list: {}", v.getMessage(), System.lineSeparator(), was, System.lineSeparator(), wasStateManager.getDownInstances());
		return Mono.empty();
	}

	private void markupWASIfUp(String was, byte[] v) {
		if (v[0] == 1) {
			wasStateManager.markUp(was);
			log.info("added WAS: {}{}total up list: {}", was, System.lineSeparator(), wasStateManager.getUpInstances());
		}
	}

}


