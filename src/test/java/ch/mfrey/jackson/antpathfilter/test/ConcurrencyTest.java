package ch.mfrey.jackson.antpathfilter.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.mfrey.jackson.antpathfilter.Jackson2Helper;

public class ConcurrencyTest {

    private final Jackson2Helper jackson2Helper = new Jackson2Helper();

    @Test
    public void testExclusion() throws JsonProcessingException, InterruptedException {
        final AtomicInteger count = new AtomicInteger(0);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            fixedThreadPool.submit(new Runnable() {
                public void run() {
                    User user = User.buildMySelf();
                    long id = Thread.currentThread().getId();
                    String json = jackson2Helper.writeFiltered(user, "*", id % 2 == 0 ? "-manager" : "-reports", id % 3 == 0 ? "-address" : "-reports");
                    try {
                        if (id % 2 == 0 && id % 3 == 0) {
                            Assert.assertEquals("{\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"reports\":[{},{},{},{},{},{},{},{},{},{}]}", json);
                        } else if (id % 2 == 0) {
                            Assert.assertEquals("{\"address\":{},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\"}", json);
                        } else if (id % 3 == 0) {
                            Assert.assertEquals("{\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"manager\":{}}", json);
                        } else {
                            Assert.assertEquals("{\"address\":{},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"manager\":{}}", json);
                        }
                        count.incrementAndGet();
                    } catch (Throwable t) {
                        System.out.println(t.getMessage());
                    }
                }
            });
        }
        fixedThreadPool.shutdown();
        fixedThreadPool.awaitTermination(1, TimeUnit.MINUTES);
        Assert.assertEquals(1000, count.intValue());
    }

}
