package com.step.planB_planner.model.exBody;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Daria Pevets
 **/
@Data
@AllArgsConstructor
@Builder
public class ExceptionBody {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String path;
}
