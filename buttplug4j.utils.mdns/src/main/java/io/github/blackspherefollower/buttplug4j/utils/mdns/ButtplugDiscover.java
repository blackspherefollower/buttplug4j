package io.github.blackspherefollower.buttplug4j.utils.mdns;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;
import java.io.Closeable;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public final class ButtplugDiscover implements ServiceListener, Closeable {
    private final ConcurrentHashMap<String, ConcurrentSkipListSet<URI>> servers = new ConcurrentHashMap<>();
    private final JmmDNS jmmdns;
    private DiscovereyEventHandler discovereyEventHandler = null;

    public ButtplugDiscover(DiscovereyEventHandler discovereyEventHandler) {
        this.discovereyEventHandler = discovereyEventHandler;
        jmmdns = JmmDNS.Factory.getInstance();
        jmmdns.addServiceListener("_intiface_engine._tcp.local.", this);
    }


    @Override
    public void serviceAdded(ServiceEvent serviceEvent) {
        // Not used
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        servers.remove(event.getInfo().getName());
        if (discovereyEventHandler != null) {
            discovereyEventHandler.LostButtplug(event.getInfo().getName());
        }
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        ConcurrentSkipListSet<URI> set = new ConcurrentSkipListSet<>();
        for (Inet4Address addr : event.getInfo().getInet4Addresses()) {
            try {
                set.add(new URI("ws://" + addr.getHostAddress() + ":" + event.getInfo().getPort()));
            } catch (URISyntaxException e) {
                // Log weirdness
            }
        }
        ConcurrentSkipListSet<URI> set2 = servers.putIfAbsent(event.getName(), set);
        if (set2 != null) {
            for (URI uri : set) {
                set2.add(uri);
            }
        }

        if (discovereyEventHandler != null) {
            discovereyEventHandler.FoundButtplug(event.getName(), set);
        }
    }

    public ConcurrentHashMap<String, ConcurrentSkipListSet<URI>> GetServers() {
        return servers;
    }

    @Override
    public void close() throws IOException {
        jmmdns.close();
    }

    public interface DiscovereyEventHandler {
        void FoundButtplug(String name, Set<URI> addresses);

        void LostButtplug(String name);
    }

}
