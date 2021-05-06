package app.a;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@SpringBootApplication
public class ApplicationA {

    @Autowired
    private ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationA.class, args);
    }

    @GetMapping(path = "manual")
    public Mono<String> getManual() {
        return WebClient.create()
                .get()
                .uri("http://service-b.default.svc.cluster.local:8080")
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping(path = "discovered")
    public Mono<String> getDiscovered() {
        return WebClient.builder().filter(loadBalancerExchangeFilterFunction).baseUrl("http://service-b").build()
                .get()
                .retrieve()
                .bodyToMono(String.class);
    }
}
