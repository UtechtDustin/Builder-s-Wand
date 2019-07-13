package de.False.BuildersWand;

import de.False.BuildersWand.ConfigurationFiles.Config;
import de.False.BuildersWand.NMS.NMS;
import de.False.BuildersWand.items.Wand;
import de.False.BuildersWand.manager.WandManager;
import de.False.BuildersWand.utilities.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Commands implements CommandExecutor {
    private Config config;
    private WandManager wandManager;
    private NMS nms;

    Commands(Config config, WandManager wandManager, NMS nms) {
        this.nms = nms;
        this.config = config;
        this.wandManager = wandManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length < 1) {
            helpCommand(sender);
            return true;
        }

        switch (args[0]) {
            case "reload":
                reloadCommand(sender);
                break;
            case "give":
                giveCommand(sender, args);
                break;
            default:
                helpCommand(sender);
        }

        return true;
    }

    private void reloadCommand(CommandSender player) {
        if (player instanceof Player && !player.hasPermission("buildersWand.reload")) {
            MessageUtil.sendMessage(player, "noPermissions");
            return;
        }

        MessageUtil.sendMessage(player, "reload");
        config.load();
        wandManager.load();
    }

    private void giveCommand(CommandSender player, String[] args) {
        boolean isPlayerInstance = player instanceof Player;
        if (isPlayerInstance && !player.hasPermission("buildersWand.give")) {
            MessageUtil.sendMessage(player, "noPermissions");
            return;
        }
        Wand wand;
        Player destPlayer;

        if (args.length < 1) {
            helpCommand(player);
            return;
        } else if (args.length == 1 && isPlayerInstance) {
            wand = wandManager.getWandTier(1);
            destPlayer = (Player) player;

        } else if (args.length == 2) {
            destPlayer = Bukkit.getPlayer(args[1]);
            wand = wandManager.getWandTier(1);
        } else {
            wand = wandManager.getWandTier(Integer.parseInt(args[2]));
            destPlayer = Bukkit.getPlayer(args[1]);
        }

        if (destPlayer == null) {
            MessageUtil.sendMessage(player, "playerNotFound");
            return;
        } else if (wand == null) {
            MessageUtil.sendMessage(player, "wandNotFound");
            return;
        }

        ItemStack itemStack = wand.getRecipeResult();
        itemStack = nms.setTag(itemStack, "uuid", UUID.randomUUID() + "");
        destPlayer.getInventory().addItem(itemStack);
    }

    private void helpCommand(CommandSender player) {
        MessageUtil.sendSeparator(player);
        MessageUtil.sendRawMessage(player, "             &b&lBuildersWand help");
        player.sendMessage("");
        MessageUtil.sendRawMessage(player, "&e&l»&r&e /bw reload &7- Reloads the config file.");
        MessageUtil.sendRawMessage(player, "&e&l»&r&e /bw give <player> &7- Give the builderswand tier 1 to a player.");
        MessageUtil.sendRawMessage(player, "&e&l»&r&e /bw give <player> <tier> &7- Give the builderswand tier X to a player.");
        MessageUtil.sendSeparator(player);
    }
}
