package me.Allogeneous.MoEdibles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class MoEdiblesTabCompleter implements TabCompleter{
	
	private MoEdibles plugin;
	private String[] arg0 = new String[]{"foods", "help", "give"};
	private List<String> tabs = new ArrayList<>();
	
	public MoEdiblesTabCompleter(MoEdibles plugin){
		this.plugin = plugin;
	}
	

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String commandLable, String[] args) {
		
		tabs.clear();
		
		if(command.getName().equalsIgnoreCase("moedibles")){
			if(args.length == 0){
				return Arrays.asList(arg0);
			}
		}
			
		if(args.length == 1){
			for(String arg : arg0){
				if(arg.startsWith(args[0])){
					tabs.add(arg);
				}
			}
			
			if(tabs.isEmpty()){
				return Arrays.asList(arg0);
			}
			return tabs;
		}
			
		if(args.length == 2){
			if(args[0].equals(arg0[2])){
				for(Player ps : Bukkit.getOnlinePlayers()){
					if(ps.getName().startsWith(args[1])){
						tabs.add(ps.getName());
					}
				}
				if(tabs.isEmpty()){
					for(Player ps : Bukkit.getOnlinePlayers()){
						tabs.add(ps.getName());
					}
					return Arrays.asList(plugin.foodItemsCommandNames);
				}
				return tabs;
			}
		}
		
		if(args.length == 3){
			if(args[0].equals(arg0[2])){
				for(String arg : plugin.foodItemsCommandNames){
					if(arg.startsWith(args[2])){
						tabs.add(arg);
					}
				}
				if(tabs.isEmpty()){
					return Arrays.asList(plugin.foodItemsCommandNames);
				}
				return tabs;
			}
		}
		return tabs;
	}
}
