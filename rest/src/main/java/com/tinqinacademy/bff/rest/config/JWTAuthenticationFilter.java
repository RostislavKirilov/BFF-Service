package com.tinqinacademy.bff.rest.config;

import com.tinqinacademy.authentication.api.getrole.input.GetRoleInput;
import com.tinqinacademy.authentication.api.getrole.output.GetRoleOutput;
import com.tinqinacademy.authentication.api.getusername.input.GetUsernameInput;
import com.tinqinacademy.authentication.api.getusername.output.GetUsernameOutput;
import com.tinqinacademy.authentication.api.operations.validatetoken.input.ValidateTokenInput;
import com.tinqinacademy.authentication.restexport.RestExportValidateToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final RestExportValidateToken restExportValidateToken;

    @Override
    protected void doFilterInternal ( @NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                      @NotNull FilterChain filterChain ) throws ServletException, IOException {

        String token = getTokenFromRequest(request);
        ValidateTokenInput input = ValidateTokenInput.builder().token(token).build();

        if (StringUtils.hasText(token) && restExportValidateToken.validateToken(input)) {

            GetUsernameInput getUsernameInput = GetUsernameInput.builder().token(token).build();
            GetRoleInput getRoleInput = GetRoleInput.builder().token(token).build();
           GetUsernameOutput username = restExportValidateToken.getUsernameFromToken(getUsernameInput);
            GetRoleOutput role = restExportValidateToken.getRoleFromToken(getRoleInput);

            CustomUser userDetails = CustomUser.builder()
                    .username(username.getUsername())
                    .role(role.getRole())
                    .build();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest ( HttpServletRequest request ) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}