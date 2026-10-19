package com.step.planB_planner.handler;

import com.step.planB_planner.exception.IllegalTimeArgument;
import com.step.planB_planner.model.exBody.ExceptionBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author Daria Pevets
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    private String getRequestPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

    @ExceptionHandler(IllegalTimeArgument.class)
    public ExceptionBody handleInvalidMarkException
            (IllegalTimeArgument exception, WebRequest request) {
        return ExceptionBody.builder()
                .timestamp(now())
                .status(BAD_REQUEST)
                .message(exception.getMessage())
                .path(getRequestPath(request))
                .build();
    }


}
