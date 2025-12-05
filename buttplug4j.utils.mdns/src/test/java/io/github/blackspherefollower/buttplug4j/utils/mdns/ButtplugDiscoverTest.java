package io.github.blackspherefollower.buttplug4j.utils.mdns;

import io.github.blackspherefollower.buttplug4j.utils.test.IntifaceEngineWrapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ButtplugDiscoverTest {

    @Disabled("Not able to get mDNS this working right now")
    @Test
    public void TestConnect() throws Exception {

        CompletableFuture<Boolean> seen1 = new CompletableFuture<>();
        CompletableFuture<Boolean> seen2 = new CompletableFuture<>();
        CompletableFuture<Boolean> gone1 = new CompletableFuture<>();
        CompletableFuture<Boolean> gone2 = new CompletableFuture<>();
        try (
                IntifaceEngineWrapper wrapper1 = new IntifaceEngineWrapper(new ArrayList<>(Arrays.asList("--broadcast-server-mdns")));
                IntifaceEngineWrapper wrapper2 = new IntifaceEngineWrapper(new ArrayList<>(Arrays.asList("--broadcast-server-mdns", "--mdns-suffix", "test-intiface-2")));

                ButtplugDiscover discover = new ButtplugDiscover(new ButtplugDiscover.DiscovereyEventHandler() {
                    @Override
                    public void FoundButtplug(String name, Set<URI> addresses) {
                        System.out.println("WOO, a buttplug appeared : " + name + " : " + String.join(", ", addresses.stream().map(uri -> uri.toString()).collect(Collectors.toList())));
                    }

                    @Override
                    public void LostButtplug(String name) {
                        System.out.println("Oh... a buttplug disappeared : " + name);
                    }
                });
        ) {

            assertTrue(seen1.get(2, TimeUnit.MINUTES));
            assertTrue(seen2.get(15, TimeUnit.SECONDS));

            assertFalse(gone1.isDone());
            wrapper1.close();
            assertTrue(gone1.get(15, TimeUnit.SECONDS));

            assertFalse(gone2.isDone());
            wrapper2.close();
            assertTrue(gone2.get(15, TimeUnit.SECONDS));
        }
    }
}