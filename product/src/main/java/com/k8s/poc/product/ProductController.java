package com.k8s.poc.product;

import com.k8s.poc.product.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Resource
    private ProductRepository productRepository;

    @Operation(summary = "Get all products")
    @GetMapping
    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    @Operation(summary = "Get product by id")
    @GetMapping("/{id}")
    public Product findProduct(@PathVariable(name = "id") String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
