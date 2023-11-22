package com.manager.social_network.common.constan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessGeneral {
    @Autowired
    private static MessageSource messageSource;

    protected  static String getMess(String code) {
        assert messageSource != null;
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
