package com.example.productservice.repository;

import com.example.productservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryCategory extends JpaRepository<Category,Integer> {
}
