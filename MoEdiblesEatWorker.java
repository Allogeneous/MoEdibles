package me.Allogeneous.MoEdibles;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MoEdiblesEatWorker extends BukkitRunnable{

	private Player player;
	private int foodLevel, initialSlotNumber;
	private EquipmentSlot hand;
	private float saturationLevel;
	private ItemStack initialItemInHand;
	private MoEdibles plugin;
	private int iterations = 0;
	
	public MoEdiblesEatWorker(Player player, ItemStack initialItemInHand, EquipmentSlot hand, int initialSlotNumber, int foodLevel, float saturationLevel, MoEdibles plugin){
		this.player = player;
		this.initialItemInHand = initialItemInHand;
		this.hand = hand;
		this.initialSlotNumber = initialSlotNumber;
		this.foodLevel = foodLevel;
		this.saturationLevel = saturationLevel;
		this.plugin = plugin;
	}
	
	//This runs every tick for 30 ticks, this is the player eat cycle
	
	@Override
	public void run() {
		if(iterations == 0){
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 35, 4));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 35, 50));
		}
		if(player.getInventory().getHeldItemSlot() != initialSlotNumber){
			player.removePotionEffect(PotionEffectType.SLOW);
			player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
			plugin.eatingPlayers.remove(player.getUniqueId());
			this.cancel();
		}
		if(hand == EquipmentSlot.HAND){
			if(!player.getInventory().getItemInMainHand().equals(initialItemInHand)){
				player.removePotionEffect(PotionEffectType.SLOW);
				player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
				plugin.eatingPlayers.remove(player.getUniqueId());
				this.cancel();
			}
		}
		if(hand == EquipmentSlot.OFF_HAND){
			if(!player.getInventory().getItemInOffHand().equals(initialItemInHand)){
				player.removePotionEffect(PotionEffectType.SLOW);
				player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
				plugin.eatingPlayers.remove(player.getUniqueId());
				this.cancel();
			}
		}
		
		if(iterations % 4 == 0 && iterations < 28){
			player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 3.0f, 0.95f);
		}
		if(iterations >= 30){
			plugin.eatingPlayers.remove(player.getUniqueId());
			PlayerItemConsumeEvent pice = new PlayerItemConsumeEvent(player, player.getInventory().getItemInMainHand());
			plugin.getServer().getPluginManager().callEvent(pice);
			player.removePotionEffect(PotionEffectType.SLOW);
			player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
			if(!pice.isCancelled()){
				player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
				player.setFoodLevel(player.getFoodLevel() + foodLevel);
				player.setSaturation(player.getSaturation() + saturationLevel);
			}
			this.cancel();
		}
		iterations+=1;
	}

}
