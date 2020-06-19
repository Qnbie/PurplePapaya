package PurplePapaya.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import PurplePapaya.dto.SubredditDto;
import PurplePapaya.model.Post;
import PurplePapaya.model.Subreddit;

@Mapper(componentModel = "spring")
public interface SubredditMapper {
    
    @Mapping(target = "numberOfPost", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPost){return numberOfPost.size();}

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}