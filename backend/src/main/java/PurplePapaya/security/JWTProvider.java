package PurplePapaya.security;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import PurplePapaya.exeption.PurplePapayaException;
import PurplePapaya.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;

@Service

public class JWTProvider {

    private KeyStore keyStore;

    @PostConstruct
    public void init() throws PurplePapayaException {
        try {
            keyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (Exception e) {
            throw new PurplePapayaException("Exception occurred while loading keystore");
        }
    }

    public String generateToken(Authentication authentication) throws InvalidKeyException, PurplePapayaException {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder().setSubject(principal.getUsername()).signWith(getPrivateKey()).compact();
    }

    private Key getPrivateKey() throws PurplePapayaException {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        } catch (Exception e) {
            throw new PurplePapayaException("Exception occurred while retrieving key from keystore.");
        }
    }

    public boolean validateToken(String jwt) throws SignatureException, ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, IllegalArgumentException, PurplePapayaException {
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJwt(jwt);
        return true;
    }

    private PublicKey getPublicKey() throws PurplePapayaException {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (Exception e) {
            throw new PurplePapayaException("Exception occurred while certificate key from keystore.");
        }
    }

    public String getUserFromJwt(String token) throws SignatureException, ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, IllegalArgumentException, PurplePapayaException {
        Claims claims = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJwt(token).getBody();
        return claims.getSubject();
    }
}