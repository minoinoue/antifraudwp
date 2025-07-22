package com.tuandat.antifraudwp.service;

import com.tuandat.antifraudwp.model.Video;
import com.tuandat.antifraudwp.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }
} 