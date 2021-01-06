package dev.efnilite.witp.command;

import dev.efnilite.witp.player.ParkourPlayer;
import dev.efnilite.witp.util.Util;
import dev.efnilite.witp.util.Verbose;
import dev.efnilite.witp.util.wrapper.BukkitCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainCommand extends BukkitCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = null;
        if (sender instanceof Player)   player = (Player) sender;


        if (args.length == 0) {
            sender.sendMessage(Util.color("&7--------------- &aWITPfork &7---------------"));
            sender.sendMessage(Util.color("&a/witp &f- &7Main command"));
            sender.sendMessage(Util.color("&a/witp join [player] &f- &7Join the game on this server or make another player join"));
            sender.sendMessage(Util.color("&a/witp leave &f- &7Leave the game on this server"));
            sender.sendMessage(Util.color("&a/witp menu &f- &7Open the customization menu"));
            sender.sendMessage(Util.color("&a/witp leaderboard &f- &7Open the leaderboard"));
            return true;
        } else if (args.length == 1) {
            if (player == null) {
                return true;
            }
            if (args[0].equalsIgnoreCase("join")) {
                try {
                    ParkourPlayer.register(player);
                    ParkourPlayer pp = ParkourPlayer.getPlayer(player);
                    if (pp != null) {
                        pp.send("&aYou joined the parkour");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Verbose.error("Error while joining");
                }
            } else if (args[0].equalsIgnoreCase("leave")) {
                ParkourPlayer pp = ParkourPlayer.getPlayer(player);
                if (pp != null) {
                    try {
                        pp.send("&cYou left the parkour");
                        ParkourPlayer.unregister(pp, true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Verbose.error("Error while leaving");
                    }
                }
            } else if (args[0].equalsIgnoreCase("menu")) {
                ParkourPlayer pp = ParkourPlayer.getPlayer(player);
                if (pp != null) {
                    pp.menu();
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("leaderboard") && args[1] != null && player != null) {
                int page = Integer.parseInt(args[1]);
                ParkourPlayer pp = ParkourPlayer.getPlayer(player);
                if (pp != null) {
                    pp.scoreboard(page);
                }
            } else if (args[0].equalsIgnoreCase("join") && args[1] != null) {
                if (sender.isOp()) {
                    Player join = Bukkit.getPlayer(args[1]);
                    if (join == null) {
                        Verbose.error("Player " + args[1] + " doesn't exist!");
                        return true;
                    }
                    try {
                        ParkourPlayer.register(join);
                        ParkourPlayer pp = ParkourPlayer.getPlayer(join);
                        if (pp != null) {
                            pp.send("&aYou joined the parkour");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Verbose.error("Error while joining");
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return Arrays.asList("join", "leave", "leaderboard", "menu");
    }
}
