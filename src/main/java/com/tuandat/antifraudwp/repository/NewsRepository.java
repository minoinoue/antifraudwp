package com.tuandat.antifraudwp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tuandat.antifraudwp.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
} 