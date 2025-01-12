package com.university.room.reservation.interceptor;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;

@Component
public class HeaderLocaleChangeInterceptor implements HandlerInterceptor {

    private static final String HEADER_NAME = "Accept-Language";
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String language = request.getHeader(HEADER_NAME);
        Locale locale;
        if (StringUtils.isNotBlank(language)) {
            locale = Locale.forLanguageTag(language);
        } else {
            locale = DEFAULT_LOCALE;
        }

        LocaleContextHolder.setLocale(locale);
        return true;
    }

}
