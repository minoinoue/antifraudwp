package com.tuandat.antifraudwp.controller;

import com.tuandat.antifraudwp.model.Tag;
import com.tuandat.antifraudwp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/admin/tags")
public class TagAdminController {
    @Autowired
    private TagService tagService;

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        return "admin/tag_list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tag_form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Tag tag) {
        tagService.saveTag(tag);
        return "redirect:/admin/tags";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Tag> tagOpt = tagService.getTag(id);
        if (tagOpt.isPresent()) {
            model.addAttribute("tag", tagOpt.get());
            return "admin/tag_form";
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Tag tag) {
        tag.setId(id);
        tagService.saveTag(tag);
        return "redirect:/admin/tags";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        tagService.deleteTag(id);
        return "redirect:/admin/tags";
    }
} 