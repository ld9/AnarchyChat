package cx.lynch.AnarchyChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;

public class DiscordChatChannel extends ChatChannel {

	public DiscordChatChannel(String name, String abr, ChatColor color, int range, int cost) {
		super(name, abr, color, range, cost);
	}

	public void send(Chatter c, String m) {
		ChannelChatEvent cce = new ChannelChatEvent(c, m, this);
		Bukkit.getPluginManager().callEvent(cce);
		
		m = buildMessage(c, m);
		broadcast(m, c);
		broadcastRawDiscord(m);
	}

	public void broadcastRawDiscord(String m) {
		DiscordUtil.sendMessage(DiscordSRV.getPlugin().getMainTextChannel(), DiscordUtil.strip(m));
	}
}
