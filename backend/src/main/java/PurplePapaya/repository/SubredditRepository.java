package PurplePapaya.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PurplePapaya.model.Subreddit;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

	Optional<Subreddit> findByName(String subredditName);
    
}