package com.example.productservice.controller;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.RepositoryProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/product")
public class ProductController {
    @Autowired
    RepositoryProduct repositoryProduct;
    private static List<Product> list ;
    static {
        list = new ArrayList<>();
        list.add(new Product(1,"Iphone 13 Pro Max",300000,30));
        list.add(new Product(2,"Iphone 12 Pro Max",200000,30));
        list.add(new Product(3,"Iphone 11 Pro Max",100000,30));
        list.add(new Product(4,"Iphone Xs  Max",50000,30));
    }
    @RequestMapping(method = RequestMethod.POST)
    public List<Product> saveAll(){
       return repositoryProduct.saveAll(list);
    }
}
