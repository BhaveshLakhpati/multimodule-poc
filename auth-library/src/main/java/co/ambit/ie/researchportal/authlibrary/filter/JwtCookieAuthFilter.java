package co.ambit.ie.researchportal.authlibrary.filter;

import co.ambit.ie.researchportal.authlibrary.properties.CustomSecurityProperties;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static co.ambit.ie.researchportal.authlibrary.util.HttpUtil.extractJwtFromCookie;

@Slf4j
public class JwtCookieAuthFilter extends OncePerRequestFilter {
    //    private final JwtDecoder jwtDecoder;
    private final List<String> excludedPaths;
    private final AntPathMatcher antPathMatcher;


    public JwtCookieAuthFilter(final CustomSecurityProperties securityProperties
//                               , final JwtDecoder jwtDecoder
    ) {
//        this.jwtDecoder = jwtDecoder;
        this.excludedPaths = securityProperties.getExcludedPaths();
        this.antPathMatcher = new AntPathMatcher();
    }

    @Override
    public boolean shouldNotFilter(@Nonnull final HttpServletRequest request) {
        return this.excludedPaths.stream()
                .anyMatch(excludedPath -> this.antPathMatcher
                        .match(excludedPath, request.getServletPath()));
    }

    @Override
    public void doFilterInternal(@Nonnull final HttpServletRequest request,
                                 @Nonnull final HttpServletResponse response,
                                 @Nonnull final FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractJwtFromCookie(request)
                .orElse("dummy");
        log.info("access-token: {}", accessToken);

        try {
//            Jwt jwt = this.jwtDecoder.decode(token);
//            String username = jwt.getSubject();
//            List<String> roles = jwt.getClaimsAsStringList("roles");
            String username = "dummy";
            List<String> roles = List.of("ADMIN");

            List<SimpleGrantedAuthority> authorities = Optional.ofNullable(roles)
                    .map(roleList -> roleList.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .toList())
                    .orElse(List.of());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                    null,
                    authorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (final Exception exception) {
            log.error("Error in authorization: ", exception);
        }

        filterChain.doFilter(request, response);
    }
}
