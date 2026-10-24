package com.step.planB_planner.handler;

import com.step.planB_planner.annotation.Log;
import com.step.planB_planner.exception.IllegalTimeArgument;
import com.step.planB_planner.exception.TaskException;
import com.step.planB_planner.model.exceptionBody.ExceptionBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

/**
 * @author Daria Pevets
 **/
@Log
@RestControllerAdvice
public class GlobalExceptionHandler {

    private String getRequestPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

    @ExceptionHandler(IllegalTimeArgument.class)
    public ExceptionBody handleIllegalTimeArgument(IllegalTimeArgument exception, WebRequest request) {
        return ExceptionBody.builder()
                .timestamp(now())
                .status(BAD_REQUEST)
                .message(exception.getMessage())
                .path(getRequestPath(request))
                .build();
    }

    @ExceptionHandler(TaskException.class)
    public ExceptionBody handleTaskException(TaskException exception, WebRequest request) {
        return ExceptionBody.builder()
                .timestamp(now())
                .status(BAD_REQUEST)
                .message(exception.getMessage())
                .path(getRequestPath(request))
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionBody handleIllegalArgumentException(IllegalArgumentException exception, WebRequest request) {
        return ExceptionBody.builder()
                .timestamp(now())
                .status(NOT_FOUND)
                .message(exception.getMessage())
                .path(getRequestPath(request))
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ExceptionBody handleException(WebRequest request) {
        return ExceptionBody.builder()
                .timestamp(now())
                .status(INTERNAL_SERVER_ERROR)
                .message("Ну вот! Все сломал!")
                .path(getRequestPath(request))
                .build();
    }
}
