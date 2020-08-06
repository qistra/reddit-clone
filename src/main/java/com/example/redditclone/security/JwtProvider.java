package com.example.redditclone.security;

import com.example.redditclone.exception.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parser;
import static io.jsonwebtoken.Jwts.parserBuilder;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @Value("${pubKey.password}")
    private String pubKeyPassword;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationTime;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream jksAsStream = getClass().getResourceAsStream("/keystore/reddit-clone.jks");
            keyStore.load(jksAsStream, pubKeyPassword.toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new SpringRedditException("Exception occurred while loading keystore");
        }

    }

    public String generateJwtToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTime)))
                .compact();
    }

    public String generateJwtTokenByUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTime)))
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("reddit-clone", pubKeyPassword.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore");
        }
    }

    public boolean validateToken(String jwt) {
//        parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("reddit-clone").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore");
        }
    }

    public String getUsernameFromJwt(String jwt) {
        Claims claims = parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return claims.getSubject();
    }

    public Long getJwtExpirationTime() {
        return jwtExpirationTime;
    }
}
