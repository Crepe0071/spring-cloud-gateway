package com.example.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.List;

@Configuration
public class LoadBalancerConfig {

    @Bean
    public ServiceInstanceListSupplier serviceInstanceListSupplier(WasStateManager wasStateManager) {
        return new CustomServiceInstanceListSupplier(wasStateManager);
    }

    @Bean
    public RandomLoadBalancer randomLoadBalancer(LoadBalancerClientFactory loadBalancerClientFactory) {
        String serviceId = "dynamic-route";
        return new RandomLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId);
    }

    // ServiceInstanceListSupplier의 구현체
    @RequiredArgsConstructor
    static class CustomServiceInstanceListSupplier implements ServiceInstanceListSupplier {

        private final WasStateManager wasStateManager;

        @Override
        public Flux<List<ServiceInstance>> get() {

            String[] addr = wasStateManager.getRandomUpInstance().split(":");
            // 서비스 인스턴스를 직접 생성합니다
            ServiceInstance serviceInstance = new DefaultServiceInstance(
                    "dynamic-route-instance", // 서비스 이름
                    "dynamic-route",
                    addr[0],              // 호스트명 (예시)
                    Integer.parseInt(addr[1]),                     // 포트
                    false);                   // SSL 사용 여부

            // Flux를 사용하여 반환 (여러 서비스 인스턴스를 지원하려면 List<ServiceInstance>를 Flux로 반환)
            return Flux.just(List.of(serviceInstance));
        }

        @Override
        public String getServiceId() {
            return "dynamic-route"; // 서비스 ID
        }
    }

}
