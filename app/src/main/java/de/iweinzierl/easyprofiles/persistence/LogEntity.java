package de.iweinzierl.easyprofiles.persistence;

import com.orm.dsl.Table;

import java.util.Date;

@Table
public class LogEntity {

    private Long id;

    private Date timestamp;

    private String level;
    private String message;
    private String stacktrace;

    public LogEntity() {}

    public LogEntity(Date timestamp, String level, String message, String stacktrace) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.stacktrace = stacktrace;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }
}
