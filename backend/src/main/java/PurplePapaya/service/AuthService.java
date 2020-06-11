package PurplePapaya.service;

import java.time.Instant;
import java.util.UUID;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import PurplePapaya.dto.RegisterRequest;
import PurplePapaya.exeption.PurplePapayaException;
import PurplePapaya.model.NotificationEmail;
import PurplePapaya.model.User;
import PurplePapaya.model.VerificationToken;
import PurplePapaya.repository.UserRepository;
import PurplePapaya.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Transactional
    public void signup(RegisterRequest registerRequest) throws PurplePapayaException {
        
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please activate your account!", user.getEmail(), 
        "Köszi, hogy regisztráltál a PurplePapayaára:3"+ 
        "az accountod aktiválásához kattints ide: " + 
        "http://localhost:8080/api/auth/accountverification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationTokenRepository.save(verificationToken); 
        return token;
    }
}