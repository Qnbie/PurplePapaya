package PurplePapaya.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import PurplePapaya.dto.VoteDto;
import PurplePapaya.exeption.PurplePapayaException;
import PurplePapaya.model.Post;
import PurplePapaya.model.Vote;
import PurplePapaya.model.VoteType;
import PurplePapaya.repository.PostRepository;
import PurplePapaya.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import static PurplePapaya.model.VoteType.UPVOTE;
import static PurplePapaya.model.VoteType.DOWNVOTE;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

	public void vote(VoteDto voteDto) throws PurplePapayaException {
        Post post = postRepository.findById(voteDto.getPostId()).orElseThrow(()->new PurplePapayaException("Post not find with id = " + voteDto.getPostId()));

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if(voteByPostAndUser.isPresent() && 
            voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
                throw new PurplePapayaException("You have alredy vote " + voteDto.getVoteType().toString());
        }
        if(UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount()+1);
        } else {
            post.setVoteCount(post.getVoteCount()-1);
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }
    
    private Vote mapToVote(VoteDto voteDto, Post post) throws PurplePapayaException {
        return Vote.builder()
            .voteType(voteDto.getVoteType())
            .post(post)
            .user(authService.getCurrentUser())
            .build();
    }

}
