package com.example.apigateway;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.apigateway.config.WasStateManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleService {

	private final WasStateManager wasStateManager;

	@Scheduled(cron = "* */1 * * * *")
	public void healthCheck() {

		wasStateManager.getDownInstances().parallelStream()
			.forEach(was -> WebClient.builder()
								.baseUrl(String.format("http://%s/tpms/health", was))
								.build()
								.get()
								.retrieve()
								.bodyToMono(byte[].class)
								.onErrorResume(v -> markDownWAS(was, v))
								.subscribe(v -> markupWASIfUp(was, v)));

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
