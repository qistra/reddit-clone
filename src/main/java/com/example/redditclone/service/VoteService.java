package com.example.redditclone.service;

import com.example.redditclone.dto.VoteDTO;
import com.example.redditclone.exception.PostNotFoundException;
import com.example.redditclone.exception.SpringRedditException;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.Vote;
import com.example.redditclone.repository.PostRepository;
import com.example.redditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.redditclone.model.VoteType.DOWNVOTE;
import static com.example.redditclone.model.VoteType.UPVOTE;

@Service
@Slf4j
@AllArgsConstructor
public class VoteService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    public void vote(VoteDTO voteDTO) {
        Post post = postRepository.findById(voteDTO.getPostId())
                .orElseThrow(() -> new PostNotFoundException(voteDTO.getPostId().toString()));

        Vote vote = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser())
                .orElseThrow(() -> new SpringRedditException("Vote not found by Post with id " + post.getPostId() +
                        " and by User with username " + authService.getCurrentUser().getUsername()));

        if (vote.getVoteType().equals(voteDTO.getVoteType())) {
            throw new SpringRedditException("You have already " + voteDTO.getVoteType() + "d for this post");
        }

        if(UPVOTE.equals(voteDTO.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        }

        if(DOWNVOTE.equals(voteDTO.getVoteType())) {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(mapToVote(voteDTO, post));
        postRepository.save(post);

    }

    private Vote mapToVote(VoteDTO voteDTO, Post post) {
        return Vote.builder()
                .voteType(voteDTO.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
