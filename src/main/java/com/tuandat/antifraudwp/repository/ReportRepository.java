package com.tuandat.antifraudwp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tuandat.antifraudwp.model.Report;
 
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
} 