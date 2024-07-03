package com.k8s.poc.product;

import com.k8s.poc.product.model.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Resource;

@SpringBootApplication
public class ProductApplication implements CommandLineRunner, WebMvcConfigurer {

    @Resource
    private ProductRepository productRepository;

    private static final String MOCK_DESCRIPTION = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Numquam, sapiente illo. Sit error voluptas repellat rerum quidem, soluta enim perferendis voluptates laboriosam. Distinctio, officia quis dolore quos sapiente tempore alias.";

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Mock data
        productRepository.save(new Product("1001", "Phone", MOCK_DESCRIPTION, "https://google.com", 15.23));
        productRepository.save(new Product("1002", "Laptop", MOCK_DESCRIPTION, "https://google.com", 177.23));
        productRepository.save(new Product("1003", "Monitor", MOCK_DESCRIPTION, "https://google.com", 53.44));
        productRepository.save(new Product("1004", "Keyboard", MOCK_DESCRIPTION, "https://google.com", 80.31));
        productRepository.save(new Product("1005", "Mouse", MOCK_DESCRIPTION, "https://google.com", 40.25));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST");
    }

}
