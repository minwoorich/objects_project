package com.objects.marketbridge.common.security.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.objects.marketbridge.common.security.constants.SecurityConst.LOCATION_FILTER;

@Getter
@ToString
public class ErrRes {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final String location;
    private final String timeStamp;

    @Builder(access = AccessLevel.PRIVATE)
    public ErrRes(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.location = LOCATION_FILTER;
        this.timeStamp = (LocalDateTime.now()).toString();
    }

    public static ErrRes of(HttpStatus httpStatus, String message, String uri) {
        return ErrRes.builder()
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(message)
                .path(uri)
                .build();
    }
    public String convertToJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
