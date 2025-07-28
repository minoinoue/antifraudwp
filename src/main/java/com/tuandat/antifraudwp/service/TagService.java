package com.tuandat.antifraudwp.service;

import com.tuandat.antifraudwp.model.Tag;
import com.tuandat.antifraudwp.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
    public Optional<Tag> getTag(Long id) {
        return tagRepository.findById(id);
    }
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
} 