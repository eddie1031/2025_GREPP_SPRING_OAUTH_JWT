package io.eddie.backend.member.app;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenProvider {

//    @Value("${custom.jwt.validation.exp}")
//    private String exp;

    public String issue() {

        String token = Jwts.builder()
                .subject("hello, World!")
                .claim("spring", "easy")
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 0L))
                .signWith(Keys.hmacShaKeyFor("A5A10BC17B4E653C78BDD55FFFD9DEE3BE5790CD6F3F64DC1E82E791F9821C17".getBytes())
                        , Jwts.SIG.HS256)
                .compact();

        return token;
    }


}
