//package com.eking.spring.cloud.alibaba.consumer;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//@SpringBootApplication
//@EnableDiscoveryClient
//public class ConsumerApplication {
//
//
//    public static void main(String[] args) {
//        SpringApplication.run(ConsumerApplication.class, args);
//    }
//
//    @RestController
//    class EchoController {
//
//        @Autowired
//        private RestTemplate restTemplate;
//
//        @LoadBalanced
//        @Bean
//        public RestTemplate restTemplate() {
//            return new RestTemplate();
//        }
//        @GetMapping("/echo/{string}")
//        public String echo(@PathVariable String string) {
//            System.out.println("baha");
//            return restTemplate.getForObject("http://127.0.0.1:18082/echo/" + string, String.class);
//        }
//    }
//}
