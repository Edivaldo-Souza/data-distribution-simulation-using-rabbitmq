package org.pratica3.commons;

import java.time.LocalDateTime;

public class OrdemDeServico{
    private LocalDateTime timestamp;
    private String service;
    private String status;
    private String server;
    private String problem;
    private String action_required;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getAction_required() {
        return action_required;
    }

    public void setAction_required(String action_required) {
        this.action_required = action_required;
    }

    @Override
    public String toString() {
        return "{" +
                "timestamp=" + timestamp +
                ", service=" + service +
                ", status=" + status +
                ", server=" + server +
                ", problem=" + problem +
                ", action_required=" + action_required +
                '}';
    }
}
