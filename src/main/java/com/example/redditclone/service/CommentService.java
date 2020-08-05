package com.example.redditclone.service;

import com.example.redditclone.dto.CommentDTO;
import com.example.redditclone.exception.PostNotFoundException;
import com.example.redditclone.mapper.CommentMapper;
import com.example.redditclone.model.Comment;
import com.example.redditclone.model.NotificationEmail;
import com.example.redditclone.model.Post;
import com.example.redditclone.repository.CommentRepository;
import com.example.redditclone.repository.PostRepository;
import com.example.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;

    public Comment save(CommentDTO commentDTO) {
        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentDTO.getPostId().toString()));

        Comment comment = commentMapper.mapDtoToComment(commentDTO, post, authService.getCurrentUser());
        Comment savedComment = commentRepository.save(comment);

        String message = post.getUser().getUsername() + " posted a comment on your post";
//        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post ");
        mailService.sendMail(new NotificationEmail(
                post.getUser().getUsername() + " commented on your post",
                post.getUser().getEmail(),
                message));

        return savedComment;

    }
}
