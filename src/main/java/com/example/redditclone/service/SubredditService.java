package com.example.redditclone.service;

import com.example.redditclone.dto.SubredditDTO;
import com.example.redditclone.exception.SpringRedditException;
import com.example.redditclone.mapper.SubredditMapper;
import com.example.redditclone.model.Subreddit;
import com.example.redditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    private final AuthService authService;

    @Transactional
    public SubredditDTO save(SubredditDTO subredditDTO) {
        Subreddit savedSubreddit = subredditRepository.save(
                subredditMapper.mapDtoToSubreddit(subredditDTO, authService.getCurrentUser()));
//        subredditDTO.setId(savedSubreddit.getId());

        return subredditMapper.mapSubredditToDto(savedSubreddit);
    }

    @Transactional(readOnly = true)
    public List<SubredditDTO> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    public SubredditDTO getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository
                .findById(id)
                .orElseThrow(() -> new SpringRedditException("Subreddit with id " + id + " not found"));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
