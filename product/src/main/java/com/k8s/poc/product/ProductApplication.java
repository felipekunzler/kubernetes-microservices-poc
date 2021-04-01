package com.k8s.poc.product;

import com.k8s.poc.product.model.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class ProductApplication implements CommandLineRunner {

    @Resource
    private ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Mock data
        Product product1 = new Product("3312", "Phone", "Phone description", "https://google.com", 15.23);
        productRepository.save(product1);

        Product product2 = new Product("4423", "Laptop", "Laptop description", "https://google.com", 177.23);
        productRepository.save(product2);
    }

}
