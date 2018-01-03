package de.False.BuildersWand;

import de.False.BuildersWand.ConfigurationFiles.Config;
import de.False.BuildersWand.items.Wand;
import de.False.BuildersWand.utilities.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor
{
    Config config;

    Commands(Config config)
    {
        this.config = config;
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
        if(!player.hasPermission("buildsWand.reload"))
        {
            MessageUtil.sendMessage(player, "noPermissions");
            return;
        }

        MessageUtil.sendMessage(player,"reload");
        config.load();
    }

    private void giveCommand(Player player, String[] args)
    {
        if(!player.hasPermission("buildsWand.give"))
        {
            MessageUtil.sendMessage(player, "noPermissions");
            return;
        }

        if(args.length == 1)
        {
            player.getInventory().addItem(Wand.getRecipeResult());
            return;
        }
        Player destPlayer = Bukkit.getPlayer(args[1]);

        if(destPlayer == null)
        {
            MessageUtil.sendMessage(player,"playerNotFound");
            return;
        }

        destPlayer.getInventory().addItem(Wand.getRecipeResult());
    }

    private void helpCommand(Player player)
    {
        MessageUtil.sendMessage(player,"/bw reload");
    }
}
