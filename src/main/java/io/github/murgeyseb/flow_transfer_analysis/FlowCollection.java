package io.github.murgeyseb.flow_transfer_analysis;

public class FlowCollection {
    private final double initialFlow;
    private final double finalFlow;

    public FlowCollection(double initialFlow, double finalFlow) {
        this.initialFlow = initialFlow;
        this.finalFlow = finalFlow;
    }

    public double getInitialFlow() {
        return initialFlow;
    }

    public double getFinalFlow() {
        return finalFlow;
    }
}
