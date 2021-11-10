package me.dragonsteam.grapple.commands;

import me.dragonsteam.grapple.Grapple;
import me.dragonsteam.grapple.utils.GrappleUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FishCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!sender.hasPermission(GrappleUtils.getGrapplePermission("help"))) {
                noPermission(sender);
                return false;
            }

            sendHelp(sender);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                if (!sender.hasPermission(GrappleUtils.getGrapplePermission("help"))) {
                    noPermission(sender);
                    return false;
                }

                sendHelp(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission(GrappleUtils.getGrapplePermission("staff"))) {
                    noPermission(sender);
                    return false;
                }

                sender.sendMessage(Grapple.translate(Grapple.INSTANCE.getConfig().getString("MESSAGES.RELOADING")));
                Grapple.INSTANCE.reloadConfig();
                return true;
            }

            if (args[0].equalsIgnoreCase("give")) {
                if (!sender.hasPermission(GrappleUtils.getGrapplePermission("give"))) {
                    noPermission(sender);
                    return false;
                }

                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    GrappleUtils.giveGrappleItem(p);
                    sender.sendMessage(Grapple.translate(Grapple.INSTANCE.getConfig().getString("MESSAGES.GRAPPLE-GIVEN")));
                } else sender.sendMessage("Only players can execute this command.");
                return true;
            }
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission(GrappleUtils.getGrapplePermission("give") + ".others")) {
                noPermission(sender);
                return false;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Grapple.translate(Grapple.INSTANCE.getConfig().getString("MESSAGES.NO-PLAYER").replace("<grapple_player>", args[1])));
                return true;
            }

            GrappleUtils.giveGrappleItem(target);
            sender.sendMessage(Grapple.translate(Grapple.INSTANCE.getConfig().getString("MESSAGES.GRAPPLE-FORCE-GIVEN").replace("<grapple_player>", args[1])));
        }

        return true;
    }

    private void noPermission(CommandSender sender) {
        sender.sendMessage(Grapple.translate(Grapple.INSTANCE.getConfig().getString("MESSAGES.NO-PERMISSION")));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "GRAPPLE V" + Grapple.INSTANCE.getDescription().getVersion() + " INFO");
        if (sender.hasPermission(GrappleUtils.getGrapplePermission("staff")))
            sender.sendMessage(Grapple.translate("&f* &e/grapple reload &7| &fReload grapple config."));
        sender.sendMessage(Grapple.translate("&f* &e/grapple give (player) &7| &fGive grapple item."));
        sender.sendMessage(Grapple.translate("&f* &e/grapple help &7| &fShow this help page."));
        sender.sendMessage("");
        sender.sendMessage(Grapple.translate("&f* &fPlugin created by &5Joansiitoh&f."));
    }

}