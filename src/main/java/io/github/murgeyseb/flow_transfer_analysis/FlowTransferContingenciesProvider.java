package io.github.murgeyseb.flow_transfer_analysis;

import com.powsybl.contingency.BranchContingency;
import com.powsybl.contingency.ContingenciesProvider;
import com.powsybl.contingency.Contingency;
import com.powsybl.iidm.network.Network;

import java.util.List;
import java.util.stream.Collectors;

public class FlowTransferContingenciesProvider implements ContingenciesProvider {
    private final FlowTransferAnalysisInput flowTransferAnalysisInput;

    public FlowTransferContingenciesProvider(FlowTransferAnalysisInput flowTransferAnalysisInput) {
        this.flowTransferAnalysisInput = flowTransferAnalysisInput;
    }

    @Override
    public List<Contingency> getContingencies(Network network) {
        return flowTransferAnalysisInput.outages().stream()
                .map(BranchContingency::new)
                .map(brc -> new Contingency(brc.getId(), brc))
                .collect(Collectors.toList());
    }
}
