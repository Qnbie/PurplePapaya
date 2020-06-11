package PurplePapaya.controller;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PurplePapaya.dto.AuthenticationResponse;
import PurplePapaya.dto.LoginRequest;
import PurplePapaya.dto.RegisterRequest;
import PurplePapaya.exeption.PurplePapayaException;
import PurplePapaya.service.AuthService;
import io.jsonwebtoken.security.InvalidKeyException;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) throws PurplePapayaException {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registrarion Succesful", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) throws PurplePapayaException {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest)
            throws InvalidKeyException, PurplePapayaException {
        return authService.login(loginRequest);
    }
}