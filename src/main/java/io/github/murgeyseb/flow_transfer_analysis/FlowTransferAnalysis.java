package io.github.murgeyseb.flow_transfer_analysis;

import com.powsybl.contingency.ContingenciesProvider;
import com.powsybl.iidm.network.Network;
import com.powsybl.sensitivity.SensitivityAnalysis;
import com.powsybl.sensitivity.SensitivityAnalysisResult;
import com.powsybl.sensitivity.SensitivityFactorsProvider;
import com.powsybl.sensitivity.SensitivityValue;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FlowTransferAnalysis {
    public FlowTransferAnalysisResult run(Network network, FlowTransferAnalysisInput input) {
        SensitivityFactorsProvider sensitivityFactorsProvider = new FlowTransferSensitivityFactorsProvider();
        ContingenciesProvider contingenciesProvider = new FlowTransferContingenciesProvider(input);
        SensitivityAnalysisResult sensitivityAnalysisResult = SensitivityAnalysis.run(network, sensitivityFactorsProvider, contingenciesProvider);
        return buildResult(sensitivityAnalysisResult);
    }

    private FlowTransferAnalysisResult buildResult(SensitivityAnalysisResult sensitivityAnalysisResult) {
        Map<String, Double> nFlowsByBranch = flowsOnBranch(sensitivityAnalysisResult.getSensitivityValues());
        Map<String, FlowTransfer> flowTransfers = sensitivityAnalysisResult.getSensitivityValuesContingencies().entrySet()
                .stream()
                .map(entry -> buildFlowTransfer(entry.getKey(), nFlowsByBranch, flowsOnBranch(entry.getValue())))
                .collect(Collectors.toMap(FlowTransfer::getOutage, Function.identity()));
        return new FlowTransferAnalysisResult(flowTransfers);
    }

    private FlowTransfer buildFlowTransfer(String outage, Map<String, Double> nFlowsByBranch, Map<String, Double> outageFlowsByBranch) {
        Double nFlow = nFlowsByBranch.get(outage);
        if (nFlow == null) {
            // TODO log error
        }
        Map<String, FlowCollection> flowCollections = new TreeMap<>();
        nFlowsByBranch.forEach((branchId, nFlowBranchValue) -> {
            Double outageFlowBranchValue = outageFlowsByBranch.get(branchId);
            if (outageFlowBranchValue == null) {
                // TODO log error
            }
            flowCollections.put(branchId, new FlowCollection(nFlowBranchValue, outageFlowBranchValue));
        });
        return new FlowTransfer(outage, nFlow, flowCollections);
    }

    private Map<String, Double> flowsOnBranch(Collection<SensitivityValue> sensitivityValues) {
        return sensitivityValues.stream()
                .collect(Collectors.toMap(sensitivityValue -> sensitivityValue.getFactor().getFunction().getId(), SensitivityValue::getFunctionReference));
    }
}
