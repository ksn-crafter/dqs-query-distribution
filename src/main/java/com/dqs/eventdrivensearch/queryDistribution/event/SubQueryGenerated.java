package com.dqs.eventdrivensearch.queryDistribution.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SubQueryGenerated {

    @JsonProperty
    private String subQueryId;

    @JsonProperty
    private String queryId;

    @JsonProperty
    private String tenant;

    @JsonProperty
    private List<String> filePaths;

    @JsonProperty
    private int totalSubQueries;

    public SubQueryGenerated() { }

    public SubQueryGenerated(String subQueryId, String queryId, String tenant, List<String> filePaths, int totalSubQueries) {
        this.subQueryId = subQueryId;
        this.queryId = queryId;
        this.tenant = tenant;
        this.filePaths = filePaths;
        this.totalSubQueries = totalSubQueries;
    }

    public String subQueryId() {
        return subQueryId;
    }

    public String queryId() {
        return queryId;
    }

    public String tenant() {
        return tenant;
    }

    public List<String> filePaths() {
        return filePaths;
    }

    public int totalSubQueries() {
        return totalSubQueries;
    }

    @Override
    public String toString() {
        return "SubQueryGenerated{" +
                "subQueryId='" + subQueryId + '\'' +
                ", queryId='" + queryId + '\'' +
                ", tenant='" + tenant + '\'' +
                ", totalSubQueries=" + totalSubQueries +
                '}';
    }
}
