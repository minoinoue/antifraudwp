package com.tuandat.antifraudwp.controller;

import com.tuandat.antifraudwp.model.News;
import com.tuandat.antifraudwp.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/news")
public class NewsAdminController {
    @Autowired
    private NewsRepository newsRepository;

    @GetMapping("")
    public String listNews(Model model) {
        List<News> newsList = newsRepository.findAll();
        model.addAttribute("newsList", newsList);
        return "admin/news_list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("news", new News());
        return "admin/news_form";
    }

    @PostMapping("/create")
    public String createNews(@ModelAttribute News news) {
        news.setCreatedAt(LocalDateTime.now());
        news.setUpdatedAt(LocalDateTime.now());
        newsRepository.save(news);
        return "redirect:/admin/news";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<News> newsOpt = newsRepository.findById(id);
        if (newsOpt.isPresent()) {
            model.addAttribute("news", newsOpt.get());
            return "admin/news_form";
        }
        return "redirect:/admin/news";
    }

    @PostMapping("/edit/{id}")
    public String updateNews(@PathVariable Long id, @ModelAttribute News news) {
        news.setId(id);
        news.setUpdatedAt(LocalDateTime.now());
        newsRepository.save(news);
        return "redirect:/admin/news";
    }

    @PostMapping("/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsRepository.deleteById(id);
        return "redirect:/admin/news";
    }
} 