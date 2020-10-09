package io.github.murgeyseb.flow_transfer_analysis;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class FlowTransferAnalysisResult {
    private final Map<String, FlowTransfer> flowTransfersMap;

    public FlowTransferAnalysisResult(Map<String, FlowTransfer> flowTransfersMap) {
        this.flowTransfersMap = flowTransfersMap;
    }

    public Map<String, FlowTransfer> allFlowTransfers() {
        return Collections.unmodifiableMap(flowTransfersMap);
    }

    public FlowTransfer flowTransfer(String outage) {
        Objects.requireNonNull(outage);
        return flowTransfersMap.get(outage);
    }
}
