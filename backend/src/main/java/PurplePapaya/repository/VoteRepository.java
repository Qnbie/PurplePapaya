package PurplePapaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PurplePapaya.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    
}