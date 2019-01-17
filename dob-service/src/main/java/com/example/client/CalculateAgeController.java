package com.example.client;


import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * This is client side load balancing with spring auto configuration.
 * It actually uses the ribbon and load balanced rest template from the
 * spring.
 */
@RestController
public class CalculateAgeController {

    private static final Logger logger = LoggerFactory.getLogger(CalculateAgeController.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @ResponseBody
    @RequestMapping(value = "/age", method = RequestMethod.GET)
    public int getStudents(@RequestParam("dob") String dob) {

        logger.info("Dob is '{}'", dob);

        //For example only. Shows our list of instances
        final List<ServiceInstance> instances = this.discoveryClient.getInstances("age-service");

        logger.info("Instances size ===========> {}", instances.size());

        for(final ServiceInstance instance : instances){
            logger.info("Instance: {} ", instance.getHost().toString());
            logger.info("Port: {} ", instance.getPort());
            logger.info("URI: {} ", instance.getUri().toString());
            logger.info("Scheme: {} ", instance.getScheme());
            logger.info("Service Id : {} ", instance.getServiceId());
        }

        if(instances.size() != 0) {

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("http://age-service/age")
                    .queryParam("dob", dob);

            ResponseEntity<Integer> response = restTemplate.exchange(uriBuilder.toUriString(),
                    HttpMethod.GET, null, Integer.class);

            logger.debug("Age for dob '{}' is {} ", dob, response.getBody());

            return response.getBody();
        }

        return 0;
    }
}
