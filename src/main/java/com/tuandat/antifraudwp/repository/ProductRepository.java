package com.tuandat.antifraudwp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tuandat.antifraudwp.model.Product;
 
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
} 