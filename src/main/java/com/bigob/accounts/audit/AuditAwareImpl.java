package com.bigob.accounts.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(
        value = "auditorAware")
public class AuditAwareImpl implements AuditorAware<Integer> {
    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    @Override
    public Optional<Integer> getCurrentAuditor() {
        return Optional.of(1220);
    }
}
