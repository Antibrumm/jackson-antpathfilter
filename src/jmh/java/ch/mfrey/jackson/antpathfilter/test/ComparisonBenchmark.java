package ch.mfrey.jackson.antpathfilter.test;

import java.util.ArrayList;
import java.util.List;
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

import ch.mfrey.jackson.antpathfilter.Jackson2Helper;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@Threads(5)
public class ComparisonBenchmark {

    private final Jackson2Helper jackson2Helper = new Jackson2Helper();
    private final List<User> users = new ArrayList<User>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public ComparisonBenchmark() {
        for (int i = 0; i < 10; i++) {
            users.add(User.buildRecursive(100));
        }
    }

    @Benchmark
    public void measureFiltered() {
        jackson2Helper.writeFiltered(users,
                "*", "**.manager.*", "**.address.streetName", "**.reports.lastName");
    }

    @Benchmark
    public void measureSpecificFilters() {
        jackson2Helper.writeFiltered(users,
                "firstName", "lastName", "email", "manager.firstName", "manager.lastName");
    }

    @Benchmark
    public void measureStandard() throws JsonProcessingException {
        objectMapper.writer().writeValueAsString(users);
    }
}
