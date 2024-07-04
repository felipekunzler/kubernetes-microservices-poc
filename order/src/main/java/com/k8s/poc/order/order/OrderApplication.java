package com.k8s.poc.order.order;

import io.nats.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import jakarta.annotation.Resource;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class OrderApplication implements CommandLineRunner {

    @Resource
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    private static final Logger LOG = LoggerFactory.getLogger(OrderApplication.class);

    @Override
    public void run(String... args) throws Exception {
        String natsUrl = environment.getProperty("NATS_URL", Options.DEFAULT_URL);
        Connection nc = Nats.connect(natsUrl);

        Dispatcher d = nc.createDispatcher((msg) -> {
        });
        Subscription s = d.subscribe("placeOrder", (msg) -> {
            String response = new String(msg.getData(), StandardCharsets.UTF_8);
            LOG.info("Message received: " + response);
        });
    }

}
