package dev.efnilite.witp.util.web;

import dev.efnilite.witp.WITP;
import dev.efnilite.witp.util.Verbose;
import dev.efnilite.witp.util.task.Tasks;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

public class UpdateChecker {

    public void check() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                String latest;
                String forkLatest;
                try {
                    latest = getLatestVersion();
                    forkLatest = getLatestForkVersion();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Verbose.error("Error while trying to fetch latest version!");
                    return;
                }

                if(!forkLatest.equals(WITP.getInstance().getDescription().getVersion())) {
                        Verbose.info("A new version of forked WITP is available to download!");
                        Verbose.info("Newest version: " + forkLatest);
                        WITP.isOutdated = true;
                } else {
                    Verbose.info("WITP fork is is currently up-to-date! v"+forkLatest+" (official: v"+latest+")");
                }
            }
        };
        Tasks.asyncTask(runnable);
    }

    private String getLatestVersion() throws IOException {
        InputStream stream;
        Verbose.info("Checking for updates...");
        try {
            stream = new URL("https://raw.githubusercontent.com/Efnilite/Walk-in-the-Park/master/src/main/resources/plugin.yml").openStream();
        } catch (IOException e) {
            Verbose.info("Unable to check for updates!");
            return "";
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String version = reader.lines()
                .filter(s -> s.contains("version: ") && !s.contains("api"))
                .collect(Collectors.toList())
                .get(0)
                .replace("version: ", "");
        stream.close();
        reader.close();
        return version;
    }

    private String getLatestForkVersion() throws IOException {
        InputStream stream;
        Verbose.info("Checking for updates...");
        try {
            stream = new URL("https://raw.githubusercontent.com/Mrwere/Walk-in-the-Park/main/src/main/resources/plugin.yml").openStream();
        } catch (IOException e) {
            Verbose.info("Unable to check for updates!");
            return "";
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String version = reader.lines()
                .filter(s -> s.contains("version: ") && !s.contains("api"))
                .collect(Collectors.toList())
                .get(0)
                .replace("version: ", "");
        stream.close();
        reader.close();
        return version;
    }

}