package com.github.bogdanovmn.memorydeluge.cli.collectdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
class ConcurrentConsuming {
    private final int poolSize;

    private AtomicInteger processed;
    private volatile boolean hasError;

    <T> void process(List<T> itemsToProcess, Consumer<T> processing) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        int totalItems = itemsToProcess.size();
        processed = new AtomicInteger(0);
        try {
            for (T item : itemsToProcess) {
                if (hasError) {
                    break;
                }
                executorService.submit(
                    () -> {
                        try {
                            processing.accept(item);
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                            hasError = true;
                        } finally {
                            processed.incrementAndGet();
                        }
                    });
            }
        } finally {
            executorService.shutdown();
            while (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
                log.info("Items to process left: {}", totalItems - processed.get());
            }
        }

    }
}
