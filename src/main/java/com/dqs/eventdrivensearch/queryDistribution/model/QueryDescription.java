package com.dqs.eventdrivensearch.queryDistribution.model;

import com.dqs.eventdrivensearch.queryDistribution.event.QueryReceived;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "queryDescription")
public class QueryDescription {

    @Id
    private UUID queryId;
    private String tenantId;
    private String term;
    private int yearStart;
    private int yearEnd;
    private QueryStatus status;
    private LocalDateTime creationTime;
    private LocalDateTime completionTime;

    public QueryDescription() {}

    public QueryDescription(QueryReceived event) {
        this.queryId = UUID.fromString(event.queryId());
        this.tenantId = event.tenantId().toLowerCase();
        this.term = event.term();
        this.yearStart = event.yearStart();
        this.yearEnd = event.yearEnd();
        this.creationTime = event.creationTime();
        this.status = QueryStatus.Acknowledged;
    }

    public UUID queryId() {
        return queryId;
    }

    public String tenantId() {
        return tenantId;
    }

    public String term() {
        return term;
    }

    public int yearStart() {
        return yearStart;
    }

    public int yearEnd() {
        return yearEnd;
    }

    public QueryStatus status() {
        return status;
    }

    public LocalDateTime creationTime() {
        return creationTime;
    }

    public LocalDateTime completionTime() {
        return completionTime;
    }
}
