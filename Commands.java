package ru.tanz.tblacknick;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PrefixNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.w3c.dom.Node;

import java.util.UUID;

public class Commands implements CommandExecutor {
    TBlackNick plugin = TBlackNick.plugin();
    String c(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
    LuckPerms api = provider.getProvider();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tblacknick")) {
            if (args.length < 1) {
                for (String s : plugin.getConfig().getStringList("help-message")) {
                    sender.sendMessage(c(s));
                }
                return true;
            }
            if (!sender.hasPermission("tblacknick.use")) {
                sender.sendMessage(c(plugin.getConfig().getString("no-perm")));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(c(plugin.getConfig().getString("reload")));
                plugin.reloadConfig();
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            CachedMetaData metaData = api.getPlayerAdapter(Player.class).getMetaData(target);
            String prefix = metaData.getPrefix();
            User user = api.getUserManager().getUser(target.getUniqueId());





            if (args[0].equalsIgnoreCase("set") && target.isOnline()) {
                if(plugin.uuid.contains(target.getUniqueId())){
                    sender.sendMessage(c(plugin.getConfig().getString("have-blacknick")));
                    return true;
                }
                if (!plugin.uuid.contains(target.getUniqueId())) {
                    plugin.uuid.add(target.getUniqueId());
                    Events.setHealth(target, plugin.getConfig().getInt("on-blacknick.hp")*2);
                    target.sendTitle(c(plugin.getConfig().getString("on-blacknick.title")), c(plugin.getConfig().getString("on-blacknick.subtitle")));
                    for (String s : plugin.getConfig().getStringList("on-blacknick.message")) {
                        target.sendMessage(c(s));
                    }
                    if(plugin.uuid.contains(target.getUniqueId())) {
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.sendMessage(c(plugin.getConfig().getString("on-blacknick.who")).replace("{target}", target.getName()).replace("{player}", sender.getName()));
                        }
                    }
                    for (String s : plugin.getConfig().getStringList("effect-on-give")) {
                        PotionEffectType type = PotionEffectType.getByName(s.substring(0, s.indexOf(";")));
                        target.addPotionEffect(new PotionEffect(type, 99999, Integer.valueOf(s.substring(s.lastIndexOf(";") + 1))));
                    }
                    target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 99999));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                        @Override
                        public void run() {
                            target.removePotionEffect(PotionEffectType.BLINDNESS);
                        }
                    }, 20 * 5);
                }

                target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 99999, 1));
                user.data().add(PrefixNode.builder(prefix.replace(prefix.substring(prefix.length() - 1), "0"), 2).build());
                user.data().remove(PrefixNode.builder(prefix, 2).build());
                api.getUserManager().saveUser(user);

            }
            if (args[0].equalsIgnoreCase("remove") && target.isOnline()) {
                if(!plugin.uuid.contains(target.getUniqueId())){
                    sender.sendMessage(c(plugin.getConfig().getString("not-have-blacknick")));
                    return true;
                }
                if (plugin.uuid.contains(target.getUniqueId())) {
                    plugin.uuid.remove(target.getUniqueId());
                    Events.setHealth(target, 20);
                    target.setHealth(20);
                    plugin.uuid.remove(target.getUniqueId());
                    target.sendTitle(c(plugin.getConfig().getString("off-blacknick.title")), c(plugin.getConfig().getString("off-blacknick.subtitle")));
                    for (String s : plugin.getConfig().getStringList("off-blacknick.message")) {
                        target.sendMessage(c(s));
                    }
                    for (String s : plugin.getConfig().getStringList("effect-on-give")) {
                        PotionEffectType type = PotionEffectType.getByName(s.substring(0, s.indexOf(";")));
                        target.removePotionEffect(type);
                    }
                    user.data().remove(PrefixNode.builder(api.getPlayerAdapter(Player.class).getMetaData(target).getPrefix(), 2).build());
                    user.data().add(PrefixNode.builder(prefix.replace(prefix.substring(prefix.length() - 1), "f"), 2).build());
                    api.getUserManager().saveUser(user);
                }
            }
        }
                return false;
            }
}























