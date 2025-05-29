package com.dqs.eventdrivensearch.queryDistribution.model;

import com.dqs.eventdrivensearch.queryDistribution.event.QueryReceived;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "query_descriptions")
public class QueryDescription {

    @Id
    private String queryId;
    private String tenant;
    private String term;
    private int yearStart;
    private int yearEnd;
    private QueryStatus status;
    private LocalDateTime creationTime;
    private LocalDateTime completionTime;

    public QueryDescription() {}

    public QueryDescription(QueryReceived event) {
        this.queryId = event.queryId();
        this.tenant = event.tenantId().toLowerCase();
        this.term = event.term();
        this.yearStart = event.yearStart();
        this.yearEnd = event.yearEnd();
        this.creationTime = event.creationTime();
        this.status = QueryStatus.Acknowledged;
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
