package org.invoicebuilder.users.service.registration;

import org.invoicebuilder.users.dto.UserRegisterRequest;
import org.invoicebuilder.users.dto.UserRegistrationResponse;
import org.invoicebuilder.users.service.registration.interfaces.RegistrationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service that manages and delegates to appropriate registration strategies
 * Provides a single entry point for all registration operations
 */
@Service
public class RegistrationService {

    private final Map<String, RegistrationStrategy> strategies;

    @Autowired
    public RegistrationService(List<RegistrationStrategy> strategyList) {
        // Convert list to map for efficient lookup by strategy type
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        RegistrationStrategy::getStrategyType,
                        Function.identity()
                ));
    }

    /**
     * Registers a user using a specific strategy type
     * 
     * @param request The registration request
     * @param strategyType The strategy type to use
     * @return RegistrationResponse with created user and account details
     * @throws IllegalArgumentException if strategy type is not supported
     */
    public UserRegistrationResponse registerWithStrategy(UserRegisterRequest request, String strategyType) {
        RegistrationStrategy strategy = getStrategy(strategyType);
        return strategy.register(request);
    }

    /**
     * Gets all available registration strategy types
     * 
     * @return Array of available strategy type identifiers
     */
    public String[] getAvailableStrategies() {
        return strategies.keySet().toArray(new String[0]);
    }

    /**
     * Checks if a strategy type is supported
     * 
     * @param strategyType The strategy type to check
     * @return true if strategy is available, false otherwise
     */
    public boolean isStrategySupported(String strategyType) {
        return strategies.containsKey(strategyType);
    }

    private RegistrationStrategy getStrategy(String strategyType) {
        RegistrationStrategy strategy = strategies.get(strategyType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported registration strategy: " + strategyType + 
                    ". Available strategies: " + String.join(", ", getAvailableStrategies()));
        }
        return strategy;
    }
}
