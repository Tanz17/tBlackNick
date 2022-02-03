package ru.tanz.tblacknick;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Events implements Listener {
    TBlackNick plugin = TBlackNick.plugin();
    public static void setHealth(Player p, double hp){
        p.setMaxHealth(hp);
    }
    public void onDead(PlayerRespawnEvent e ){
        if(plugin.uuid.contains(e.getPlayer().getUniqueId())){
            for (String s : plugin.getConfig().getStringList("effect-on-give")) {
                PotionEffectType type = PotionEffectType.getByName(s.substring(0, s.indexOf(";")));
                e.getPlayer().addPotionEffect(new PotionEffect(type, 99999, Integer.valueOf(s.substring(s.lastIndexOf(";") + 1))));
            }

        }
    }
}













