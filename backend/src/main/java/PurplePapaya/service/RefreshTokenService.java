package PurplePapaya.service;

import PurplePapaya.exeption.PurplePapayaException;
import PurplePapaya.model.RefreshToken;
import PurplePapaya.model.VerificationToken;
import PurplePapaya.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken() {
        final RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    void validateRefreshToken(final String token) throws PurplePapayaException {
        refreshTokenRepository.findByToken(token)
                    .orElseThrow(() -> new PurplePapayaException("Invalid refresh Token"));
    }

    public void deleteRefreshToken(final String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}