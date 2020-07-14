package com.example.redditclone.mapper;

import com.example.redditclone.dto.SubredditDTO;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.Subreddit;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDTO mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDTO subredditDTO);
}
