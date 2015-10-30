package ch.mfrey.jackson.antpathfilter.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ch.mfrey.jackson.antpathfilter.Jackson2Helper;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@Threads(5)
public class FilterBenchmark {

    private final Jackson2Helper jackson2Helper = new Jackson2Helper();
    private final List<User> users = new ArrayList<User>();

    private final Map<Integer, ObjectMapper> cache = new ConcurrentHashMap<Integer, ObjectMapper>();

    /**
     * Convenience method to simply write an object to a json representation
     * using the given filters.
     * 
     * @param value
     *            Any object that can be serialized to json
     * @param filters
     *            The desired filters to be used
     * @return The json representation
     */
    public String writeCachedFiltered(final Object value, final String... filters) {
        try {
            int key = Arrays.hashCode(filters);
            if (!cache.containsKey(key)) {
                cache.put(key, jackson2Helper.buildObjectMapper(filters));
            }
            return cache.get(key).writeValueAsString(value);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not write object filtered.", ioe);
        }
    }

    public FilterBenchmark() {
        for (int i = 0; i < 1000; i++) {
            users.add(User.buildMySelf());
        }
    }

    @Benchmark
    public void measureCached() {
        for (int i = 0; i < 100; i++) {
            writeCachedFiltered(users, "*", i % 2 == 0 ? "-manager" : "-reports", i % 3 == 0 ? "-address" : "-reports");
        }
    }

    @Benchmark
    public void measureNotCached() {
        for (int i = 0; i < 100; i++) {
            jackson2Helper.writeFiltered(users, "*", i % 2 == 0 ? "-manager" : "-reports",
                    i % 3 == 0 ? "-address" : "-reports");
        }
    }

    @Benchmark
    public void measureWriter() throws JsonProcessingException {
        for (int i = 0; i < 100; i++) {
            ObjectWriter writer = jackson2Helper.getObjectMapper()
                    .writer(jackson2Helper.buildFilterProvider("*",
                            i % 2 == 0 ? "-manager" : "-reports",
                            i % 3 == 0 ? "-address" : "-reports"));
            writer.writeValueAsString(users);
        }
    }
}
