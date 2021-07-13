package de.False.BuildersWand.utilities;

import de.False.BuildersWand.ConfigurationFiles.Locales;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MessageUtil {
    private static String defaultLocale = "en_us";
    private static String preFix = "&bBuildersWand » &7";

    public static void sendMessage(CommandSender player, String messagePath) {
        player.sendMessage(colorize(preFix + getMessage(messagePath, player)));
    }

    public static void sendRawMessage(CommandSender player, String messagePath) {
        player.sendMessage(colorize(getMessage(messagePath, player)));
    }

    public static void sendRawPrefixMessage(Player player, String message) {
        player.sendMessage(colorize(preFix + message));
    }

    private static String getMessage(String messagePath, CommandSender player) {
        String locale = getPlayerLocale(player);
        HashMap<String, String> messages = getMessagesForLocale(locale);

        if (messages.containsKey(messagePath)) {
            return messages.get(messagePath);
        }

        return messagePath;
    }

    private static HashMap<String, String> getMessagesForLocale(String locale) {
        if (Locales.messages.containsKey(locale)) {
            return Locales.messages.get(locale);
        }

        return Locales.messages.get(defaultLocale);
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    private static String getPlayerLocale(CommandSender player) {
        if (player instanceof Player) {
            return ((Player) player).getLocale();
        }

        return defaultLocale;
    }

    public static String getText(String messagePath, Player player) {
        return getMessage(messagePath, player);
    }

    public static void sendSeparator(CommandSender player) {
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
    }
}
