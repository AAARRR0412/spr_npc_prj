package com.spr.socialtv.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spr.socialtv.dto.LoginRequestDto;
import com.spr.socialtv.entity.UserRoleEnum;
import com.spr.socialtv.jwt.JwtUtil;
import com.spr.socialtv.util.redis.TokenDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final RedisTemplate redisTemplate;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RedisTemplate redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
        setFilterProcessesUrl("/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        /*try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }*/

        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            Authentication authentication = getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
            UserRoleEnum role = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getRole();
            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenDto.TokenInfo tokenInfo = jwtUtil.generateToken(authentication, role);

            // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
            redisTemplate.opsForValue()
                    .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

            return authentication;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 인증성공
     * */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        TokenDto.TokenInfo token = jwtUtil.generateToken(authResult, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token.getAccessToken());
        response.getWriter().println(" 로그인을 성공하였습니다." + " (상태코드 : "
                + response.getStatus()
                + ")"
                + "\n access token : " + token.getAccessToken()
                + "\n refresh token : " + token.getRefreshToken()
        );
    }

    /**
     * 실패
     * */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(400);
        response.getWriter().println(" 회원을 찾을 수 없습니다." + " (상태코드 : " + response.getStatus() + ")");
    }

}