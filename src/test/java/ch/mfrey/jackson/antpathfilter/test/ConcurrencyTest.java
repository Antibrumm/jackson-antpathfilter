package ch.mfrey.jackson.antpathfilter.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import ch.mfrey.jackson.antpathfilter.Jackson2Helper;

public class ConcurrencyTest {

    private final class UserRunner implements Callable<Object> {
        private final AtomicInteger count;
        private final User user;

        private UserRunner(User user, AtomicInteger count) {
            this.user = user;
            this.count = count;
        }

        public Object call() throws Exception {
            try {
                for (int i = 0; i < 30; i++) {
                    String json = jackson2Helper.writeFiltered(user, "*", i % 2 == 0 ? "-manager" : "-reports", i % 3 == 0 ? "-address" : "-reports");
                    if (i % 2 == 0 && i % 3 == 0) {
                        Assert.assertEquals("{\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"reports\":[{},{},{},{},{},{},{},{},{},{}]}", json);
                    } else if (i % 2 == 0) {
                        Assert.assertEquals("{\"address\":{},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\"}", json);
                    } else if (i % 3 == 0) {
                        Assert.assertEquals("{\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"manager\":{}}", json);
                    } else {
                        Assert.assertEquals("{\"address\":{},\"email\":\"somewhere@no.where\",\"firstName\":\"Martin\",\"lastName\":\"Frey\",\"manager\":{}}", json);
                    }
                }
                count.incrementAndGet();
            } catch (Throwable t) {
                System.out.println(t.getMessage());
            }
            return null;
        }
    }

    private final Jackson2Helper jackson2Helper = new Jackson2Helper();

    @Test
    public void testConcurrency() throws InterruptedException {
        final AtomicInteger count = new AtomicInteger(0);
        User user = User.buildMySelf();
        UserRunner runner = new UserRunner(user, count);

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            fixedThreadPool.submit(runner);
        }
        fixedThreadPool.shutdown();
        fixedThreadPool.awaitTermination(5, TimeUnit.MINUTES);
        Assert.assertEquals(1000, count.intValue());
    }

}
