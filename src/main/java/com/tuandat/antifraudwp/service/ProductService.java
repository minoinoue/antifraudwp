package com.tuandat.antifraudwp.service;

import com.tuandat.antifraudwp.model.Product;
import com.tuandat.antifraudwp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
} 