package com.example.productservice.repository;

import com.example.productservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryCategory extends JpaRepository<Category,Integer> {
}
