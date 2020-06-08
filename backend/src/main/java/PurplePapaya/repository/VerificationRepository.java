package PurplePapaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PurplePapaya.model.VerificationToken;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationToken, Long> {
    
}