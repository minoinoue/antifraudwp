package com.tuandat.antifraudwp.controller;

import com.tuandat.antifraudwp.model.Video;
import com.tuandat.antifraudwp.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/videos")
public class VideoAdminController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public String listVideos(Model model) {
        model.addAttribute("videos", videoService.getAllVideos());
        return "admin/video_list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("video", new Video());
        return "admin/video_form";
    }

    @PostMapping("/create")
    public String createVideo(@ModelAttribute Video video) {
        video.setCreatedAt(LocalDateTime.now());
        videoService.saveVideo(video);
        return "redirect:/admin/videos";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Video video = videoService.getVideo(id);
        model.addAttribute("video", video);
        return "admin/video_form";
    }

    @PostMapping("/edit/{id}")
    public String updateVideo(@PathVariable Long id, @ModelAttribute Video video) {
        Video existingVideo = videoService.getVideo(id);
        existingVideo.setTitle(video.getTitle());
        existingVideo.setDescription(video.getDescription());
        existingVideo.setVideoUrl(video.getVideoUrl());
        existingVideo.setThumbnailUrl(video.getThumbnailUrl());
        existingVideo.setStatus(video.getStatus());
        videoService.saveVideo(existingVideo);
        return "redirect:/admin/videos";
    }

    @GetMapping("/delete/{id}")
    public String deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return "redirect:/admin/videos";
    }
} 