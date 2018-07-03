package cx.lynch.AnarchyChat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import github.scarsz.discordsrv.DiscordSRV;

public class AnarchyChat extends JavaPlugin {
	public ChatListener dsl = new ChatListener(this);

	public static HashMap<String, ChatChannel> channels = new HashMap<String, ChatChannel>();
	public static HashMap<UUID, Chatter> pcmap = new HashMap<UUID, Chatter>();
	
	public ChatChannel defaultChannel;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(dsl, this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
		getServer().getPluginManager().registerEvents(new InboundMessageListener(), this);

		thisisgoingtoberemovedandisonlyfortesting();
	}

	@Override
	public void onDisable() {
		DiscordSRV.api.unsubscribe(dsl);
	}
	
	public static HashMap<String, ChatChannel> getChannels() {
		return channels;
	}
	
	public static ArrayList<Chatter> getChattersInChannel(ChatChannel c) {
		ArrayList<Chatter> res = new ArrayList<Chatter>();
		for(Entry<UUID, Chatter> e : pcmap.entrySet()) {
			res.add(e.getValue());
		}
		return res;
	}

	// When complete, this should load from a DB or a YML.
	private void thisisgoingtoberemovedandisonlyfortesting() {
		channels.put("Global", new DiscordChatChannel("Global", "g", ChatColor.DARK_GREEN, 99999, 1));
		channels.put("Staff", new ChatChannel("Staff", "s", ChatColor.DARK_RED, 99999, 0));
		channels.put("Casino", new ChatChannel("Casino", "c", ChatColor.GOLD, 99999, 0));
		ChatChannel c = new ChatChannel("Local", "l", ChatColor.DARK_AQUA, 50, 0);
		defaultChannel = c;
		channels.put("Local", c);
	}
}
