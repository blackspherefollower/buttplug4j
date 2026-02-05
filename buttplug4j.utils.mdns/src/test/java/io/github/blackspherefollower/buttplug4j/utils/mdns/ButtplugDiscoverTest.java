package io.github.blackspherefollower.buttplug4j.utils.mdns;

import io.github.blackspherefollower.buttplug4j.utils.test.IntifaceEngineWrapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ButtplugDiscoverTest {

    @Test
    public void TestConnect() throws Exception {

        CompletableFuture<Boolean> seen1 = new CompletableFuture<>();
        CompletableFuture<Boolean> gone1 = new CompletableFuture<>();

        try (
                IntifaceEngineWrapper wrapper1 = new IntifaceEngineWrapper(new ArrayList<>(Arrays.asList("--broadcast-server-mdns")));
                ButtplugDiscover discover = new ButtplugDiscover(new ButtplugDiscover.DiscovereyEventHandler() {
                    @Override
                    public void FoundButtplug(String name, Set<URI> addresses) {
                        seen1.complete(true);
                        System.out.println("WOO, a buttplug appeared : " + name + " : " + String.join(", ", addresses.stream().map(uri -> uri.toString()).collect(Collectors.toList())));
                    }

                    @Override
                    public void LostButtplug(String name) {
                        gone1.complete(true);
                        System.out.println("Oh... a buttplug disappeared : " + name);
                    }
                });
        ) {

            assertTrue(seen1.get(15, TimeUnit.SECONDS));

            assertFalse(gone1.isDone());
            wrapper1.close();

            // Looks like a mdns service going offline doesn't mean it's deregistered
            //assertTrue(gone1.get(1, TimeUnit.MINUTES));
        }
    }
}