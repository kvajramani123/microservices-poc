package com.microservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@EnableCircuitBreaker
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationClientApplication {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ReservationClientApplication.class, args);
    }
}


@RestController
@RequestMapping("/reservations")
class ReservationApiGatewayRestController {
    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;
    @Autowired
    LoadBalancerClient loadBalancerClient;

    public String getReservationNamesFallback() {
        return "MicroServices Error";
    }


    //@HystrixCommand(fallbackMethod = "getReservationNamesFallback")
    @GetMapping(value= "/names")
    public @ResponseBody String getReservationNames() {
        //List<String> x = new ArrayList<String>(){};
    /*    ServiceInstance serviceInstance = loadBalancerClient.choose("reservation-service");
        URI uri = serviceInstance.getUri();
        String url = uri.toString() + "/reservations";*/
        //

        //ParameterizedTypeReference<Resources<Reservation>> rtr = new ParameterizedTypeReference<Resources<Reservation>>() {
        //};
        //ResponseEntity<Resources<Reservation>> entity = this.restTemplate.exchange("http://reservation-service/reservations", HttpMethod.GET, null, rtr);
        ResponseEntity<String> reservation = this.restTemplate.exchange("http://reservation-service/reservations", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        return reservation.getBody();

    }
}

class Reservation {
    private String reservationName;

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }
}

