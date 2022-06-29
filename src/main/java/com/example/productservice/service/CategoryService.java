package com.example.productservice.service;

import com.example.productservice.entity.Category;
import com.example.productservice.repository.RepositoryCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryService {
    @Autowired
    RepositoryCategory repositoryCategory;
    public List<Category> findAll(){
        return repositoryCategory.findAll();
    }
}
