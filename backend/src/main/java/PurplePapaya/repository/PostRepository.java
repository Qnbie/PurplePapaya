package PurplePapaya.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PurplePapaya.model.Post;
import PurplePapaya.model.Subreddit;
import PurplePapaya.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findAllBySubreddit(Subreddit subreddit);

	Optional<Post> findByUser(User user);
}