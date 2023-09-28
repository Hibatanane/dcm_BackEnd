package prjt.dcm.Helpers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UUIDGenerator {
    private static final AtomicLong counter = new AtomicLong(0);
    public static UUID generateUniqueUUID() {
        LocalDate today = LocalDate.now();
        long timestamp = today.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000; // Convert to milliseconds
        long increment = counter.incrementAndGet();
        return new UUID(timestamp, increment);
    }
}
