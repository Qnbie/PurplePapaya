package PurplePapaya.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import PurplePapaya.dto.AuthenticationResponse;
import PurplePapaya.dto.LoginRequest;
import PurplePapaya.dto.RegisterRequest;
import PurplePapaya.exeption.PurplePapayaException;
import PurplePapaya.model.NotificationEmail;
import PurplePapaya.model.User;
import PurplePapaya.model.VerificationToken;
import PurplePapaya.repository.UserRepository;
import PurplePapaya.repository.VerificationTokenRepository;
import PurplePapaya.security.JWTProvider;
import io.jsonwebtoken.security.InvalidKeyException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

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
                "Köszi, hogy regisztráltál a PurplePapayaára:3" + "az accountod aktiválásához kattints ide: "
                        + "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) throws PurplePapayaException {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new PurplePapayaException("Invalid Token"));
        fetchUserEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserEnable(VerificationToken verificationToken) throws PurplePapayaException {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new PurplePapayaException("User not found with name " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) throws InvalidKeyException, PurplePapayaException {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getUsername());
	}

	@Transactional(readOnly = true)
    public User getCurrentUser() throws PurplePapayaException {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new PurplePapayaException("User name not found - " + principal.getUsername()));
    }

	public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}