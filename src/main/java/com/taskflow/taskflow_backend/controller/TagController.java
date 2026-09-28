package com.taskflow.taskflow_backend.controller;

import com.taskflow.taskflow_backend.model.Tag;
import com.taskflow.taskflow_backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @PostMapping
    public Tag createTag(@RequestBody Map<String, String> body) {
        return tagService.createTag(body.get("nom"));
    }
}