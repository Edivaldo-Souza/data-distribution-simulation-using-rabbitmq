package org.pratica3.commons;

import java.time.LocalDateTime;

public class MensagemDeServico{
    private LocalDateTime timestamp;
    private String service;
    private String status;
    private String server;
    private Integer cpu_usage;
    private Integer memory_usage;
    private Integer response_time;

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

    public Integer getCpu_usage() {
        return cpu_usage;
    }

    public void setCpu_usage(Integer cpu_usage) {
        this.cpu_usage = cpu_usage;
    }

    public Integer getMemory_usage() {
        return memory_usage;
    }

    public void setMemory_usage(Integer memory_usage) {
        this.memory_usage = memory_usage;
    }

    public Integer getResponse_time() {
        return response_time;
    }

    public void setResponse_time(Integer response_time) {
        this.response_time = response_time;
    }

    @Override
    public String toString() {
        return "{" +
                "timestamp=" + timestamp +
                ", service=" + service +
                ", status=" + status +
                ", server=" + server +
                ", cpu_usage=" + cpu_usage +
                ", memory_usage=" + memory_usage +
                ", response_time=" + response_time +
                '}';
    }

}
