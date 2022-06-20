package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RepositoryProduct extends JpaRepository<Product,Integer>, JpaSpecificationExecutor<Product> {
}
