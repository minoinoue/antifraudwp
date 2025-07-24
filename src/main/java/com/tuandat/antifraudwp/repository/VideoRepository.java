package com.tuandat.antifraudwp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tuandat.antifraudwp.model.Video;
 
@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
} 