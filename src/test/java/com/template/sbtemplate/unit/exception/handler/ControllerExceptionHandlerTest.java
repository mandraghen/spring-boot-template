package com.template.sbtemplate.unit.exception.handler;

import com.template.sbtemplate.exception.handler.ControllerExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerExceptionHandlerTest {

    private final ControllerExceptionHandler handler = new ControllerExceptionHandler();

    @Test
    void handleIllegalArgument_returnsBadRequestWithMessage() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid input");
        ResponseEntity<String> response = handler.handleIllegalArgument(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("The request contains errors: Invalid input");
    }
}