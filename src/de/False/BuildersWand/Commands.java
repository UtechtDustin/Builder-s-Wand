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
            MessageUtil.sendMessage(player,"/bw reload");
            return true;
        }

        if(args[0].equals("reload") && player.hasPermission("buildsWand.reload"))
        {
            MessageUtil.sendMessage(player,"reload");
            config.load();
            return true;
        }
        else if(args[0].equals("give"))
        {
            if(args.length == 2 && player.hasPermission("buildsWand.give"))
            {
                Player destPlayer = Bukkit.getPlayer(args[1]);

                if(!(destPlayer instanceof Player))
                {
                    MessageUtil.sendMessage(player,"playerNotFound");
                    return true;
                }

                destPlayer.getInventory().addItem(Wand.getRecipeResult());
                return true;
            }

            player.getInventory().addItem(Wand.getRecipeResult());

            return true;
        }

        return true;
    }
}
