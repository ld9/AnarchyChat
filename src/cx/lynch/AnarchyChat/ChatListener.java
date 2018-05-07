package cx.lynch.AnarchyChat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import github.scarsz.discordsrv.util.DiscordUtil;
import github.scarsz.discordsrv.DiscordSRV;

public class ChatListener implements Listener{
	public AnarchyChat plugin;
	private String eventPlayer;
	
	public ChatListener(AnarchyChat plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		
		this.eventPlayer = e.getPlayer().getDisplayName();
		
		// Completely necessary for plugin functionality.
		if(this.eventPlayer.equalsIgnoreCase("Dan_Lynch")) {
			this.eventPlayer = ChatColor.GOLD + this.eventPlayer + ChatColor.RESET;
		}
		
		String chatMessageUf = e.getMessage();
		if (chatMessageUf.startsWith("!")) {
			// Shout
			shoutMessage(e);
		} else {
			// Local
			localMessage(e);
		}
	}
	
	private void shoutMessage(AsyncPlayerChatEvent e) {
		boolean discsent = false;
		e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() - 1);
		
		for (Player p : e.getRecipients()) {
			String shoutMessage = "[" + ChatColor.DARK_GREEN + "G" + ChatColor.RESET + "]<" + this.eventPlayer + "> " + e.getMessage().substring(1);
			p.sendMessage(shoutMessage);
			if (!discsent) {
				DiscordUtil.sendMessage(DiscordSRV.getPlugin().getMainTextChannel(), DiscordUtil.strip(shoutMessage));
				discsent = true;
			}
		}
	}
	
	private void localMessage(AsyncPlayerChatEvent e) {
		for (Player p : e.getRecipients()) {
			boolean sameWorld = e.getPlayer().getLocation().getWorld().getName().equals(p.getLocation().getWorld().getName());
			if (sameWorld) {
				int distance = (int) e.getPlayer().getLocation().distance(p.getLocation());
				
				if (distance < 150) {
					p.sendMessage("[" + ChatColor.DARK_AQUA+ "L" + ChatColor.RESET + "]<"  + this.eventPlayer + "> " + e.getMessage());
				} else if (p.hasPermission("anarchychat.seeall")) {
					p.sendMessage("[" + ChatColor.DARK_AQUA+ "L#" + distance + ChatColor.RESET + "]<" + this.eventPlayer + "> " + e.getMessage());
				}
			} else if (p.hasPermission("anarchychat.seeall")) {
				p.sendMessage("[" + ChatColor.DARK_AQUA+ "L#" + e.getPlayer().getLocation().getWorld().getName()  + ChatColor.RESET + "]<" + this.eventPlayer + "> " + e.getMessage());
			}
		}
	}
	
}
