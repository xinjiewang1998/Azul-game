package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertTrue;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class GenerateActionTest {

    private String errorPrefix(String[] inputState, String move) {
        return "Azul.generateAction({\"" + inputState[0] + "\", \"" + inputState[1] + "\"})"
                + System.lineSeparator()
                + "returned {\"" + move + "\"};"
                + System.lineSeparator();
    }

    private String[][] validMoves = {
            new String[]{"A1b0", "A2d1", "A0d3", "A4d0", "A1b1", "A2d2", "A0d4", "A0d1", "A2d0", "A0d2", "A0d0", "A4cF", "A2cF", "A3eF", "A1eF", "A4c4", "A4c2", "A2c4", "A4c3", "A4c0", "A2c2", "A3e3", "A4c1", "A2c3", "A3e4", "A0a0", "A1c1", "A3c0", "A0a1", "A1c2", "A1c0", "A3bF", "A1bF", "A4dF", "A2dF", "A0dF", "A3b4", "A3b2", "A4d3", "A1b4", "A3b3", "A4d4", "A3b0", "A4d1", "A1b2", "A2d3", "A3b1", "A4d2", "A1b3", "A2d4", "A3d0", "A0b1", "A2b0", "A3d1", "A0b2", "A0b0", "A0aF", "A3cF", "A1cF", "A3c3", "A0a4", "A3c4", "A3c1", "A0a2", "A1c3", "A3c2", "A0a3", "A1c4", "A2c0", "A3e1", "A1e3", "A2c1", "A3e2", "A1e4", "A1e1", "A3e0", "A1e2", "A1e0", "A4bF", "A3dF", "A2bF", "A0bF", "A4b3", "A4b4", "A4b1", "A2b3", "A3d4", "A4b2", "A2b4", "A2b1", "A3d2", "A0b3", "A4b0", "A2b2", "A3d3", "A0b4"},
            new String[]{"B3c0", "B1c2", "B3c1", "B1c3", "B1c0", "B1c1", "B4dF", "B3bF", "B2dF", "B1bF", "BCaF", "B3b3", "B4d4", "B3b4", "B3b1", "B4d2", "B1b3", "B2d4", "B3b2", "B4d3", "B1b4", "B2b0", "B3d1", "B2b1", "B3d2", "B3d0", "B3cF", "B1cF", "B3c4", "B3c2", "B1c4", "B3c3", "B2c1", "B3e2", "B1e4", "B4c0", "B2c2", "B3e3", "B3e0", "B1e2", "B2c0", "B3e1", "B1e3", "B1e0", "B1e1", "B4bF", "B2bF", "B3dF", "BCd4", "BCd3", "BCd2", "BCd1", "BCd0", "B4b4", "B4b2", "B2b4", "B4b3", "B4b0", "B2b2", "B3d3", "B4b1", "B2b3", "B3d4", "B4d0", "B1b1", "B2d2", "B3b0", "B4d1", "B1b2", "B2d3", "B2d0", "B1b0", "B2d1", "BCa4", "B4cF", "BCa3", "B3eF", "BCa2", "B2cF", "BCa1", "B1eF", "BCa0", "B4c3", "BCdF", "B4c4", "B4c1", "B2c3", "B3e4", "B4c2", "B2c4"},
            new String[]{"A1b0", "A1b1", "A2d2", "A2d0", "ACa3", "A2cF", "ACa4", "ACa2", "A3eF", "ACa0", "A1eF", "ACdF", "A2c4", "A2c2", "A3e3", "A2c3", "A3e4", "A3c0", "A1c2", "A1c0", "A3bF", "ACb4", "A1bF", "ACb2", "ACb3", "A2dF", "ACb0", "ACb1", "ACaF", "A3b4", "A3b2", "A1b4", "A3b3", "A3b0", "A1b2", "A2d3", "A3b1", "A1b3", "A2d4", "A3d0", "A2b0", "A3cF", "A1cF", "ACbF", "A3c3", "A3c4", "A1c3", "A3c2", "A1c4", "A2c0", "A1e3", "A3e2", "A1e4", "A3e0", "A1e2", "A1e0", "A3dF", "A2bF", "ACd4", "ACd2", "ACd3", "ACd0", "A2b3", "A3d4", "A2b4", "A2b1", "A3d2", "A2b2", "A3d3"},
            new String[]{"B1c2", "B3c1", "B1c3", "B1c1", "B3bF", "B2dF", "B1bF", "BCaF", "B3b3", "B3b4", "B3b1", "B1b3", "B2d4", "B3b2", "B1b4", "B3d1", "B2b1", "B3d2", "B3cF", "B1cF", "B3c4", "B3c2", "B1c4", "B3c3", "B2c1", "B3e2", "B1e4", "B2c2", "B3e3", "B1e2", "B3e1", "B1e3", "B1e1", "B2bF", "B3dF", "BCd4", "BCd3", "BCd2", "BCd1", "B2b4", "B2b2", "B3d3", "B2b3", "B3d4", "B1b1", "B2d2", "B1b2", "B2d3", "B2d1", "BCa4", "BCa3", "B3eF", "BCa2", "B2cF", "BCa1", "B1eF", "BCdF", "B2c3", "B3e4", "B2c4"},
            new String[]{"A1b0", "A2d2", "A2d0", "A2cF", "A3eF", "A1eF", "ACdF", "A2c4", "A2c2", "A3e3", "A2c3", "A3e4", "A3c0", "A1c2", "A1c0", "A3bF", "A1bF", "A2dF", "A3b4", "A3b2", "A1b4", "A3b3", "A3b0", "A1b2", "A2d3", "A1b3", "A2d4", "A3d0", "A2b0", "A3cF", "A1cF", "A3c3", "A3c4", "A1c3", "A3c2", "A1c4", "A2c0", "A1e3", "A3e2", "A1e4", "A3e0", "A1e2", "A1e0", "A3dF", "A2bF", "ACd4", "ACd2", "ACd3", "ACd0", "A2b3", "A3d4", "A2b4", "A3d2", "A2b2", "A3d3"},
            new String[]{"B1c2", "B3e2", "B1e4", "B1c3", "B3e3", "B1e2", "B1e3", "B3bF", "BCb4", "BCb3", "B1bF", "B3dF", "BCb2", "BCd4", "BCd3", "BCd2", "BCcF", "B3b3", "B3b4", "B1b3", "B3d3", "B3b2", "B1b4", "B3d4", "B1b2", "B3d2", "B3cF", "B1cF", "B3eF", "BCc4", "BCc3", "B1eF", "BCc2", "BCbF", "B3c4", "BCdF", "B3c2", "B1c4", "B3e4", "B3c3"},
            new String[]{"A1b0", "ACc4", "A1cF", "ACc2", "ACe4", "A1eF", "ACc0", "ACe2", "ACe0", "ACbF", "ACdF", "A1c4", "A1c2", "A1e4", "A1c0", "A1e2", "A1e0", "ACb4", "A1bF", "ACb2", "ACd4", "ACb0", "ACd2", "ACd3", "ACd0", "ACcF", "A1b4", "ACeF", "A1b2"},
            new String[]{"BCb4", "BCc4", "BCb2", "BCc3", "BCd4", "BCc2", "BCe4", "BCd2", "BCe2", "BCbF", "BCcF", "BCdF", "BCeF"},
            new String[]{"ACe2", "ACbF", "ACb4", "ACcF", "ACb2", "ACc4", "ACeF", "ACc2", "ACe4"},
            new String[]{"BCbF", "BCb4", "BCeF", "BCe4"},
            new String[]{"ACbF", "ACb2"},
            new String[]{"A00", "A01", "A02", "A03", "A04"},
            new String[]{"A10", "A11", "A12", "A13"},
            new String[]{"A21", "A23", "A20"},
            new String[]{"B00", "B01", "B02", "B03", "B04"},
            new String[]{"B20", "B21", "B22", "B23", "B24"},
            new String[]{"A2d1", "A2d2", "A2d0", "A0cF", "A0c4", "A0a0", "A4e0", "A1c1", "A2e2", "A3c0", "A0a1", "A4e1", "A1c2", "A2e0", "A0e2", "A1c0", "A2e1", "A0e0", "A0e1", "A3bF", "A1bF", "A2dF", "A2d3", "A3d0", "A1d2", "A3d1", "A1d3", "A1d0", "A1d1", "A4aF", "A0aF", "A4eF", "A3cF", "A2eF", "A1cF", "A0eF", "A4a2", "A4a0", "A4a1", "A3c4", "A3c1", "A0a2", "A4e2", "A3c2", "A1c4", "A0c2", "A0c0", "A0c1", "A3dF", "A2bF", "A1dF", "A0bF", "A3d2", "A3d3"},
            new String[]{"B0a1", "B1c2", "B0c3", "B0a2", "B4e2", "B1c3", "B0a0", "B4e0", "B0c2", "B0e4", "B0e2", "B0e0", "B3bF", "B0bF", "B1bF", "B3dF", "BCb2", "B1dF", "BCb0", "BCd0", "B3b2", "B0b2", "B3b0", "B1b2", "B0b0", "B1b0", "B3d0", "B1d0", "B4aF", "B3cF", "B1cF", "B0aF", "B4eF", "B0cF", "B0eF", "BCbF", "B4a1", "BCdF", "B4a2", "B3c2", "B4a0", "B3c3", "B4e4"},
            new String[]{"A1d2", "A1d3", "A1d0", "A4aF", "A0aF", "A4eF", "A0cF", "A1cF", "A0eF", "ACbF", "A4a2", "ACdF", "A4a0", "A0a2", "A4e2", "A0c4", "A1c4", "A0a0", "A4e0", "A0c2", "A1c2", "A0c0", "A0e2", "A1c0", "A0e0", "A1bF", "A1dF", "A0bF", "ACd2", "ACd3", "ACd0"},
            new String[]{"B1c2", "B4e2", "B1c3", "B4e0", "B1bF", "BCb2", "B1dF", "BCb0", "BCd0", "BCaF", "BCeF", "B1b2", "B1b0", "B1d0", "B4aF", "B1cF", "BCa2", "B4eF", "BCa1", "BCa0", "BCe4", "BCe2", "BCe0", "BCbF", "B4a1", "BCdF", "B4a2", "B4a0", "B4e4"},
            new String[]{"A1d2", "A1c2", "A1d3", "A1bF", "A1dF", "ACa2", "A1cF", "ACd2", "ACd3", "ACe2", "ACbF", "ACaF", "ACdF", "ACeF", "A1c4"},
            new String[]{"B1c3", "B1b0", "B1d0", "B1cF", "B1bF", "BCe4", "B1dF", "BCb0", "BCe2", "BCd0", "BCe0", "BCbF", "BCdF", "BCeF"},
            new String[]{"A1d3", "ACbF", "A1bF", "ACdF", "A1dF", "A1cF", "ACd3", "A1c4"},
            new String[]{"BCb0", "B1c3", "B1b0", "B1d0", "BCbF", "B1cF", "B1bF", "B1dF"},
            new String[]{"ACbF", "ACcF", "ACc4"},
            new String[]{"BCbF"},
            new String[]{"B01", "B03", "B04"},
            new String[]{"A00", "A01", "A02", "A03"},
            new String[]{"A10", "A11", "A13", "A14"},
            new String[]{"A22", "A23", "A24", "A20"},
            new String[]{"A3d0", "A2d1", "A1d2", "A3d1", "A2d2", "A1d3", "A1d0", "A2d0", "A1d1", "A4aF", "A2aF", "A3aF", "A0aF", "A4eF", "A1aF", "A1cF", "A3eF", "A1eF", "A4a0", "A3a1", "A4a1", "A2a0", "A1a1", "A4e2", "A3a0", "A2a1", "A1c4", "A0a0", "A4e0", "A1c1", "A1a0", "A0a1", "A1c2", "A3e2", "A3e0", "A1e2", "A1e0", "A3dF", "A2bF", "A1dF", "A0bF", "A2dF", "A3d2", "A2d3", "A3d3"},
            new String[]{"B0a1", "B3e2", "B2a0", "B4e2", "B3e0", "B0a0", "B4e0", "B2bF", "B0bF", "B2dF", "B3dF", "BCaF", "BCcF", "B2b0", "B0b0", "B3aF", "B4aF", "B2aF", "B3eF", "B0aF", "B4eF", "BCa1", "BCc3", "BCa0", "B4a1", "BCdF", "B3a0", "B2a1", "B3e4", "B4a0", "B3a1", "B4e4"},
            new String[]{"A2d1", "A2d0", "A4aF", "A2aF", "A0aF", "A4eF", "ACa1", "ACc4", "ACc1", "ACa0", "ACdF", "A4a0", "A4a1", "A2a0", "A4e2", "A2a1", "A0a0", "A4e0", "A0a1", "A2bF", "A0bF", "A2dF", "ACd3", "ACd0", "ACd1", "ACaF", "ACcF", "A2d3"},
            new String[]{"B2b0", "B0a1", "B2a0", "B4e2", "B0b0", "B0a0", "B4e0", "B4aF", "B2bF", "B2aF", "B0bF", "B2dF", "B0aF", "B4eF", "BCa1", "BCc3", "BCa0", "BCaF", "BCcF", "B4a1", "B2a1", "B4a0"},
            new String[]{"A4e0", "A2d1", "A2d0", "A4aF", "A2aF", "A4eF", "A2bF", "ACa1", "ACc4", "A2dF", "ACc1", "ACa0", "ACbF", "ACaF", "ACcF", "A4a0", "A4a1", "A2a0", "A4e2", "A2a1"},
            new String[]{"BCb0", "B4e2", "B4e0", "B4aF", "BCaF", "BCbF", "BCcF", "BCdF", "B4eF", "BCc3", "B4a0", "BCa0"},
            new String[]{"A4e0", "A4aF", "A4eF", "ACa1", "ACc4", "ACa0", "ACd0", "ACaF", "ACdF", "ACcF", "A4a0", "A4a1", "A4e2"},
            new String[]{"B4e2", "B4aF", "BCaF", "BCcF", "B4eF", "BCc3"},
            new String[]{"ACaF", "ACcF", "ACa1", "ACc4"},
            new String[]{"BCcF", "BCc3"},
            new String[]{"B00", "B03", "B04"},
            new String[]{"B10", "B11", "B12", "B13", "B14"},
            new String[]{"B21", "B22", "B23", "B24"},
            new String[]{"B31", "B33", "B34", "B30"},
            new String[]{"B42", "B43", "B40", "B41"},
            new String[]{"A00", "A01", "A02"},
            new String[]{"A10", "A11"},
            new String[]{"A32", "A33", "A34", "A30"},
            new String[]{"A2d1", "A0d1", "A4aF", "A2aF", "A3aF", "A1aF", "A0cF", "A1cF", "A3eF", "A0eF", "A3a3", "A4a3", "A4a0", "A1a3", "A2a3", "A2a0", "A1c3", "A3e3", "A0c4", "A3a0", "A1c4", "A1c1", "A1a0", "A3e2", "A0c3", "A0e2", "A3e0", "A0c1", "A0e3", "A0e0", "A3bF", "A4bF", "A1bF", "A4dF", "A2dF", "A0dF", "A4b3", "A3b3", "A4d1", "A1b3"},
            new String[]{"B1c2", "B2a0", "B0e3", "B1c1", "B0e1", "B0e0", "B4dF", "B2dF", "B1bF", "B0dF", "BCaF", "B4d4", "BCeF", "B1b3", "B2d4", "B4d3", "B1b4", "B4aF", "B2aF", "B1cF", "B0eF", "B4a3", "B4a4", "B2a3", "B4a2", "B2a4", "B1c4", "B4a0", "B2a2", "B1a0", "B0c4", "B0c1", "B0c2", "B4bF", "B4b4", "B4b2", "B4b3", "B4b1", "B1b1", "B0d4", "B4d1", "B1b2", "B2d3", "B2d1", "B0d3", "B0d1", "B1aF", "BCa4", "BCa3", "BCa2", "BCa0", "B0cF", "BCe3", "BCe1", "BCe0", "B1a4", "B1a2", "B1a3"},
            new String[]{"A0d1", "A4aF", "A1aF", "A0cF", "A1cF", "A0eF", "ACa0", "ACe2", "ACe0", "ACdF", "A4a0", "A0c4", "A1c4", "A1c1", "A1a0", "A0e2", "A0c1", "A0e0", "A4bF", "A1bF", "A4dF", "A0dF", "ACd1", "ACaF", "A4b3", "ACeF", "A4d1", "A1b3"},
            new String[]{"B1a0", "B1c2", "B1c1", "B4bF", "B4dF", "B1bF", "BCd4", "BCd1", "BCaF", "B4b4", "BCcF", "B4b2", "B4d4", "BCeF", "B4b1", "B1b4", "B1b1", "B4d1", "B1b2", "B4aF", "B1aF", "BCa4", "BCa3", "B1cF", "BCa2", "BCc4", "BCa0", "BCc2", "BCc1", "BCe1", "BCe0", "B4a3", "B4a4", "B1a4", "BCdF", "B4a2", "B1a2", "B1c4", "B4a0", "B1a3"},
            new String[]{"A1c1", "A4aF", "A4bF", "A1bF", "A1aF", "A4dF", "A1cF", "ACd1", "ACe2", "ACaF", "A4b3", "ACdF", "ACeF", "A4d1", "A1b3", "A1c4"},
            new String[]{"B1a0", "B1b1", "B1c2", "B1b2", "B1c1", "B1aF", "BCa3", "B1cF", "BCa2", "B1bF", "BCa0", "BCd1", "BCe1", "BCe0", "BCaF", "BCdF", "BCeF", "B1a2", "B1c4", "B1a3"},
            new String[]{"A1c1", "ACd1", "ACe2", "A1bF", "ACdF", "A1aF", "A1cF", "ACeF", "A1b3", "A1c4"},
            new String[]{"BCd1", "BCe1", "BCe0", "BCaF", "BCbF", "BCa3", "BCdF", "BCeF", "BCa0", "BCb1"},
            new String[]{"ACbF", "ACaF", "ACdF", "ACb3"},
            new String[]{"BCd1", "BCaF", "BCa3", "BCdF"},
            new String[]{"ACaF"},
            new String[]{"A00", "A02"},
            new String[]{"A33", "A30"},
            new String[]{"B03"},
            new String[]{"B12", "B13", "B14"},
            new String[]{"B22", "B23"},
            new String[]{"B1a0", "B2c1", "B4e1", "B3c1", "B2c2", "B2e1", "B0a0", "B0e1", "B1e1", "B4dF", "B0bF", "B2dF", "B1bF", "B3dF", "B1b1", "B0b2", "B1b2", "B0b1", "B1aF", "B3cF", "B4cF", "B0aF", "B2cF", "B4eF", "B1eF", "B2eF", "B0eF", "B3c4", "B4c4", "B4c1", "B3c2", "B0a3", "B4c2", "B1a3", "B2c4"},
            new String[]{"A4cF", "A0aF", "A2cF", "A4eF", "A1aF", "A2eF", "A0eF", "A1eF", "A4c4", "ACdF", "A1a3", "A2c4", "A4c3", "A4e2", "A4c1", "A0a3", "A2c3", "A4e3", "A0a0", "A2e2", "A1e3", "A1a0", "A2c1", "A2e3", "A0e2", "A1e2", "A0e3", "A1bF", "A4dF", "A0bF", "A2dF"},
            new String[]{"B1a0", "B2c1", "B2c2", "B2e1", "B0a0", "B0e1", "B1e1", "B0bF", "B2dF", "B1bF", "BCeF", "B1b1", "B0b2", "B1b2", "B0b1", "B1aF", "B0aF", "B2cF", "B1eF", "B2eF", "B0eF", "BCe1", "BCdF", "B0a3", "B1a3", "B2c4"},
            new String[]{"A0a0", "A2e2", "A2c1", "A2e3", "A0e2", "A0e3", "ACa3", "A0aF", "A2cF", "A2eF", "A0bF", "A2dF", "ACe3", "A0eF", "ACa0", "ACe2", "ACbF", "ACaF", "ACdF", "ACeF", "A0a3", "A2c3"},
            new String[]{"B0b2", "B2c2", "B2e1", "B0a0", "B0e1", "B0bF", "B2dF", "B0aF", "B2cF", "BCb2", "B2eF", "B0eF", "BCe1", "BCbF", "BCdF", "BCeF", "B0a3", "B2c4"},
            new String[]{"A2e2", "A2c1", "A2e3", "A0e2", "A0e3", "A0aF", "A2cF", "A2eF", "A0bF", "A2dF", "ACe3", "A0eF", "ACe2", "ACdF", "ACeF", "A0a3", "A2c3"},
            new String[]{"B0b2", "B2e1", "B0a0", "B0e1", "B0bF", "B2dF", "B0aF", "B2cF", "B2eF", "B0eF", "BCdF", "B0a3", "B2c4"},
            new String[]{"A2e2", "A2c1", "ACe2", "A2e3", "ACbF", "A2cF", "ACdF", "A2eF", "A2dF", "ACeF", "ACe3"},
            new String[]{"B2e1", "BCbF", "B2dF", "BCdF", "B2cF", "BCb2", "B2c4", "B2eF"},
            new String[]{"A2e2", "A2c1", "A2e3", "A2cF", "ACdF", "A2eF", "A2dF"},
            new String[]{"BCcF", "BCdF", "BCc4"},
            new String[]{"ACdF"},
            new String[]{"A00"},
            new String[]{"A44", "A40", "A41", "A42"},
            new String[]{"B04"},
            new String[]{"B21", "B22"},
    };

    private String[][] gameStates = {
            new String[]{"AF0abdd1bbce2bbcd3bcde4bcddCfB1913161418D0000000000", "A0MSFB0MSF"},
            new String[]{"BF1bbce2bbcd3bcde4bcddCaddfB1913161418D0000000000", "A0MS1b1FB0MSF"},
            new String[]{"AF1bbce2bbcd3bcdeCabddddfB1913161418D0000000000", "A0MS1b1FB0MS0c1F"},
            new String[]{"BF1bbce2bbcd3bcdeCaddddB1913161418D0000000000", "A0MS1b2FfB0MS0c1F"},
            new String[]{"AF1bbce2bbcd3bcdeCddddB1913161418D0000000000", "A0MS1b2FfB0MS0c11a1F"},
            new String[]{"BF1bbce3bcdeCbbcddddB1913161418D0000000000", "A0MS1b23d1FfB0MS0c11a1F"},
            new String[]{"AF1bbceCbbbcdddddeB1913161418D0000000000", "A0MS1b23d1FfB0MS0c11a13c1F"},
            new String[]{"BFCbbbccdddddeeB1913161418D0000000000", "A0MS0b11b23d1FbfB0MS0c11a13c1F"},
            new String[]{"AFCbbbcceeB1913161418D0000000000", "A0MS0b11b23d1FbfB0MS0c11a12d33c1Fdd"},
            new String[]{"BFCbbbeeB1913161418D0000000000", "A0MS0b11b23d14c2FbfB0MS0c11a12d33c1Fdd"},
            new String[]{"AFCbbbB1913161418D0000000000", "A0MS0b11b23d14c2FbfB0MS0c11a12d33c14e2Fdd"},
            new String[]{"AFCB1913161418D0000000000", "A0MS0b11b22b33d14c2FbfB0MS0c11a12d33c14e2Fdd"},
            new String[]{"AFCB1913161418D0000000000", "A1Mb04S1b22b33d14c2FbfB0MS0c11a12d33c14e2Fdd"},
            new String[]{"AFCB1914161418D0000000000", "A2Mb04b12S2b33d14c2FbfB0MS0c11a12d33c14e2Fdd"},
            new String[]{"BFCB1916161418D0000000000", "A3Mb04b12b21S3d14c2FbfB0MS0c11a12d33c14e2Fdd"},
            new String[]{"BFCB1916161418D0000000000", "A3Mb04b12b21S3d14c2FbfB1Mc02S1a12d33c14e2Fdd"},
            new String[]{"AF0abce1bccd2bdee3bccd4aaeeCfB1612111313D0001000200", "A1Mb04b12b21S3d14c2FB0Mc02d20S1a13c14e2F"},
            new String[]{"BF0abce1bccd3bccd4aaeeCbdfB1612111313D0001000200", "A1Mb04b12b21S1e23d14c2FB0Mc02d20S1a13c14e2F"},
            new String[]{"AF0abce1bccd4aaeeCbbddfB1612111313D0001000200", "A1Mb04b12b21S1e23d14c2FB0Mc02d20S1a13c34e2F"},
            new String[]{"BF1bccd4aaeeCabbbddefB1612111313D0001000200", "A1Mb04b12b21S0c11e23d14c2FB0Mc02d20S1a13c34e2F"},
            new String[]{"AF1bccdCaaabbbddefB1612111313D0001000200", "A1Mb04b12b21S0c11e23d14c2FB0Mc02d20S1a12e23c34e2F"},
            new String[]{"BF1bccdCbbbddeB1612111313D0001000200", "A1Mb04b12b21S0c11e22a33d14c2FfB0Mc02d20S1a12e23c34e2F"},
            new String[]{"AF1bccdCbbbddB1612111313D0001000200", "A1Mb04b12b21S0c11e22a33d14c2FfB0Mc02d20S1a12e23c34e3F"},
            new String[]{"BF1bccdCbbbB1612111313D0001000200", "A1Mb04b12b21S0c11e22a33d34c2FfB0Mc02d20S1a12e23c34e3F"},
            new String[]{"AFCbbbbccB1612111313D0001000200", "A1Mb04b12b21S0c11e22a33d34c2FfB0Mc02d20S0d11a12e23c34e3F"},
            new String[]{"BFCbbbbB1612111313D0001000200", "A1Mb04b12b21S0c11e22a33d34c4FfB0Mc02d20S0d11a12e23c34e3F"},
            new String[]{"BFCB1612111313D0001000200", "A1Mb04b12b21S0c11e22a33d34c4FfB0Mc02d20S0d11a12e23c34e3Fbbbb"},
            new String[]{"AFCB1612111313D0001000200", "A1Mb04b12b21S0c11e22a33d34c4FfB2Md01c02d20S1a12e23c34e3Fbbbb"},
            new String[]{"AFCB1612111313D0001000200", "A3Mc03b04b12b21S1e22a33d34c4FfB2Md01c02d20S1a12e23c34e3Fbbbb"},
            new String[]{"AFCB1612111314D0001000200", "A7Mc03b04b12e13b21S2a33d34c4FfB2Md01c02d20S1a12e23c34e3Fbbbb"},
            new String[]{"AF0aaab1acde2abbd3adee4aeeeCfB1109101008D0005000200", "A7Mc03b04b12e13b21a24S3d34c4FB0Md01c02d20S1a12e23c34e3F"},
            new String[]{"BF0aaab2abbd3adee4aeeeCacdfB1109101008D0005000200", "A7Mc03b04b12e13b21a24S2e13d34c4FB0Md01c02d20S1a12e23c34e3F"},
            new String[]{"AF0aaab2abbd4aeeeCaacddfB1109101008D0005000200", "A7Mc03b04b12e13b21a24S2e13d34c4FB0Md01c02d20S1a12e23c34e5F"},
            new String[]{"BF0aaab2abbd4aeeeCaacB1109101008D0005000200", "A7Mc03b04b12e13b21a24S2e13d44c4FdfB0Md01c02d20S1a12e23c34e5F"},
            new String[]{"AF2abbd4aeeeCaabcB1109101008D0005000200", "A7Mc03b04b12e13b21a24S2e13d44c4FdfB0Md01c02d20S1a22e23c34e5Faa"},
            new String[]{"BF4aeeeCaabbbcdB1109101008D0005000200", "A7Mc03b04b12e13b21a24S1a12e13d44c4FdfB0Md01c02d20S1a22e23c34e5Faa"},
            new String[]{"AF4aeeeCaacdB1109101008D0005000200", "A7Mc03b04b12e13b21a24S1a12e13d44c4FdfB0Md01c02d20S0b11a22e23c34e5Faabb"},
            new String[]{"BF4aeeeCaacB1109101008D0005000200", "A7Mc03b04b12e13b21a24S0d11a12e13d44c4FdfB0Md01c02d20S0b11a22e23c34e5Faabb"},
            new String[]{"AFCaaacB1109101008D0005000200", "A7Mc03b04b12e13b21a24S0d11a12e13d44c4FdfB0Md01c02d20S0b11a22e33c34e5Faabbee"},
            new String[]{"BFCcB1109101008D0005000200", "A7Mc03b04b12e13b21a24S0d11a22e13d44c4FaadfB0Md01c02d20S0b11a22e33c34e5Faabbee"},
            new String[]{"BFCB1109101008D0005000200", "A7Mc03b04b12e13b21a24S0d11a22e13d44c4FaadfB0Md01c02d20S0b11a22e33c44e5Faabbee"},
            new String[]{"BFCB1109101008D0005000200", "A7Mc03b04b12e13b21a24S0d11a22e13d44c4FaadfB3Mb00d01c02d20S1a22e33c44e5Faabbee"},
            new String[]{"BFCB1209101008D0005000200", "A7Mc03b04b12e13b21a24S0d11a22e13d44c4FaadfB5Mb00d01c02a11d20S2e33c44e5Faabbee"},
            new String[]{"BFCB1209101010D0005000200", "A7Mc03b04b12e13b21a24S0d11a22e13d44c4FaadfB6Mb00d01c02a11d20e24S3c44e5Faabbee"},
            new String[]{"BFCB1209131010D0005000200", "A7Mc03b04b12e13b21a24S0d11a22e13d44c4FaadfB7Mb00d01c02a11d20e24c31S4e5Faabbee"},
            new String[]{"AFCB1209131014D0005000200", "A7Mc03b04b12e13b21a24S0d11a22e13d44c4FaadfB9Mb00d01c02a11d20e24c31e41SFaabbee"},
            new String[]{"AFCB1209131014D0005000200", "A8Md01c03b04b12e13b21a24S1a22e13d44c4FaadfB9Mb00d01c02a11d20e24c31e41SFaabbee"},
            new String[]{"AFCB1309131014D0005000200", "A14Md01c03b04a11b12e13b21a24S2e13d44c4FaadfB9Mb00d01c02a11d20e24c31e41SFaabbee"},
            new String[]{"AF0cdde1abbc2aaad3aabe4abddCfB0605110812D0407000302", "A9Md01c03b04a11b12e13b21a24d32S2e14c4FB0Mb00d01c02a11d20e24c31e41SF"},
            new String[]{"BF0cdde1abbc2aaad4abddCaaefB0605110812D0407000302", "A9Md01c03b04a11b12e13b21a24d32S2e13b14c4FB0Mb00d01c02a11d20e24c31e41SF"},
            new String[]{"AF0cdde1abbc4abddCaadefB0605110812D0407000302", "A9Md01c03b04a11b12e13b21a24d32S2e13b14c4FB0Mb00d01c02a11d20e24c31e41S3a3F"},
            new String[]{"BF1abbc4abddCaacdddefB0605110812D0407000302", "A9Md01c03b04a11b12e13b21a24d32S0e12e13b14c4FB0Mb00d01c02a11d20e24c31e41S3a3F"},
            new String[]{"AF1abbc4abddCaadddeB0605110812D0407000302", "A9Md01c03b04a11b12e13b21a24d32S0e12e13b14c4FB0Mb00d01c02a11d20e24c31e41S3a34c1Ff"},
            new String[]{"BF1abbcCaaadddddeB0605110812D0407000302", "A9Md01c03b04a11b12e13b21a24d32S0e12e13b24c4FB0Mb00d01c02a11d20e24c31e41S3a34c1Ff"},
            new String[]{"AF1abbcCdddddeB0605110812D0407000302", "A9Md01c03b04a11b12e13b21a24d32S0e12e13b24c4FB0Mb00d01c02a11d20e24c31e41S2a33a34c1Ff"},
            new String[]{"BFCabbdddddeB0605110812D0407000302", "A9Md01c03b04a11b12e13b21a24d32S0e11c12e13b24c4FB0Mb00d01c02a11d20e24c31e41S2a33a34c1Ff"},
            new String[]{"AFCabbdddddB0605110812D0407000302", "A9Md01c03b04a11b12e13b21a24d32S0e11c12e13b24c4FB0Mb00d01c02a11d20e24c31e41S0e12a33a34c1Ff"},
            new String[]{"BFCadddddB0605110812D0407000302", "A9Md01c03b04a11b12e13b21a24d32S0e11c12e13b44c4FB0Mb00d01c02a11d20e24c31e41S0e12a33a34c1Ff"},
            new String[]{"AFCaB0605110812D0407000302", "A9Md01c03b04a11b12e13b21a24d32S0e11c12e13b44c4FB0Mb00d01c02a11d20e24c31e41S0e11d22a33a34c1Fdddf"},
            new String[]{"AFCB0605110812D0407000302", "A9Md01c03b04a11b12e13b21a24d32S0e11c12e13b44c4FaB0Mb00d01c02a11d20e24c31e41S0e11d22a33a34c1Fdddf"},
            new String[]{"AFCB0605110812D0407000302", "A15Md01e02c03b04a11b12e13b21a24d32S1c12e13b44c4FaB0Mb00d01c02a11d20e24c31e41S0e11d22a33a34c1Fdddf"},
            new String[]{"BFCB0608110812D0407000302", "A16Md01e02c03b04a11b12e13b21a24b30d32S1c12e14c4FaB0Mb00d01c02a11d20e24c31e41S0e11d22a33a34c1Fdddf"},
            new String[]{"BFCB0608110812D0407000302", "A16Md01e02c03b04a11b12e13b21a24b30d32S1c12e14c4FaB4Mb00d01c02e03a11d20e24c31e41S1d22a33a34c1Fdddf"},
            new String[]{"BFCB0608110912D0407000302", "A16Md01e02c03b04a11b12e13b21a24b30d32S1c12e14c4FaB8Mb00d01c02e03a11d12d20e24c31e41S2a33a34c1Fdddf"},
            new String[]{"BF0abbe1aabe2cdde3ccdd4cddeCfB0505070308D0507000602", "A15Md01e02c03b04a11b12e13b21a24b30d32S1c12e14c4FB4Mb00d01c02e03a11d12d20a23e24c31e41S3a34c1F"},
            new String[]{"AF0abbe1aabe2cdde4cddeCddfB0505070308D0507000602", "A15Md01e02c03b04a11b12e13b21a24b30d32S1c12e14c4FB4Mb00d01c02e03a11d12d20a23e24c31e41S3a34c3F"},
            new String[]{"BF0abbe1aabe2cddeCddddefB0505070308D0507000602", "A15Md01e02c03b04a11b12e13b21a24b30d32S1c12e14c5FB4Mb00d01c02e03a11d12d20a23e24c31e41S3a34c3F"},
            new String[]{"AF0abbe2cddeCaabddddefB0505070308D0507000602", "A15Md01e02c03b04a11b12e13b21a24b30d32S1c12e14c5FB4Mb00d01c02e03a11d12d20a23e24c31e41S1e13a34c3F"},
            new String[]{"BF0abbe2cddeCbddddeB0505070308D0507000602", "A15Md01e02c03b04a11b12e13b21a24b30d32S0a11c12e14c5FafB4Mb00d01c02e03a11d12d20a23e24c31e41S1e13a34c3F"},
            new String[]{"AF0abbe2cddeCddddeB0505070308D0507000602", "A15Md01e02c03b04a11b12e13b21a24b30d32S0a11c12e14c5FafB4Mb00d01c02e03a11d12d20a23e24c31e41S1e12b13a34c3F"},
            new String[]{"BF0abbe2cddeCddddB0505070308D0507000602", "A15Md01e02c03b04a11b12e13b21a24b30d32S0a11c12e13e14c5FafB4Mb00d01c02e03a11d12d20a23e24c31e41S1e12b13a34c3F"},
            new String[]{"AF2cddeCbbddddeB0505070308D0507000602", "A15Md01e02c03b04a11b12e13b21a24b30d32S0a11c12e13e14c5FafB4Mb00d01c02e03a11d12d20a23e24c31e41S0a11e12b13a34c3F"},
            new String[]{"BF2cddeCbbddddB0505070308D0507000602", "A15Md01e02c03b04a11b12e13b21a24b30d32S0a11c12e23e14c5FafB4Mb00d01c02e03a11d12d20a23e24c31e41S0a11e12b13a34c3F"},
            new String[]{"AF2cddeCddddB0505070308D0507000602", "A15Md01e02c03b04a11b12e13b21a24b30d32S0a11c12e23e14c5FafB4Mb00d01c02e03a11d12d20a23e24c31e41S0a11e12b33a34c3F"},
            new String[]{"BFCcddddddB0505070308D0507000602", "A15Md01e02c03b04a11b12e13b21a24b30d32S0a11c12e23e24c5FafB4Mb00d01c02e03a11d12d20a23e24c31e41S0a11e12b33a34c3F"},
            new String[]{"AFCddddddB0505070308D0507000602", "A15Md01e02c03b04a11b12e13b21a24b30d32S0a11c12e23e24c5FafB4Mb00d01c02e03a11d12d20a23e24c31e41S0a11e12b33a34c4F"},
            new String[]{"AFCB0505070308D0507000702", "A15Md01e02c03b04a11b12e13b21a24b30d32S0a11c12e23e24c5FadddddfB4Mb00d01c02e03a11d12d20a23e24c31e41S0a11e12b33a34c4F"},
            new String[]{"AFCB0505070308D0507000702", "A20Ma00d01e02c03b04a11b12e13b21a24b30d32S1c12e23e24c5FadddddfB4Mb00d01c02e03a11d12d20a23e24c31e41S0a11e12b33a34c4F"},
            new String[]{"BFCB0505110308D0507000702", "A22Ma00d01e02c03b04a11b12e13b21a24b30d32c42S1c12e23e2FadddddfB4Mb00d01c02e03a11d12d20a23e24c31e41S0a11e12b33a34c4F"},
            new String[]{"BFCB0505110308D0507000702", "A22Ma00d01e02c03b04a11b12e13b21a24b30d32c42S1c12e23e2FadddddfB9Mb00d01c02e03a04a11d12d20a23e24c31e41S1e12b33a34c4F"},
    };

    @Test
    public void testDraftingAction() {
        for (int i = 0; i < gameStates.length; i++) {
            if (validMoves[i][0].length() == 4) {
                String out = Azul.generateAction(gameStates[i]);
                String errorMessagePrefix = errorPrefix(gameStates[i], out);
                Set<String> moves = new HashSet<>(Arrays.asList(validMoves[i]));
                assertTrue(moves.contains(out), errorMessagePrefix + "expected a move from \n" + Arrays.toString(validMoves[i]));
            }
        }
    }

    @Test
    public void testTilingAction() {
        for (int i = 0; i < gameStates.length; i++) {
            if (validMoves[i][0].length() == 3) {
                String out = Azul.generateAction(gameStates[i]);
                String errorMessagePrefix = errorPrefix(gameStates[i], out);
                Set<String> moves = new HashSet<>(Arrays.asList(validMoves[i]));
                assertTrue(moves.contains(out), errorMessagePrefix + "expected a move from \n" + Arrays.toString(validMoves[i]));
            }
        }
    }
}
