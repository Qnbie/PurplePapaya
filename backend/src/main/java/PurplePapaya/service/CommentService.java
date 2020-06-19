package PurplePapaya.service;

import org.springframework.stereotype.Service;
import static java.util.stream.Collectors.toList;

import java.util.List;

import PurplePapaya.dto.CommentsDto;
import PurplePapaya.exeption.PurplePapayaException;
import PurplePapaya.mapper.CommentMapper;
import PurplePapaya.model.Comment;
import PurplePapaya.model.NotificationEmail;
import PurplePapaya.model.Post;
import PurplePapaya.model.User;
import PurplePapaya.repository.CommentRepository;
import PurplePapaya.repository.PostRepository;
import PurplePapaya.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) throws PurplePapayaException {
        Post post = postRepository.findById(commentsDto.getId())
            .orElseThrow(()->new PurplePapayaException("Post not found with id = " + commentsDto.getId()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + post.getUrl());
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) throws PurplePapayaException {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

	public List<CommentsDto> getAllCommentsForPost(Long postId) throws PurplePapayaException {
        Post post = postRepository.findById(postId).orElseThrow(()->new PurplePapayaException("Post not finde with id = " + postId));
        return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto)
                .collect(toList());
    }
    
    public List<CommentsDto> getAllCommentsForUser(String userName) throws PurplePapayaException {
        User user = userRepository.findByUsername(userName).orElseThrow(()->new PurplePapayaException("User not finde with name = " + userName));
        return commentRepository.findByUser(user).stream().map(commentMapper::mapToDto)
            .collect(toList());
    }
}