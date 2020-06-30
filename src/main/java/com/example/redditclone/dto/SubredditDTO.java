package com.example.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SubredditDTO {
    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;
}
