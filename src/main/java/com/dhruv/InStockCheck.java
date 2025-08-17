package com.dhruv;

import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class InStockCheck {
    private final String url;
    private LocalDateTime timeChecked;
    private Status status;

    public InStockCheck(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getTimeChecked() {
        return timeChecked;
    }

    public void setTimeChecked(LocalDateTime timeChecked) {
        this.timeChecked = timeChecked;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(InStockCheck.class).append("url", url).append("status", status).append("checked at:", timeChecked).toString();
    }

}
