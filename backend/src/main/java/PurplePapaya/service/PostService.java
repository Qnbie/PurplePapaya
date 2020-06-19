package PurplePapaya.service;

import java.util.List;
import static java.util.stream.Collectors.toList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import PurplePapaya.dto.PostRequest;
import PurplePapaya.dto.PostResponse;
import PurplePapaya.exeption.PurplePapayaException;
import PurplePapaya.mapper.PostMapper;
import PurplePapaya.model.Post;
import PurplePapaya.model.Subreddit;
import PurplePapaya.model.User;
import PurplePapaya.repository.PostRepository;
import PurplePapaya.repository.SubredditRepository;
import PurplePapaya.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

	private final SubredditRepository subredditRepository;
	private final AuthService authService;
	private final PostMapper postMapper;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	public Post save(PostRequest postRequest) throws PurplePapayaException {
		Subreddit subreddit = subredditRepository.findByName(postRequest.getSubRedditName()).orElseThrow(()-> new PurplePapayaException("Subreddit not found"));
		User userDetail = authService.getCurrentUser();
	
		return postMapper.map(postRequest, subreddit, userDetail);
	}

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) throws PurplePapayaException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PurplePapayaException("Post not found with id = " + id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) throws PurplePapayaException {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new PurplePapayaException("Subreddit not found with thid id = " + subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) throws PurplePapayaException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new PurplePapayaException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

}
