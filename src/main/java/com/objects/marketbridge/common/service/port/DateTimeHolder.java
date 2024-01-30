package com.objects.marketbridge.common.service.port;

import com.objects.marketbridge.common.domain.BaseEntity;

import java.time.LocalDateTime;

public interface DateTimeHolder {

    LocalDateTime getTimeNow();

    LocalDateTime getCreateTime(BaseEntity entity);

    LocalDateTime getUpdateTime(BaseEntity entity);

    LocalDateTime getDeleteTime(BaseEntity entity);
}
