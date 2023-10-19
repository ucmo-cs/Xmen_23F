package edu.ucmo.cbbackend.service;



import edu.ucmo.cbbackend.model.User;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;


// https://www.danvega.dev/blog/2022/09/06/spring-security-jwt/
@Service
public class TokenService {

    private final JwtEncoder encoder;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(TokenService.class);
    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        String scope = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(user.getUsername())
                .claim("scope", scope)
                .build();

        logger.info("claims: {}", scope);
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}


