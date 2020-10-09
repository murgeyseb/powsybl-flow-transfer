package io.github.murgeyseb.flow_transfer_analysis;

import com.powsybl.iidm.network.Network;
import org.junit.jupiter.api.Test;

public class FlowTransferAnalysisTest {
    @Test
    public void flowTransferTest() {
        Network network = ExampleGenerator.network();
        FlowTransferAnalysisInput input = ExampleGenerator.input();

        FlowTransferAnalysis flowTransferAnalysis = new FlowTransferAnalysis();
        FlowTransferAnalysisResult result = flowTransferAnalysis.run(network, input);

        result.allFlowTransfers().forEach((outage, flowTransfer) -> {
            System.out.println(String.format("Analysing transfer flows for outage '%s'", outage));
            System.out.println(String.format("Flow before outage: %f MW", flowTransfer.getNFlow()));
            flowTransfer.getFlowCollections().forEach((branch, flowCollection) -> {
                if (branch.equals(outage)) {
                    System.out.println(String.format("  %s, outage analysed", branch));
                } else {
                    System.out.println(String.format("  %s, init: %f MW, final: %f MW, transfer rate: %.2f%%", branch, flowCollection.getInitialFlow(), flowCollection.getFinalFlow(), computeRate(flowTransfer.getNFlow(), flowCollection)));
                }
            });
        });
    }

    private double computeRate(double initialFlow, FlowCollection flowCollection) {
        return (initialFlow - (flowCollection.getFinalFlow() - flowCollection.getInitialFlow())) / 100;
    }
}