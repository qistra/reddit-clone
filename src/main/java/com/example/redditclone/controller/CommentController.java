package com.example.redditclone.controller;

import com.example.redditclone.dto.CommentDTO;
import com.example.redditclone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity createComment(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.save(commentDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity getComment(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getComment(id));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByPost(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsByPost(id));
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByUser(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsByUser(username));
    }
}
