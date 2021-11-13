package com.oh.register.config;

import com.oh.register.exception.RegisterException;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class BindingErrorHandler {
    public void bindingResult(BindingResult bindingResult, String logMessage, Logger logger) {
        AtomicReference<String> sumMessage = new AtomicReference<>("");
        if (bindingResult.hasErrors()) {
            logger.error(logMessage + bindingResult.getErrorCount());
            bindingResult.getAllErrors().forEach(error -> {
                String message = "Object name:" + error.getObjectName() + ", error code:" + error.getCode() + ", error message:" + error.getDefaultMessage();
                sumMessage.set(sumMessage + message + "\n");
            });
            throw new RegisterException(sumMessage.get());
        }
    }
}
