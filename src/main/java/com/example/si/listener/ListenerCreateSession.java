package com.example.si.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class ListenerCreateSession implements HttpSessionListener {
    private static final String LOG_MASSAGE = "has been created {} session ID{}";

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.info(LOG_MASSAGE, se.getSession().getId(), new Date());
    }
}
