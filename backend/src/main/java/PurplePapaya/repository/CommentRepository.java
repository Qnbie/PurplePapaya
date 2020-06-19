package PurplePapaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PurplePapaya.model.Comment;
import PurplePapaya.model.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	Object findByPost(Post post);
    
}