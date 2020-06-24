package com.example.redditclone.service.jwt;

import com.example.redditclone.exception.SpringRedditException;
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

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @Value("${pk.password}")
    private String pubKeyPassword;


    @PostConstruct
    public void init() {
        try {
            System.out.println("public key password: " + pubKeyPassword);
            keyStore = KeyStore.getInstance("JKS");
            InputStream jksAsStream = getClass().getResourceAsStream("/keystore/reddit-clone.jks");
            keyStore.load(jksAsStream, pubKeyPassword.toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new SpringRedditException("Exception occurred while loading keystore");
        }

    }

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }
    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("reddit-clone", pubKeyPassword.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
           throw new SpringRedditException("Exception occurred while retrieving public key from keystore");
        }
    }
}
