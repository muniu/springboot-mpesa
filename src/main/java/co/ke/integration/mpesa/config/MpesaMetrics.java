package co.ke.integration.mpesa.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MpesaMetrics {
    private final MeterRegistry registry;
    private final Counter authRequestCounter;
    private final Counter authFailureCounter;
    private final Timer authRequestTimer;

    public MpesaMetrics(MeterRegistry registry) {
        this.registry = registry; // Store registry for creating dynamic counters

        this.authRequestCounter = Counter.builder("mpesa.auth.requests")
                .description("Number of M-Pesa auth requests")
                .register(registry);

        this.authFailureCounter = Counter.builder("mpesa.auth.failures")
                .description("Number of M-Pesa auth failures")
                .register(registry);

        this.authRequestTimer = Timer.builder("mpesa.auth.duration")
                .description("Time taken for auth requests")
                .register(registry);
    }

    public void incrementAuthRequests() {
        authRequestCounter.increment();
    }

    public void incrementAuthFailures() {
        authFailureCounter.increment();
    }

    public Timer.Sample startAuthTimer() {
        return Timer.start();
    }

    public void stopAuthTimer(Timer.Sample sample) {
        sample.stop(authRequestTimer);
    }

    // For transactions, create counters with tags dynamically
    public void recordTransaction(String type) {
        Counter.builder("mpesa.transactions")
                .description("Number of M-Pesa transactions")
                .tags("type", type)
                .register(registry)
                .increment();
    }

    public void recordTransactionFailure(String type) {
        Counter.builder("mpesa.transactions.failures")
                .description("Number of failed M-Pesa transactions")
                .tags("type", type)
                .register(registry)
                .increment();
    }

    // Alternative approach using Counter maps
    private final Map<String, Counter> transactionCounters = new ConcurrentHashMap<>();
    private final Map<String, Counter> failureCounters = new ConcurrentHashMap<>();

    public void recordTransactionWithCache(String type) {
        transactionCounters.computeIfAbsent(type, this::createTransactionCounter)
                .increment();
    }

    public void recordTransactionFailureWithCache(String type) {
        failureCounters.computeIfAbsent(type, this::createFailureCounter)
                .increment();
    }

    private Counter createTransactionCounter(String type) {
        return Counter.builder("mpesa.transactions")
                .description("Number of M-Pesa transactions")
                .tags("type", type)
                .register(registry);
    }

    private Counter createFailureCounter(String type) {
        return Counter.builder("mpesa.transactions.failures")
                .description("Number of failed M-Pesa transactions")
                .tags("type", type)
                .register(registry);
    }
}
