package com.objects.marketbridge.common.infra;

import com.objects.marketbridge.common.service.port.DateTimeHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public abstract class SystemDataTimeHolder implements DateTimeHolder {

    @Override
    public LocalDateTime getTimeNow() {
        return LocalDateTime.now();
    }

}
