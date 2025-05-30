package com.dqs.eventdrivensearch.queryDistribution.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class QueryReceived {

    @JsonProperty
    private String queryId;

    @JsonProperty
    private String tenant;

    @JsonProperty
    private String term;

    @JsonProperty
    private int beginYear;

    @JsonProperty
    private int endYear;

    @JsonProperty
    private LocalDateTime creationTime;

    public QueryReceived() {}

    public QueryReceived(String queryId, String tenant, String term, int beginYear, int endYear, LocalDateTime creationTime) {
        this.queryId = queryId;
        this.tenant = tenant;
        this.term = term;
        this.beginYear = beginYear;
        this.endYear = endYear;
        this.creationTime = creationTime;
    }

    public String queryId() {
        return queryId;
    }

    public String tenantId() {
        return tenant;
    }

    public String term() {
        return term;
    }

    public int beginYear() {
        return beginYear;
    }

    public int endYear() {
        return endYear;
    }

    public LocalDateTime creationTime() {
        return creationTime;
    }


    @Override
    public String toString() {
        return "QueryReceived{" +
                "queryId='" + queryId + '\'' +
                ", tenant='" + tenant + '\'' +
                ", term='" + term + '\'' +
                ", beginYear=" + beginYear +
                ", endYear=" + endYear +
                ", creationTime=" + creationTime +
                '}';
    }
}