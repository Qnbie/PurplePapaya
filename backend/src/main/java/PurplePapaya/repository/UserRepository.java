package PurplePapaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PurplePapaya.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}