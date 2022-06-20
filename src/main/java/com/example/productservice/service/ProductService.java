package com.example.productservice.service;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.RepositoryProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    RepositoryProduct productRepository;

    public Page<Product> findAll(int page, int limit,
                                 Specification<Product> productSpecification) {
        return productRepository.findAll(
                productSpecification, PageRequest.of(page - 1, limit));
    }
}
