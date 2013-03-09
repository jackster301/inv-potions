package com.glen3b.plugin.invpotions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.potion.PotionEffect;
//import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InventoryPotionEffects extends JavaPlugin {
	
	private int taskid;
	
	protected boolean nonstandardHelmet(PlayerInventory pi){
		ItemStack h = pi.getHelmet();
		return h != null && h.getType() != Material.LEATHER_HELMET && h.getType() != Material.GOLD_HELMET && h.getType() != Material.CHAINMAIL_HELMET && h.getType() != Material.IRON_HELMET && h.getType() != Material.DIAMOND_HELMET;
	}
	
	@Override
	public void onEnable(){
		this.saveDefaultConfig();
	    taskid = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
	    	
	        @Override  
	        public void run() {
	        	Iterator<Entry<String, Object>> nondeepconfig = getConfig().getDefaultSection().getValues(false).entrySet().iterator();
	            for(Player p : getServer().getOnlinePlayers()) {
	            	if(p != null){
	            		PlayerInventory pi = p.getInventory();
	            		ItemStack[] parmor = pi.getArmorContents();
	            		while(nondeepconfig.hasNext()){
	            			Entry<String, Object> entry = nondeepconfig.next();
	            			String basekey = entry.getKey()+".";
	            			List<ArmorConfigValueType> armorcfg = ArmorConfigValueType.populateList(new String[]{
	            					getConfig().getString(basekey+"armor.helmet"),
	            					getConfig().getString(basekey+"armor.chestplate"),
	            					getConfig().getString(basekey+"armor.leggings"),
	            					getConfig().getString(basekey+"armor.boots")});
	            			Material[] armor = new Material[]{
	            					Material.getMaterial(getConfig().getString(basekey+"armor.helmet")),
	            					Material.getMaterial(getConfig().getString(basekey+"armor.chestplate")),
	            					Material.getMaterial(getConfig().getString(basekey+"armor.leggings")),
	            					Material.getMaterial(getConfig().getString(basekey+"armor.boots"))};
	            			//getLogger().log(Level.INFO, "Configuration prefix is "+basekey+", and their is a helmet of: "+getConfig().getString(basekey+"armor.helmet")+" with a material of "+armor[0]+", "+p.getDisplayName()+" has a helmet of "+parmor[3]);
	            			boolean armorvalid = true;
	            			for(int i = 0; i < 4; i++){
		            			if(
		            					!((armorcfg.get(i) == ArmorConfigValueType.Acknowledge && parmor[3-i].getType() == armor[i])
		            					|| (armorcfg.get(i) == ArmorConfigValueType.NonStandard && nonstandardHelmet(pi))
		            					|| (armorcfg.get(i) == ArmorConfigValueType.Ignore))){
		            				armorvalid = false;
		            			}
		            			}
		            		}
	            		}
	                
	            }
	        
	        }
	    }, 250L, 100L);
	}
	
	@Override
	public void onDisable(){
		this.getServer().getScheduler().cancelTask(taskid);
	}

}
