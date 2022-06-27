package com.example.productservice.service;
import com.example.productservice.entity.Category;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.RepositoryCategory;
import com.example.productservice.repository.RepositoryProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProductService {
    @Autowired
    RepositoryProduct productRepository;
    @Autowired
    RepositoryCategory repositoryCategory;

    public Page<Product> findAll(int page, int limit,
                                 Specification<Product> productSpecification) {
        return productRepository.findAll(
                productSpecification, PageRequest.of(page - 1, limit));
    }

    public Product save(Product product){
        Category category = repositoryCategory.findById(product.getCategoryId()).orElse(null);
        if (category != null){
            product.setCategory(category);
            product.setStatus(1);
            return productRepository.save(product);
        }
         return null;
    }

    public Product findById(int id){
        Product product = productRepository.findById(id).orElse(null);
        if (product != null){
            return product;
        }
        return null;
    }

    public Product updated(int id, Product product){
        Product oldProduct = productRepository.findById(id).orElse(null);
        if (oldProduct != null){
            if (product.getName() != null){
                oldProduct.setName(product.getName());
            }
            if (product.getDescription() !=null){
                oldProduct.setDescription(product.getDescription());
            }
            if (product.getThumbnail() != null){
                oldProduct.setThumbnail(product.getThumbnail());
            }
            if (product.getPrice() > 0){
                oldProduct.setPrice(product.getPrice());
            }
            if (product.getCategoryId() > 0){
                Category category = repositoryCategory.findById(product.getCategoryId()).orElse(null);
                product.setCategory(category);
            }
            product.setUpdatedAt(LocalDate.now());
            return oldProduct;
        }
        return  null;
    }

    public boolean isDelete(int id){
        Product product = productRepository.findById(id).orElse(null);
        if(product != null){
            productRepository.delete(product);
            return  true;
        }
        return false;
    }
}
