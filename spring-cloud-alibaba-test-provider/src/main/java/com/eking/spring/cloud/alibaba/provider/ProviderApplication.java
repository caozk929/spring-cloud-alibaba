/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eking.spring.cloud.alibaba.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zeke
 */
@ImportResource({"classpath:spring.xml"})
@EnableDiscoveryClient
@SpringBootApplication
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    @Value("${node}")
    private String node;

    @RestController
    class EchoController {

        @GetMapping("/")
//        @GlobalTransactional
        public ResponseEntity index() {
            return new ResponseEntity<>("index error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @GetMapping("/test")
        public ResponseEntity test() {
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @GetMapping("/sleep")
        public String sleep() {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ok";
        }

        @GetMapping("/echo/{string}")
        public String echo(@PathVariable String string) {
            return node + "hello Nacos Discovery " + string;
        }

        @GetMapping("/divide")
        public String divide(Integer a111, Integer b222, Integer c333) {
            System.out.println(c333);
            return String.valueOf(a111 / b222);
        }

    }

}
