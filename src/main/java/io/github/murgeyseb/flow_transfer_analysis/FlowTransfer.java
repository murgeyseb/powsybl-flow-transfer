package io.github.murgeyseb.flow_transfer_analysis;

import java.util.Map;
import java.util.Objects;

public class FlowTransfer {
    private final String outage;
    private final double nFlow;
    private final Map<String, FlowCollection> flowCollections;

    public FlowTransfer(String outage, double nFlow, Map<String, FlowCollection> flowCollections) {
        this.outage = Objects.requireNonNull(outage);
        this.nFlow = nFlow;
        this.flowCollections = Objects.requireNonNull(flowCollections);
    }

    public String getOutage() {
        return outage;
    }

    public double getNFlow() {
        return nFlow;
    }

    public Map<String, FlowCollection> getFlowCollections() {
        return flowCollections;
    }
}
