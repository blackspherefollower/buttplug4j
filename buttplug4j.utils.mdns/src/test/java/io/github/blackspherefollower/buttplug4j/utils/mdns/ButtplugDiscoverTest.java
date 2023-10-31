package io.github.blackspherefollower.buttplug4j.utils.mdns;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

public class ButtplugDiscoverTest {

    @Disabled
    @Test
    public void TestConnect() throws Exception {

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

        Thread.sleep(60000);
    }
}