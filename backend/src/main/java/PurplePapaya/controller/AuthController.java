package PurplePapaya.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PurplePapaya.dto.RegisterRequest;
import PurplePapaya.exeption.PurplePapayaException;
import PurplePapaya.service.AuthService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody RegisterRequest registerRequest) throws PurplePapayaException {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registrarion Succesful", HttpStatus.OK);
    }
}