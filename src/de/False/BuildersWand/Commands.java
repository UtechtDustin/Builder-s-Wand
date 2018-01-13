package de.False.BuildersWand;

import de.False.BuildersWand.ConfigurationFiles.Config;
import de.False.BuildersWand.events.WandEvents;
import de.False.BuildersWand.items.Wand;
import de.False.BuildersWand.manager.WandManager;
import de.False.BuildersWand.utilities.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor
{
    private Config config;
    private WandManager wandManager;

    Commands(Config config, WandManager wandManager)
    {
        this.config = config;
        this.wandManager = wandManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(!(sender instanceof Player))
        {
            return true;
        }

        Player player = (Player) sender;
        if(args.length < 1)
        {
            helpCommand(player);
            return true;
        }

        switch (args[0])
        {
            case "reload":
                reloadCommand(player);
                break;
            case "give":
                giveCommand(player, args);
                break;
            default:
                helpCommand(player);
        }

        return true;
    }

    private void reloadCommand(Player player)
    {
        if(!player.hasPermission("buildersWand.reload"))
        {
            MessageUtil.sendMessage(player, "noPermissions");
            return;
        }

        MessageUtil.sendMessage(player,"reload");
        config.load();
        wandManager.load();
    }

    private void giveCommand(Player player, String[] args)
    {
        if(!player.hasPermission("buildersWand.give"))
        {
            MessageUtil.sendMessage(player, "noPermissions");
            return;
        }
        Wand wand;
        Player destPlayer;

        if(args.length < 1)
        {
            helpCommand(player);
            return;
        }
        else if(args.length == 1)
        {
            wand = wandManager.getWandTier(1);
            destPlayer = player;

        }
        else if(args.length == 2)
        {
            destPlayer = Bukkit.getPlayer(args[1]);
            wand = wandManager.getWandTier(1);
        }
        else
        {
            wand = wandManager.getWandTier(Integer.parseInt(args[2]));
            destPlayer = Bukkit.getPlayer(args[1]);
        }

        if(destPlayer == null)
        {
            MessageUtil.sendMessage(player,"playerNotFound");
            return;
        }
        else if(wand == null)
        {
            MessageUtil.sendMessage(player,"wandNotFound");
            return;
        }

        destPlayer.getInventory().addItem(wand.getRecipeResult());
    }

    private void helpCommand(Player player)
    {
        MessageUtil.sendSeparator(player);
        MessageUtil.sendRawMessage(player,"             &b&lBuildersWand help");
        player.sendMessage("");
        MessageUtil.sendRawMessage(player,"&e&l»&r&e /bw reload &7- Reloads the config file.");
        MessageUtil.sendRawMessage(player,"&e&l»&r&e /bw give <player> &7- Give the builderswand tier 1 to a player.");
        MessageUtil.sendRawMessage(player,"&e&l»&r&e /bw give <player> <tier> &7- Give the builderswand tier X to a player.");
        MessageUtil.sendSeparator(player);
    }
}
