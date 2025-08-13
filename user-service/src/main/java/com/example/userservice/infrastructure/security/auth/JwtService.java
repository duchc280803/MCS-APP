package com.example.userservice.infrastructure.security.auth;

import com.example.userservice.domain.entity.user.TokenExpired;
import com.example.userservice.infrastructure.repository.TokenExpiredRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final int REFRESH_TOKEN_LENGTH = 64;

    @Value("${mcs.jwt.privateKey}")
    private String privateKey;

    @Value("${mcs.jwt.publicKey}")
    private String publicKey;

    private final TokenExpiredRepository tokenExpiredRepository;

    public String extractUsername(String token) throws Exception {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws Exception {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) throws Exception {
        return Jwts.builder().claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1 day
                .signWith(getPrivateKeyFromString(privateKey), Jwts.SIG.RS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) throws Exception {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) throws Exception {
        List<TokenExpired> tokenAccessDeniedExisted = tokenExpiredRepository.findAllByAccessToken(token);
        if (!tokenAccessDeniedExisted.isEmpty()) {
            return true;
        }
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) throws Exception{
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetail userDetail) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetail.getLoginId());
        claims.put("username", userDetail.getUsername());
        claims.put("role", userDetail.getRole());
        return generateToken(claims, userDetail);
    }

    private Claims extractAllClaims(String token) throws Exception {
        return Jwts.parser()
                .verifyWith(getPublicKeyFromString(publicKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Gets the private key from string.
     *
     * @param privateKeyStr the private key str
     * @return the private key from string
     * @throws Exception the exception
     */
    private PrivateKey getPrivateKeyFromString(String privateKeyStr) throws Exception {
        byte[] privateKeyBytes = DatatypeConverter.parseBase64Binary(privateKeyStr);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * Gets the public key from string.
     *
     * @param publicKeyStr the public key str
     * @return the public key from string
     * @throws Exception the exception
     */
    private PublicKey getPublicKeyFromString(String publicKeyStr) throws Exception {
        byte[] publicKeyBytes = DatatypeConverter.parseBase64Binary(publicKeyStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public String generateRefreshToken() {
        return RandomStringUtils.randomAlphanumeric(REFRESH_TOKEN_LENGTH);
    }

}
