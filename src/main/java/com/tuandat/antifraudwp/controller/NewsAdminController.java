package com.tuandat.antifraudwp.controller;

import com.tuandat.antifraudwp.model.News;
import com.tuandat.antifraudwp.repository.NewsRepository;
import com.tuandat.antifraudwp.service.CategoryService;
import com.tuandat.antifraudwp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;
import com.tuandat.antifraudwp.model.Tag;

@Controller
@RequestMapping("/admin/news")
public class NewsAdminController {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired private CategoryService categoryService;
    @Autowired private TagService tagService;

    @GetMapping("")
    public String listNews(Model model) {
        List<News> newsList = newsRepository.findAll();
        model.addAttribute("newsList", newsList);
        return "admin/news_list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("news", new News());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tags", tagService.getAllTags());
        return "admin/news_form";
    }

    @PostMapping("/create")
    public String createNews(@ModelAttribute News news, @RequestParam("category.id") Long categoryId, @RequestParam(value = "tags", required = false) List<Long> tagIds) {
        news.setCreatedAt(java.time.LocalDateTime.now());
        news.setUpdatedAt(java.time.LocalDateTime.now());
        categoryService.getCategory(categoryId).ifPresent(news::setCategory);
        if (tagIds != null) {
            Set<Tag> tags = tagIds.stream()
                .map(id -> tagService.getTag(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
            news.setTags(tags);
        }
        newsRepository.save(news);
        return "redirect:/admin/news";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<News> newsOpt = newsRepository.findById(id);
        if (newsOpt.isPresent()) {
            model.addAttribute("news", newsOpt.get());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("tags", tagService.getAllTags());
            return "admin/news_form";
        }
        return "redirect:/admin/news";
    }

    @PostMapping("/edit/{id}")
    public String updateNews(@PathVariable Long id, @ModelAttribute News news, @RequestParam("category.id") Long categoryId, @RequestParam(value = "tags", required = false) List<Long> tagIds) {
        Optional<News> oldNewsOpt = newsRepository.findById(id);
        if (oldNewsOpt.isPresent()) {
            News oldNews = oldNewsOpt.get();
            news.setId(id);
            news.setCreatedAt(oldNews.getCreatedAt());
            news.setUpdatedAt(java.time.LocalDateTime.now());
            categoryService.getCategory(categoryId).ifPresent(news::setCategory);
            if (tagIds != null) {
                Set<Tag> tags = tagIds.stream()
                    .map(tid -> tagService.getTag(tid).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
                news.setTags(tags);
            } else {
                news.setTags(new HashSet<>());
            }
            newsRepository.save(news);
        }
        return "redirect:/admin/news";
    }

    @PostMapping("/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsRepository.deleteById(id);
        return "redirect:/admin/news";
    }
} 