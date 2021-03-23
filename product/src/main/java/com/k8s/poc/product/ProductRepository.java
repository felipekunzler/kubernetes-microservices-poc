package com.k8s.poc.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, String> {

    List<Product> findByName(@Param("name") String name);

}
