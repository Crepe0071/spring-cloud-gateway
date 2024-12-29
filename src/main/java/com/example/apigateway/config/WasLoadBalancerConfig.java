package com.example.apigateway.config;

import com.example.apigateway.EnumCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Configuration(proxyBeanMethods = false)
public class WasLoadBalancerConfig {

    @Bean
    public ServiceInstanceListSupplier serviceInstanceListSupplier(WasStateManager wasStateManager) {
        return new CustomServiceInstanceListSupplier(wasStateManager);
    }

    @Bean
    public RandomLoadBalancer randomLoadBalancer(LoadBalancerClientFactory loadBalancerClientFactory) {

        ObjectProvider<ServiceInstanceListSupplier> wasLazyProvider =
                loadBalancerClientFactory.getLazyProvider(EnumCode.WAS_ROUTE.getValue(), ServiceInstanceListSupplier.class);

        return new RandomLoadBalancer(wasLazyProvider, EnumCode.WAS_ROUTE.getValue());
    }

    // ServiceInstanceListSupplier의 구현체
    @RequiredArgsConstructor
    static class CustomServiceInstanceListSupplier implements ServiceInstanceListSupplier {

        private final WasStateManager wasStateManager;

        @Override
        public Flux<List<ServiceInstance>> get() {

            List<ServiceInstance> list = new ArrayList<>();
            int count = 1;
            for (String address : wasStateManager.getUpInstances()) {
                String[] ipAndPort = address.split(":");
                // 서비스 인스턴스를 직접 생성합니다
                ServiceInstance serviceInstance = new DefaultServiceInstance(
                        String.format("%s-instance%d", EnumCode.WAS_ROUTE.getValue(), count++), // 서비스 이름
                        EnumCode.WAS_ROUTE.getValue(),
                        ipAndPort[0],              // 호스트명 (예시)
                        Integer.parseInt(ipAndPort[1]),                     // 포트
                        false);                   // SSL 사용 여부

                list.add(serviceInstance);
            }

            // Flux를 사용하여 반환 (여러 서비스 인스턴스를 지원하려면 List<ServiceInstance>를 Flux로 반환)
            return Flux.just(list);
        }

        @Override
        public String getServiceId() {
            return EnumCode.WAS_ROUTE.getValue(); // 서비스 ID
        }
    }
}
