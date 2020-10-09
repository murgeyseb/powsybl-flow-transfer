package io.github.murgeyseb.flow_transfer_analysis;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FlowTransferAnalysisInput {
    private final List<String> outages;

    public FlowTransferAnalysisInput(List<String> outages) {
        this.outages = Objects.requireNonNull(outages);
    }

    public List<String> outages() {
        return Collections.unmodifiableList(outages);
    }
}
