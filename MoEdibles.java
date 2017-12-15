package me.Allogeneous.MoEdibles;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MoEdibles extends JavaPlugin implements Listener{
	
	//List of players who have right clicked a consumable item and have started a MoEdiblesEatWorker
	protected List<UUID> eatingPlayers = new ArrayList<>();
	//List of all the cooked food material types
	private Material[] cookedFoodTypes = {Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.SEEDS, Material.BEETROOT_SEEDS, Material.MELON_SEEDS, Material.PUMPKIN_SEEDS, Material.EGG, Material.CARROT_ITEM, Material.RABBIT_FOOT, Material.PUMPKIN};
	//List of cooked food names, \"Cooked \" is added to the name during initialization cause who wants to type out cooked 10 times XD
	private String[] cookedFoodNames = {"Brown Mushroom", "Red Mushroom,", "Seeds", "Beetroot Seeds", "Mellon Seeds", "Pumpkin Seeds", "Egg", "Carrot", "Rabbit's Foot", "Pumpkin"};
	//Special invisible item meta line that goes on a cooked food so it can be differentiated from a non cooked food
	protected final String cookedMeta = hideMetaStrings("MoEdibles.CookedFood");
	//Special invisible item meta line that goes on a crafted food, specifically manna so the player cannot craft manna with manna
	protected final String craftedMeta = hideMetaStrings("MoEdibles.CraftedFood");
	//Used when a player eats a carrot on a stick
	protected final ItemStack fishingPole = new ItemStack(Material.FISHING_ROD);
	//List of actually food items that can be given as itemstacks
	protected ItemStack[] foodItems;
	//Named of foods used in give commands
	protected final String[] foodItemsCommandNames = new String[]{"cooked_brown_mushroom", "cooked_red_mushroom", "cooked_seeds", "cooked_beetroot_seeds", "cooked_mellon_seeds", "cooked_pumpkin_seeds", "cooked_egg", "cooked_carrot", "cooked_rabbits_foot", "cooked_pumpkin", "manna", "nuka_cola", "nuka_cola_quantum"};
	//Booleans to determine if break, nuka-cola, or nuka-cola quantum recipes and items should be used
	private boolean godBread, nk, nkq, godBreadR, nkR, nkqR;
	
	@Override
	public void onEnable(){
		createConfig();
		verifyConfigVersion();
		//Get booleans from config
		godBread = getConfig().getBoolean("useOpBread", false);
		nk = getConfig().getBoolean("useNK", false);
		nkq = getConfig().getBoolean("useNKQ", false);
		godBreadR = getConfig().getBoolean("useOpBreadRecipe", false);
		nkR = getConfig().getBoolean("useNKRecipe", false);
		nkqR = getConfig().getBoolean("useNKQRecipe", false);
		registerRecipes();
		getServer().getPluginManager().registerEvents(new MoEdiblesEvents(this), this);
		getCommand("moedibles").setTabCompleter(new MoEdiblesTabCompleter(this));
	}
	
	@Override
	public void onDisable(){
		
	}
	
	//Command junk
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
		if(commandLabel.equalsIgnoreCase("moedibles")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length == 0){
					p.sendMessage(ChatColor.RED + "Usage: /moedibles help");
				}
				if(args.length == 1){
					if(args[0].equals("foods")){
						if(p.hasPermission("moedibles.foods")){
							if(p.getGameMode() == GameMode.CREATIVE){
								showCreativeFoodInventory(p, this);
							}
						}else{
							sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
						}
					}
					if(args[0].equals("help")){
						if(p.hasPermission("moedibles.help")){
							if(p.hasPermission("moedibles.help")){
								p.sendMessage("/moedibles help");
							}
							if(p.hasPermission("moedibles.foods")){
								p.sendMessage("/moedibles foods");
							}
							if(p.hasPermission("moedibles.give")){
								p.sendMessage("/moedibles give <player> <type> (<amount>)");
							}
						}else{
							sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
						}
					}
				}
			}
			if(args.length == 3){
				if(args[0].equals("give")){
					if(sender.hasPermission("moedibles.give")){
						for(Player ps : Bukkit.getOnlinePlayers()){
							if(ps.getName().equals(args[1])){
								for(int i = 0; i < foodItemsCommandNames.length; i ++){
									if(foodItemsCommandNames[i].equalsIgnoreCase(args[2])){
										if(ps.getInventory().firstEmpty() != -1){
											if(foodItems[i] == null){
												sender.sendMessage(ChatColor.RED + "This item has been disabled in the config file");
												return true;
											}
											ps.getInventory().addItem(foodItems[i]);
											sender.sendMessage("Given [" + foodItems[i].getItemMeta().getDisplayName() + "" + ChatColor.RESET + "] * 1 to " + ps.getName());
										}else{
											if(foodItems[i] == null){
												sender.sendMessage(ChatColor.RED + "This item has been disabled in the config file");
												return true;
											}
											ps.getWorld().dropItemNaturally(ps.getLocation(), foodItems[i]);
											sender.sendMessage("Given [" + foodItems[i].getItemMeta().getDisplayName() + "" + ChatColor.RESET + "] * 1 to " + ps.getName());
										}
										return true;
									}
								}
								sender.sendMessage("There is no such item with the name " + args[2]);
								return true;
							}
						}
						sender.sendMessage("Player '" + args[1] + "' cannot be found");
					}else{
						sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
					}
				}
			}else if(args.length == 4){
				if(args[0].equals("give")){
					if(sender.hasPermission("moedibles.give")){
						int amount = 1;
						try{
							amount = Integer.parseInt(args[3]);
						}catch(NumberFormatException nfe){
							return true;
						}
						for(Player ps : Bukkit.getOnlinePlayers()){
							if(ps.getName().equals(args[1])){
								for(int i = 0; i < foodItemsCommandNames.length; i ++){
									if(foodItemsCommandNames[i].equalsIgnoreCase(args[2])){
										if(ps.getInventory().firstEmpty() != -1){
											if(foodItems[i] == null){
												sender.sendMessage(ChatColor.RED + "This item has been disabled in the config file");
												return true;
											}
											ItemStack toGive = new ItemStack(foodItems[i]);
											if(amount > toGive.getMaxStackSize()){
												sender.sendMessage("The number you have entered (" + amount + ") is too big, it must be at most " + toGive.getMaxStackSize());
												return true;
											}
											toGive.setAmount(amount);
											ps.getInventory().addItem(toGive);
											sender.sendMessage("Given [" + toGive.getItemMeta().getDisplayName() + "" + ChatColor.RESET + "] * " + amount + " to " + ps.getName());
										}else{
											if(foodItems[i] == null){
												sender.sendMessage(ChatColor.RED + "This item has been disabled in the config file");
												return true;
											}
											ItemStack toGive = new ItemStack(foodItems[i]);
											if(amount > toGive.getMaxStackSize()){
												sender.sendMessage("The number you have entered (" + amount + ") is too big, it must be at most " + toGive.getMaxStackSize());
												return true;
											}
											toGive.setAmount(amount);
											ps.getWorld().dropItemNaturally(ps.getLocation(), toGive);
											sender.sendMessage("Given [" + toGive.getItemMeta().getDisplayName() + "" + ChatColor.RESET + "] * " + amount + " to " + ps.getName());
										}
										return true;
									}
								}
								sender.sendMessage("There is no such item with the name " + args[2]);
								return true;
							}
						}
						sender.sendMessage("Player '" + args[1] + "' cannot be found");
					}else{
						sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
					}
				}
			}
			
		}
		return true;
	}
	
	private void createConfig(){
		try{
			if(!getDataFolder().exists()){
				getDataFolder().mkdirs();
			}
			File file = new File(getDataFolder(), "config.yml");
				if(!file.exists()){
					saveDefaultConfig();
				}
		    }
		    catch (Exception ex) {
		    	Bukkit.getLogger().info("Error creating config file!");
		    }
		}
	
	private void verifyConfigVersion(){
		//If you are modifying the source code for this for some reason don't change the 3 unless you change the number in the config file to match
		if(getConfig().getInt("configVersion") != 3){
			getLogger().info("Invalid config file found, creating a new one and copying the old one...");
			try{
			      File file = new File(getDataFolder(), "config.yml");
			      File lastConfig = new File(getDataFolder(), "last_config.yml");
			      if (file.exists()){
			    	if(lastConfig.exists()){
			    		lastConfig.delete();
			    	}
			    	file.renameTo(lastConfig);
			        file.delete();
			        saveDefaultConfig();
			        getLogger().info("Config files updated!");
			      }
			    }
			    catch (Exception ex) {
			    	getLogger().info("Something went wrong creating the new config file!");
			    }  
			}
		}
	
	//Method where all the items are prepared and the smelting and crafting recipes are made
	public void registerRecipes(){
		String prefix = ChatColor.RESET + "Cooked ";
		FurnaceRecipe[] recipes = new FurnaceRecipe[cookedFoodTypes.length];
		foodItems = new ItemStack[cookedFoodTypes.length + 3];
		for(int index = 0; index < cookedFoodTypes.length; index++){
			foodItems[index] = new ItemStack(cookedFoodTypes[index]);
			ItemMeta im = foodItems[index].getItemMeta();
			im.setDisplayName(prefix + cookedFoodNames[index]);
			im.setLore(Arrays.asList(cookedMeta));
			foodItems[index].setItemMeta(im);
			recipes[index] =  new FurnaceRecipe(foodItems[index], cookedFoodTypes[index]);
			getServer().addRecipe(recipes[index]);
		}	
		
		ItemStack bread = new ItemStack(Material.BREAD);
		if(godBread){
			ItemMeta im = bread.getItemMeta();
			im.setDisplayName(ChatColor.RESET + "Manna");
			im.setLore(Arrays.asList(craftedMeta));
			bread.setItemMeta(im);
			foodItems[cookedFoodTypes.length] = bread;
		}
		
		ItemStack nukaCola = new ItemStack(Material.POTION);
		if(nk){
			PotionMeta pm1 = (PotionMeta) nukaCola.getItemMeta();
			pm1.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 20 , 1, true), true);
			pm1.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 100 , 0, true), true);
			pm1.setDisplayName(ChatColor.RESET + "Nuka-Cola");
			pm1.setColor(Color.MAROON);
			pm1.setLore(Arrays.asList(craftedMeta));
			nukaCola.setItemMeta(pm1);
			foodItems[cookedFoodTypes.length + 1] = nukaCola; 
		}
		
		ItemStack nukaColaQuantum = new ItemStack(Material.POTION);
		if(nkq){
			PotionMeta pm2 = (PotionMeta) nukaCola.getItemMeta();
			pm2.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 20 , 10, true), true);
			pm2.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 100 , 0, true), true);
			pm2.setDisplayName(ChatColor.RESET + "" + ChatColor.AQUA +  "Nuka-Cola Quantum");
			pm2.setColor(Color.AQUA);
			pm2.setLore(Arrays.asList(craftedMeta));
			nukaColaQuantum.setItemMeta(pm2);
			foodItems[cookedFoodTypes.length + 2] = nukaColaQuantum; 
		}
			
			
			
			
		if(godBread && godBreadR){
			ShapedRecipe br = new ShapedRecipe(bread);
			br.shape("BBB", "BBB", "BBB");
			br.setIngredient('B', Material.BREAD);
			getServer().addRecipe(br);
		}
		if(nk && nkR){
			ShapedRecipe nk = new ShapedRecipe(nukaCola);
			nk.shape("SWS", "SBS", "SSS");
			nk.setIngredient('B', Material.GLASS_BOTTLE);
			nk.setIngredient('S', Material.SUGAR);
			nk.setIngredient('W', Material.WATER_BUCKET);
			getServer().addRecipe(nk);
		}
		if(nkq && nkqR){
			ShapedRecipe nkq = new ShapedRecipe(nukaColaQuantum);
			nkq.shape("SWS", "SBS", "SDS");
			nkq.setIngredient('B', Material.GLASS_BOTTLE);
			nkq.setIngredient('S', Material.SUGAR);
			nkq.setIngredient('W', Material.WATER_BUCKET);
			nkq.setIngredient('D', Material.DIAMOND);
			getServer().addRecipe(nkq);
		}
	}
	
	//Creates invisible lore lines
	
	public static String hideMetaStrings(String data) {
        StringBuilder hidden = new StringBuilder("");
        for (char c : data.toCharArray()) {
        	hidden.append(ChatColor.COLOR_CHAR + "" + c);
        }
        return hidden.toString();
    }
	
	//Reads invisible lore lines
	
	public static String retriveMetaStrings(String data) {
        return data.replace("§", "");
    }
	
	//Checks if in item has a lore line
	
	public static boolean containsLoreLine(ItemStack item, String loreLine){
		if(item.hasItemMeta() && item.getItemMeta().hasLore()){
			for(String lore : item.getItemMeta().getLore()){
				if(lore.equals(loreLine)){
					return true;
				}
			}
		}
		return false;
	}
	
	//Shows creative food inventory (you don't say)
	
	public static void showCreativeFoodInventory(Player player, MoEdibles plugin){
		Inventory inventory = Bukkit.createInventory(null, 27, "MoEdibles Foods");
		for(int i = 0; i < plugin.foodItems.length; i ++){
			inventory.setItem(i, plugin.foodItems[i]);
		}
		player.openInventory(inventory);
	}
	
	//Makes sure only the slots with food items contain items
	
	public static void resetFoodInventory(Inventory foodInventory, MoEdibles plugin){
		for(int i = 0; i < plugin.foodItems.length; i ++){
			foodInventory.setItem(i, plugin.foodItems[i]);
		}
		for(int i = plugin.foodItems.length; i < foodInventory.getSize(); i++){
			foodInventory.setItem(i, new ItemStack(Material.AIR));
		}
	}
}
