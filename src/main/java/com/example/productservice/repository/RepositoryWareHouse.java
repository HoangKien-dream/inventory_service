package com.example.productservice.repository;

import com.example.productservice.entity.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryWareHouse extends JpaRepository<WareHouse,Integer> {
}
