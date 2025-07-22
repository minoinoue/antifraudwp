package com.tuandat.antifraudwp.service;

import com.tuandat.antifraudwp.model.News;
import com.tuandat.antifraudwp.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }
} 