package de.False.BuildersWand.utilities;

import de.False.BuildersWand.ConfigurationFiles.Locales;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MessageUtil
{
    private static String defaultLocale = "en_us";
    private static String preFix = "&bBuildersWand » &7";

    public static void sendMessage(Player player, String messagePath)
    {
        player.sendMessage(colorize(preFix + getMessage(messagePath, player)));
    }

    public static void sendRawMessage(Player player, String messagePath)
    {
        player.sendMessage(colorize(getMessage(messagePath, player)));
    }

    private static String getMessage(String messagePath, Player player)
    {
        String locale = getPlayerLocale(player);
        HashMap<String, String> messages = getMessagesForLocale(locale);

        if(messages.containsKey(messagePath))
        {

            return messages.get(messagePath);
        }

        return messagePath;
    }

    private static HashMap<String, String> getMessagesForLocale(String locale)
    {
        if(Locales.messages.containsKey(locale))
        {
            return Locales.messages.get(locale);
        }

        return Locales.messages.get(defaultLocale);
    }

    public static String colorize(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    private static String getPlayerLocale(Player player)
    {
        return player.spigot().getLocale();
    }

    public static String getText(String messagePath, Player player)
    {
        return getMessage(messagePath, player);
    }

    public static void sendSeperator(Player player)
    {
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
    }
}
