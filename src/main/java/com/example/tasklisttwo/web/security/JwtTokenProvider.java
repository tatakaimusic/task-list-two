package com.example.tasklisttwo.web.security;

import com.example.tasklisttwo.model.exception.AccessDeniedException;
import com.example.tasklisttwo.model.user.Role;
import com.example.tasklisttwo.model.user.User;
import com.example.tasklisttwo.service.impl.UserServiceImpl;
import com.example.tasklisttwo.service.props.JwtProperties;
import com.example.tasklisttwo.web.dto.auth.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final JwtUserDetailsService userDetailsService;

    private final UserServiceImpl userService;

    private Key key;

    @Autowired
    public JwtTokenProvider(JwtProperties jwtProperties, JwtUserDetailsService userDetailsService, UserServiceImpl userService) {
        this.jwtProperties = jwtProperties;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId, String username, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        claims.put("roles", resolveRoles(roles));
        Instant validity = Instant.now().plus(jwtProperties.getAccess(), ChronoUnit.HOURS);
        return Jwts
                .builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId, String username) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        Date now = new Date();
        Instant validity = Instant.now().plus(jwtProperties.getRefresh(), ChronoUnit.DAYS);
        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserTokens(String refreshToken) {
        JwtResponse jwtResponse = new JwtResponse();
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException();
        }
        Long id = Long.valueOf(getId(refreshToken));
        User user = userService.getById(id);
        String username = user.getUsername();
        jwtResponse.setId(id);
        jwtResponse.setUsername(username);
        jwtResponse.setAccessToken(createAccessToken(id, username, user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(id, username));
        return jwtResponse;
    }

    public boolean validateToken(String token) {
        Jws<Claims> claimsJws = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private String getId(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }

    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream().map(Enum::name).collect(Collectors.toList());
    }


}
