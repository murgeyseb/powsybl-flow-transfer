package io.github.murgeyseb.flow_transfer_analysis;

import com.google.auto.service.AutoService;
import com.powsybl.computation.ComputationManager;
import com.powsybl.contingency.ContingenciesProvider;
import com.powsybl.contingency.Contingency;
import com.powsybl.iidm.network.*;
import com.powsybl.sensitivity.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Test case is a 4 nodes network, with 4 countries.
 *
 *       FR   (+100 MW)       BE  (0 MW)
 *          + ------------ +
 *          |              |
 *          |              |
 *          |              |
 *          + ------------ +
 *       DE   (0 MW)          NL  (-100 MW)
 *
 * All lines have same impedance.
 * Two outages are simulated:
 *      - loss of FR-BE interconnection line
 *      - loss of FR-DE interconnection line.
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public final class ExampleGenerator {

    private ExampleGenerator() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    public static Network network() {
        Network network = Network.create("Test", "code");
        Substation substationFr = network.newSubstation()
                .setId("Substation FR")
                .setName("Substation FR")
                .setCountry(Country.FR)
                .add();
        VoltageLevel voltageLevelFr = substationFr.newVoltageLevel()
                .setId("Voltage level FR")
                .setName("Voltage level FR")
                .setNominalV(400)
                .setTopologyKind(TopologyKind.BUS_BREAKER)
                .setLowVoltageLimit(300)
                .setHighVoltageLimit(500)
                .add();
        voltageLevelFr.getBusBreakerView()
                .newBus()
                .setId("Bus FR")
                .setName("Bus FR")
                .add();
        voltageLevelFr.newGenerator()
                .setId("Generator FR")
                .setName("Generator FR")
                .setBus("Bus FR")
                .setEnergySource(EnergySource.OTHER)
                .setMinP(1000)
                .setMaxP(2000)
                .setRatedS(100)
                .setTargetP(1600)
                .setTargetV(400)
                .setVoltageRegulatorOn(true)
                .add();
        voltageLevelFr.newLoad()
                .setId("Load FR")
                .setName("Load FR")
                .setBus("Bus FR")
                .setLoadType(LoadType.UNDEFINED)
                .setP0(1500)
                .setQ0(0)
                .add();

        Substation substationBe = network.newSubstation()
                .setId("Substation BE")
                .setName("Substation BE")
                .setCountry(Country.BE)
                .add();
        VoltageLevel voltageLevelBe = substationBe.newVoltageLevel()
                .setId("Voltage level BE")
                .setName("Voltage level BE")
                .setNominalV(400)
                .setTopologyKind(TopologyKind.BUS_BREAKER)
                .setLowVoltageLimit(300)
                .setHighVoltageLimit(500)
                .add();
        voltageLevelBe.getBusBreakerView()
                .newBus()
                .setId("Bus BE")
                .setName("Bus BE")
                .add();
        voltageLevelBe.newGenerator()
                .setId("Generator BE")
                .setName("Generator BE")
                .setBus("Bus BE")
                .setEnergySource(EnergySource.OTHER)
                .setMinP(1000)
                .setMaxP(2000)
                .setRatedS(100)
                .setTargetP(1500)
                .setTargetV(400)
                .setVoltageRegulatorOn(true)
                .add();
        voltageLevelBe.newLoad()
                .setId("Load BE")
                .setName("Load BE")
                .setBus("Bus BE")
                .setLoadType(LoadType.UNDEFINED)
                .setP0(1500)
                .setQ0(0)
                .add();

        Substation substationDe = network.newSubstation()
                .setId("Substation DE")
                .setName("Substation DE")
                .setCountry(Country.DE)
                .add();
        VoltageLevel voltageLevelDe = substationDe.newVoltageLevel()
                .setId("Voltage level DE")
                .setName("Voltage level DE")
                .setNominalV(400)
                .setTopologyKind(TopologyKind.BUS_BREAKER)
                .setLowVoltageLimit(300)
                .setHighVoltageLimit(500)
                .add();
        voltageLevelDe.getBusBreakerView()
                .newBus()
                .setId("Bus DE")
                .setName("Bus DE")
                .add();
        voltageLevelDe.newGenerator()
                .setId("Generator DE")
                .setName("Generator DE")
                .setBus("Bus DE")
                .setEnergySource(EnergySource.OTHER)
                .setMinP(1000)
                .setMaxP(2000)
                .setRatedS(100)
                .setTargetP(1500)
                .setTargetV(400)
                .setVoltageRegulatorOn(true)
                .add();
        voltageLevelDe.newLoad()
                .setId("Load DE")
                .setName("Load DE")
                .setBus("Bus DE")
                .setLoadType(LoadType.UNDEFINED)
                .setP0(1500)
                .setQ0(0)
                .add();

        Substation substationNl = network.newSubstation()
                .setId("Substation NL")
                .setName("Substation NL")
                .setCountry(Country.NL)
                .add();
        VoltageLevel voltageLevelNl = substationNl.newVoltageLevel()
                .setId("Voltage level NL")
                .setName("Voltage level NL")
                .setNominalV(400)
                .setTopologyKind(TopologyKind.BUS_BREAKER)
                .setLowVoltageLimit(300)
                .setHighVoltageLimit(500)
                .add();
        voltageLevelNl.getBusBreakerView()
                .newBus()
                .setId("Bus NL")
                .setName("Bus NL")
                .add();
        voltageLevelNl.newGenerator()
                .setId("Generator NL")
                .setName("Generator NL")
                .setBus("Bus NL")
                .setEnergySource(EnergySource.OTHER)
                .setMinP(1000)
                .setMaxP(2000)
                .setRatedS(100)
                .setTargetP(1500)
                .setTargetV(400)
                .setVoltageRegulatorOn(true)
                .add();
        voltageLevelNl.newLoad()
                .setId("Load NL")
                .setName("Load NL")
                .setBus("Bus NL")
                .setLoadType(LoadType.UNDEFINED)
                .setP0(1600)
                .setQ0(0)
                .add();

        network.newLine()
                .setId("FR-BE")
                .setName("FR-BE")
                .setVoltageLevel1("Voltage level FR")
                .setVoltageLevel2("Voltage level BE")
                .setBus1("Bus FR")
                .setBus2("Bus BE")
                .setR(0)
                .setX(5)
                .setB1(0)
                .setB2(0)
                .setG1(0)
                .setG2(0)
                .add();
        network.newLine()
                .setId("FR-DE")
                .setName("FR-DE")
                .setVoltageLevel1("Voltage level FR")
                .setVoltageLevel2("Voltage level DE")
                .setBus1("Bus FR")
                .setBus2("Bus DE")
                .setR(0)
                .setX(5)
                .setB1(0)
                .setB2(0)
                .setG1(0)
                .setG2(0)
                .add();
        network.newLine()
                .setId("BE-NL")
                .setName("BE-NL")
                .setVoltageLevel1("Voltage level BE")
                .setVoltageLevel2("Voltage level NL")
                .setBus1("Bus BE")
                .setBus2("Bus NL")
                .setR(0)
                .setX(5)
                .setB1(0)
                .setB2(0)
                .setG1(0)
                .setG2(0)
                .add();
        network.newLine()
                .setId("DE-NL")
                .setName("DE-NL")
                .setVoltageLevel1("Voltage level DE")
                .setVoltageLevel2("Voltage level NL")
                .setBus1("Bus DE")
                .setBus2("Bus NL")
                .setR(0)
                .setX(5)
                .setB1(0)
                .setB2(0)
                .setG1(0)
                .setG2(0)
                .add();
        return network;
    }

    public static FlowTransferAnalysisInput input() {
        return new FlowTransferAnalysisInput(Arrays.asList("FR-BE", "FR-DE"));
    }

    @AutoService(SensitivityAnalysisProvider.class)
    public static class SensitivityAnalysisProviderMock implements SensitivityAnalysisProvider {
        private static final double FAKE_SENSITIVITY_VALUE = Double.NaN;
        private static final double FAKE_VARIABLE_REFERENCE_VALUE = Double.NaN;

        @Override
        public CompletableFuture<SensitivityAnalysisResult> run(Network network, String s, SensitivityFactorsProvider sensitivityFactorsProvider, ContingenciesProvider contingenciesProvider, SensitivityAnalysisParameters sensitivityAnalysisParameters, ComputationManager computationManager) {
            List<SensitivityFactor> factors = sensitivityFactorsProvider.getFactors(network);
            List<Contingency> contingencies = contingenciesProvider.getContingencies(network);
            List<SensitivityValue> nStateResult = factors.stream().map(this::nStateValue).collect(Collectors.toList());
            Map<String, List<SensitivityValue>> outageStateResult = contingencies.stream().collect(Collectors.toMap(Contingency::getId, co -> factors.stream().map(factor -> outageStateValue(co, factor)).collect(Collectors.toList())));
            SensitivityAnalysisResult result = new SensitivityAnalysisResult(true, Collections.emptyMap(), "", nStateResult, outageStateResult);
            return CompletableFuture.completedFuture(result);
        }

        private SensitivityValue nStateValue(SensitivityFactor factor) {
            if (factor.getFunction().getId().equals("FR-BE")) {
                return new SensitivityValue(factor, FAKE_SENSITIVITY_VALUE, 50, FAKE_VARIABLE_REFERENCE_VALUE);
            } else if (factor.getFunction().getId().equals("FR-DE")) {
                return new SensitivityValue(factor, FAKE_SENSITIVITY_VALUE, 50, FAKE_VARIABLE_REFERENCE_VALUE);
            } else if (factor.getFunction().getId().equals("BE-NL")) {
                return new SensitivityValue(factor, FAKE_SENSITIVITY_VALUE, 50, FAKE_VARIABLE_REFERENCE_VALUE);
            } else if (factor.getFunction().getId().equals("DE-NL")) {
                return new SensitivityValue(factor, FAKE_SENSITIVITY_VALUE, 50, FAKE_VARIABLE_REFERENCE_VALUE);
            }
            return null;
        }

        private SensitivityValue outageStateValue(Contingency contingency, SensitivityFactor factor) {
            if (contingency.getId().equals("FR-BE")) {
                if (factor.getFunction().getId().equals("FR-BE")) {
                    return new SensitivityValue(factor, FAKE_SENSITIVITY_VALUE, 0, FAKE_VARIABLE_REFERENCE_VALUE);
                } else if (factor.getFunction().getId().equals("FR-DE")) {
                    return new SensitivityValue(factor, FAKE_SENSITIVITY_VALUE, 100, FAKE_VARIABLE_REFERENCE_VALUE);
                } else if (factor.getFunction().getId().equals("BE-NL")) {
                    return new SensitivityValue(factor, FAKE_SENSITIVITY_VALUE, 0, FAKE_VARIABLE_REFERENCE_VALUE);
                } else if (factor.getFunction().getId().equals("DE-NL")) {
                    return new SensitivityValue(factor, FAKE_SENSITIVITY_VALUE, 100, FAKE_VARIABLE_REFERENCE_VALUE);
                }
            } else if (contingency.getId().equals("FR-DE")) {
                if (factor.getFunction().getId().equals("FR-BE")) {
                    return new SensitivityValue(factor, FAKE_SENSITIVITY_VALUE, 100, FAKE_VARIABLE_REFERENCE_VALUE);
                } else if (factor.getFunction().getId().equals("FR-DE")) {
                    return new SensitivityValue(factor, FAKE_SENSITIVITY_VALUE, 0, FAKE_VARIABLE_REFERENCE_VALUE);
                } else if (factor.getFunction().getId().equals("BE-NL")) {
                    return new SensitivityValue(factor, FAKE_SENSITIVITY_VALUE, 100, FAKE_VARIABLE_REFERENCE_VALUE);
                } else if (factor.getFunction().getId().equals("DE-NL")) {
                    return new SensitivityValue(factor, FAKE_SENSITIVITY_VALUE, 0, FAKE_VARIABLE_REFERENCE_VALUE);
                }
            }
            return null;
        }

        @Override
        public String getName() {
            return "Mock";
        }

        @Override
        public String getVersion() {
            return "v0";
        }
    }
}
