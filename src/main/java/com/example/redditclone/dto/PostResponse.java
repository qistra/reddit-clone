package com.example.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String subredditName;
    private String postName;
    private String url;
    private String description;
    private String userName;
    private Integer commentCount;
    private Integer voteCount;
    private String duration;
}
