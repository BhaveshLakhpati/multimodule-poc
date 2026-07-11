package co.ambit.ie.researchportal.authlibrary.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class HttpUtil {
    public static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";

    private HttpUtil() {

    }

    public static Optional<String> extractJwtFromCookie(final HttpServletRequest request) {
        if (Objects.nonNull(request.getCookies())) {
            for (final Cookie cookie : request.getCookies()) {
                if (ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    return Optional.ofNullable(cookie.getValue());
                }
            }
        }

        return Optional.empty();
    }

    public static Map<String, Object> extractHeaders(final HttpServletRequestWrapper cachedRequest) {
        return Collections.list(cachedRequest.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(Function.identity(), cachedRequest::getHeader));
    }
}
