package com.tuandat.antifraudwp.repository;

import com.tuandat.antifraudwp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
} 