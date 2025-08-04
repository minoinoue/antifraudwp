package com.tuandat.antifraudwp.service;

import com.tuandat.antifraudwp.model.Video;
import com.tuandat.antifraudwp.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public Video getVideo(Long id) {
        Optional<Video> video = videoRepository.findById(id);
        if (video.isPresent()) {
            return video.get();
        }
        throw new RuntimeException("Video not found with id: " + id);
    }

    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }

    public void deleteVideo(Long id) {
        videoRepository.deleteById(id);
    }
} 