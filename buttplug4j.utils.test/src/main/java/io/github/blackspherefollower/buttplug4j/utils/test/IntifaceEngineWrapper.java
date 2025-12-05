package io.github.blackspherefollower.buttplug4j.utils.test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class IntifaceEngineWrapper implements Closeable {

    public final int cport;
    public final int dport;
    private Process proc = null;
    private Path userConfig = null;

    public IntifaceEngineWrapper() throws Exception {
        cport = (int) (Math.random() * 63000) + 1025;
        dport = (int) (Math.random() * 63000) + 1025;

        setup(true, new ArrayList<String>());
    }

    public IntifaceEngineWrapper(int lport) throws Exception {
        cport = lport;
        dport = (int) (Math.random() * 63000) + 1025;

        setup(false, new ArrayList<String>());
    }

    public IntifaceEngineWrapper(ArrayList<String> extraArgs) throws Exception {
        cport = (int) (Math.random() * 63000) + 1025;
        dport = (int) (Math.random() * 63000) + 1025;

        setup(true, extraArgs);
    }

    public void setup(boolean listen, ArrayList<String> extraArgs) throws Exception {
        try {
            Runtime.getRuntime().exec("intiface-engine --version").waitFor();
        } catch (IOException e) {
            org.junit.jupiter.api.Assumptions.abort("intiface-engine not found, skipping tests");
        }

        Path userConfig = Files.createTempFile("user-config_", ".json");
        try (
                InputStream in = new BufferedInputStream(
                        getClass().getResourceAsStream("/user-config.json"));
                OutputStream out = new BufferedOutputStream(
                        Files.newOutputStream(userConfig.toFile().toPath()))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        }

        ArrayList<String> args = new ArrayList<>(Arrays.asList("intiface-engine", listen ? "--websocket-port" : "--websocket-client-address", listen ? String.valueOf(cport) : ("ws://127.0.0.1:" + String.valueOf(cport) + "/bob"), "--use-device-websocket-server", "--user-device-config-file", userConfig.toAbsolutePath().toString(), "--device-websocket-server-port", String.valueOf(dport), "--log", "TRACE"));
        args.addAll(extraArgs);
        ProcessBuilder pb = new ProcessBuilder(args);

        Path log = Files.createTempFile("intiface_", ".log");
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log.toFile()));
        proc = pb.start();

        Thread.sleep(500);
        if (!proc.isAlive()) {
            close();
            org.junit.jupiter.api.Assumptions.abort("intiface-engine not started, skipping tests");
        }
    }

    @Override
    public void close() {
        if (proc != null) {
            proc.destroyForcibly();
        }
        if (userConfig != null) {
            try {
                Files.deleteIfExists(userConfig);
            } catch (IOException ignore) {
            }
        }
    }
}