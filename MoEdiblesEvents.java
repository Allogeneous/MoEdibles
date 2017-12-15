package me.Allogeneous.MoEdibles;

import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.scheduler.BukkitRunnable;

public class MoEdiblesEvents implements Listener{
	
	private MoEdibles plugin;
	
	public MoEdiblesEvents(MoEdibles plugin){
		this.plugin = plugin;
	}
	
	//Checks if a player has decided to eat a consumable item
	
	@EventHandler
	public void onPlayerEat(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR){
			Player p = e.getPlayer();
			ItemStack hand = new ItemStack(Material.AIR);
			if(e.getHand() == EquipmentSlot.HAND){
				hand = p.getInventory().getItemInMainHand();
			}
			if(e.getHand() == EquipmentSlot.OFF_HAND){
				hand = p.getInventory().getItemInOffHand();
			}
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
				if(!e.getClickedBlock().getType().toString().contains("IRON")){
					if(e.getClickedBlock().getState() instanceof InventoryHolder || e.getClickedBlock().getType().toString().contains("DOOR") || e.getClickedBlock().getType().toString().contains("GATE") || e.getClickedBlock().getType().toString().contains("BUTTON") || e.getClickedBlock().getType().toString().contains("LEVER") || e.getClickedBlock().getType().toString().contains("TABLE") || e.getClickedBlock().getType().toString().contains("ANVIL") || e.getClickedBlock().getType().toString().contains("WORKBENCH")){
						return;
					}
				}
			}
			if(hand != null && hand.getType() != Material.AIR){
				if(p.getGameMode() != GameMode.SPECTATOR && p.getGameMode() != GameMode.CREATIVE && p.getFoodLevel() < 20 && !plugin.eatingPlayers.contains(p.getUniqueId())){
					int foodLevel = 0;
					float saturationLevel = 0;
					boolean found = true;
					switch(hand.getType()){
						case BROWN_MUSHROOM: 
							if(!p.isSneaking()){
								if(MoEdibles.containsLoreLine(hand, plugin.cookedMeta)){
									foodLevel = 4;
									saturationLevel = 2;
								}else{
									foodLevel = 2;
									saturationLevel = 1;
								}
							}else{
								found = false;
							}
							break;
						case RED_MUSHROOM:
							if(!p.isSneaking()){
								if(MoEdibles.containsLoreLine(hand, plugin.cookedMeta)){
									foodLevel = 4;
									saturationLevel = 2;
								}else{
									foodLevel = 2;
									saturationLevel = 1;
								}
							}else{
								found = false;
							}
							break;
						case SEEDS: 
							if(!p.isSneaking()){
								if(MoEdibles.containsLoreLine(hand, plugin.cookedMeta)){
									foodLevel = 2;
									saturationLevel = 1;
								}else{
									foodLevel = 1;
									saturationLevel = 0.5f;
								}
							}else{
								found = false;
							}
							break;
						case BEETROOT_SEEDS: 
							if(!p.isSneaking()){
								if(MoEdibles.containsLoreLine(hand, plugin.cookedMeta)){
									foodLevel = 2;
									saturationLevel = 1;
								}else{
									foodLevel = 1;
									saturationLevel = 0.5f;
								}
							}else{
								found = false;
							}
							break;
						case MELON_SEEDS: 
							if(!p.isSneaking()){
								if(MoEdibles.containsLoreLine(hand, plugin.cookedMeta)){
									foodLevel = 2;
									saturationLevel = 1;
								}else{
									foodLevel = 1;
									saturationLevel = 0.5f;
								}
							}else{
								found = false;
							}
							break;
						case PUMPKIN_SEEDS: 
							if(!p.isSneaking()){
								if(MoEdibles.containsLoreLine(hand, plugin.cookedMeta)){
									foodLevel = 2;
									saturationLevel = 1;
								}else{
									foodLevel = 1;
									saturationLevel = 0.5f;
								}
							}else{
								found = false;
							}
							break;
						case EGG:
							if(!p.isSneaking()){
								if(MoEdibles.containsLoreLine(hand, plugin.cookedMeta)){
									foodLevel = 2;
									saturationLevel = 2;
								}else{
									found = false;
								}
							}else{
								found = false;
							}
							break;
						case SUGAR:
							foodLevel = 2;
							saturationLevel = 3;
							break;
						case SPECKLED_MELON:
							foodLevel = 4;
							saturationLevel = 2;
							break;
						case CARROT_STICK:
							foodLevel = 3;
							saturationLevel = 3.6f;
							plugin.fishingPole.setDurability(hand.getDurability());
							p.getWorld().dropItem(p.getLocation(), plugin.fishingPole);
							break;
						case CARROT_ITEM: 
							if(MoEdibles.containsLoreLine(hand, plugin.cookedMeta)){
								foodLevel = 6;
								saturationLevel = 8;
							}else{
								found = false;
							}
							break;
						case RABBIT_FOOT: 
							if(MoEdibles.containsLoreLine(hand, plugin.cookedMeta)){
								foodLevel = 4;
								saturationLevel = 8;
							}else{
								foodLevel = 2;
								saturationLevel = 4;
							}
							break;
						case INK_SACK:
							if(hand.isSimilar(new Dye(DyeColor.BROWN).toItemStack())){
								if(!p.isSneaking()){
									foodLevel = 1;
									saturationLevel = 0.5f;
								}
							}
							break;
						case CHORUS_FRUIT_POPPED:
							foodLevel = 8;
							saturationLevel = 12.8f;
							break;
						case PUMPKIN:
							if(!p.isSneaking()){
								if(MoEdibles.containsLoreLine(hand, plugin.cookedMeta)){
									foodLevel = 6;
									saturationLevel = 6;
								}else{
									foodLevel = 3;
									saturationLevel = 3;
								}
							}else{
								found = false;
							}
							break;
						case BREAD:
							if(MoEdibles.containsLoreLine(hand, plugin.craftedMeta)){
								foodLevel = 40;
								saturationLevel = 40;
							}else{
								found = false;
							}
							break;
						default:
							found = false;
							break;
					}
					
					if(found){
						plugin.eatingPlayers.add(p.getUniqueId());
						new MoEdiblesEatWorker(p, hand, e.getHand(), p.getInventory().getHeldItemSlot(), foodLevel, saturationLevel, plugin).runTaskTimer(plugin, 0, 1);
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	//A bunch of creative inventory methods
	
	@EventHandler
	public void onPlayeUseCreativeInventory(InventoryClickEvent e){
		if(e.getClickedInventory() == null){
			return;
		}
		if(e.getWhoClicked() instanceof Player){
			Player p = (Player) e.getWhoClicked();
			if(p.getGameMode() == GameMode.CREATIVE && e.getClickedInventory().getTitle().equals("MoEdibles Foods")){
				e.setCancelled(true);
				if(e.getCursor().getType() != null && e.getCursor().getType() != Material.AIR){
					if(!e.getCurrentItem().isSimilar(e.getCursor())){
						e.getCursor().setAmount(0);
					}else{
						if(e.isRightClick()){
							e.getCursor().setAmount(0);
						}
						if(e.isLeftClick() && !e.isShiftClick()){
							if(e.getCurrentItem().isSimilar(e.getCursor())){
								if(e.getCursor().getAmount() < e.getCursor().getMaxStackSize()){
									e.getCursor().setAmount(e.getCursor().getAmount() + 1);
								}
							}
						}
						if(e.isLeftClick() && e.isShiftClick()){
							if(e.getCurrentItem().isSimilar(e.getCursor())){
								if(e.getCursor().getAmount() < e.getCursor().getMaxStackSize()){
									e.getCursor().setAmount(e.getCursor().getMaxStackSize());
								}
							}
						}
					}
				}else if(e.getCurrentItem().getType() != null && e.getCurrentItem().getType() != Material.AIR){
					e.setCancelled(true);
					if(e.isLeftClick() && e.isShiftClick()){
						ItemStack maxStack = new ItemStack(e.getCurrentItem());
						maxStack.setAmount(maxStack.getMaxStackSize());
						p.setItemOnCursor(maxStack);
					}
					
					if(e.isRightClick() && e.isShiftClick()){
						ItemStack maxStack = new ItemStack(e.getCurrentItem());
						maxStack.setAmount(maxStack.getMaxStackSize());
						p.setItemOnCursor(maxStack);
					}
					
					if(e.isLeftClick() && !e.isShiftClick()){
						p.setItemOnCursor(e.getCurrentItem());
					}
					
					if(e.isRightClick() && !e.isShiftClick()){
						p.setItemOnCursor(e.getCurrentItem());
					}
				}else{
					e.getCursor().setAmount(0);
				}
			}
			
			if(p.getOpenInventory().getTopInventory().getTitle().equals("MoEdibles Foods")){
				if(e.getClickedInventory().getTitle().equals("container.inventory")){
					if(e.isShiftClick()){
						e.setCancelled(true);
						e.getCurrentItem().setAmount(0);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void annoyingFoodInventoryDragEvent(InventoryDragEvent e){
		if(e.getWhoClicked() instanceof Player){
			Player p = (Player) e.getWhoClicked();
			if(p.getOpenInventory().getTopInventory().getTitle().equals("MoEdibles Foods")){
				new BukkitRunnable(){
					@Override
					public void run() {
						if(e.getType() == DragType.EVEN){
							p.setItemOnCursor(e.getInventory().getItem(e.getInventorySlots().iterator().next()));
						}
						MoEdibles.resetFoodInventory(e.getView().getTopInventory(), plugin);
					}
				}.runTaskLater(plugin, 1);
			}
		}
	}
	
	//Prevents players from eating the same food and getting 2 status updates from it
	
	@EventHandler
	public void onPlayerDoubleEat(PlayerItemConsumeEvent e){
		if(plugin.eatingPlayers.contains(e.getPlayer().getUniqueId())){
			e.setCancelled(true);
		}
	}
	
	//Makes sure chickens don't come out of cooked eggs XD
	
	@EventHandler
	public void onPlayerThrowCookedEgg(PlayerEggThrowEvent e){
		if(e.getPlayer().getInventory().getItemInMainHand() != null && e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR){
			if(e.getPlayer().getInventory().getItemInMainHand().isSimilar(plugin.foodItems[6])){
				e.setHatching(false);
			}
		}else if (e.getPlayer().getInventory().getItemInOffHand() != null && e.getPlayer().getInventory().getItemInOffHand().getType() != Material.AIR && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR){
			if(e.getPlayer().getInventory().getItemInOffHand().isSimilar(plugin.foodItems[6])){
				e.setHatching(false);
			}
		}
	}
	
	//Makes sure players can't place a cooked food block on the ground
	
	@EventHandler
	public void onPlayerPlaceFood(BlockPlaceEvent e){
		if(e.getHand() == EquipmentSlot.HAND){
			if(MoEdibles.containsLoreLine(e.getPlayer().getInventory().getItemInMainHand(), plugin.cookedMeta)){
				e.setCancelled(true);
			}
		}
		if(e.getHand() == EquipmentSlot.OFF_HAND){
			if(MoEdibles.containsLoreLine(e.getPlayer().getInventory().getItemInOffHand(), plugin.cookedMeta)){
				e.setCancelled(true);
			}
		}
		if(plugin.eatingPlayers.contains(e.getPlayer().getUniqueId())){
			e.setCancelled(true);
		}
		
	}
	
	//Makes sure cooked and crafted foods can't be used in crafting recipes 
	
	@EventHandler
	public void onPlayerCraftWithFood(PrepareItemCraftEvent e){
		for(int i = 1; i < e.getInventory().getContents().length; i++){
			if(e.getInventory().getContents()[i] == null || e.getInventory().getContents()[i].getType() == Material.AIR){
				continue;
			}
			if(MoEdibles.containsLoreLine(e.getInventory().getContents()[i], plugin.cookedMeta) || MoEdibles.containsLoreLine(e.getInventory().getContents()[i], plugin.craftedMeta)){
				e.getInventory().setResult(new ItemStack(Material.AIR));
			}
		}
	}
	
	//Smelting events
	
	@EventHandler
	public void onPlayerShiftFurnace(InventoryClickEvent e){
		if(e.isShiftClick() && e.getInventory().getType() == InventoryType.FURNACE){
			if(e.getWhoClicked() instanceof Player){
				Player p = (Player) e.getWhoClicked();
				new BukkitRunnable(){
						@Override
						public void run(){
							p.updateInventory();
						}
				}.runTaskLater(plugin, 1);
			}
		}
	}
	
	@EventHandler
	public void onSmeltStart(FurnaceBurnEvent e){
		if(e.getBlock().getType() == Material.FURNACE || e.getBlock().getType() == Material.BURNING_FURNACE){
			Furnace furnace = (Furnace) e.getBlock().getState();
			if(MoEdibles.containsLoreLine(furnace.getInventory().getSmelting(), plugin.cookedMeta) || MoEdibles.containsLoreLine(furnace.getInventory().getSmelting(), plugin.cookedMeta)){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onSmeltEnd(FurnaceSmeltEvent e){
		if(e.getBlock().getType() == Material.FURNACE || e.getBlock().getType() == Material.BURNING_FURNACE){
			Furnace furnace = (Furnace) e.getBlock().getState();
			if(MoEdibles.containsLoreLine(furnace.getInventory().getSmelting(), plugin.cookedMeta) || MoEdibles.containsLoreLine(furnace.getInventory().getSmelting(), plugin.cookedMeta)){
				e.setCancelled(true);
			}
		}
	}

}
