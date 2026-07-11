package co.ambit.ie.researchportal.authlibrary.filter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

import static co.ambit.ie.researchportal.authlibrary.util.HttpUtil.extractHeaders;

@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(@Nonnull final HttpServletRequest request,
                                 @Nonnull final HttpServletResponse response,
                                 @Nonnull final FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(request, (5 * 1024 * 1024)); // 5MB

        try {
            log.info(">>> REQUEST method: {}, URI: {}, query-string: {}, headers: {}, body: {}",
                    cachedRequest.getMethod(),
                    cachedRequest.getRequestURI(),
                    request.getQueryString(),
                    extractHeaders(cachedRequest),
                    cachedRequest.getContentAsString());

            filterChain.doFilter(request, response);
        } finally {
            log.info("<<< RESPONSE method: {}, URI:{}, status: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus());
        }
    }
}
