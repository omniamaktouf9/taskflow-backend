package com.taskflow.taskflow_backend.service;

import com.taskflow.taskflow_backend.model.Tag;
import com.taskflow.taskflow_backend.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag createTag(String nom) {
        return tagRepository.findByNom(nom).orElseGet(() -> {
            Tag tag = new Tag();
            tag.setNom(nom);
            return tagRepository.save(tag);
        });
    }
}