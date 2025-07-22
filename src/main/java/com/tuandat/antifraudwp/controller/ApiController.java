package com.tuandat.antifraudwp.controller;

import com.tuandat.antifraudwp.model.News;
import com.tuandat.antifraudwp.model.Video;
import com.tuandat.antifraudwp.model.Product;
import com.tuandat.antifraudwp.model.Report;
import com.tuandat.antifraudwp.service.NewsService;
import com.tuandat.antifraudwp.service.VideoService;
import com.tuandat.antifraudwp.service.ProductService;
import com.tuandat.antifraudwp.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReportService reportService;

    @GetMapping("/news")
    public List<News> getAllNews() {
        return newsService.getAllNews();
    }

    @GetMapping("/videos")
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/reports")
    public Report submitReport(@RequestBody Report report) {
        return reportService.saveReport(report);
    }
} 