package cx.lynch.AnarchyChat;

import org.bukkit.Bukkit;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;

public class DiscordChatChannel extends ChatChannel {

	public DiscordChatChannel(String name, String abr, String color, int range, int cost, boolean def) {
		super(name, abr, color, range, cost, def);
	}

	public void send(Chatter c, String mn, String m) {
		ChannelChatEvent cce = new ChannelChatEvent(c, m, this);
		Bukkit.getPluginManager().callEvent(cce);
		
		m = buildMessage(c, m);
		broadcast(m, mn, c);
		broadcastRawDiscord(m);
	}

	public void broadcastRawDiscord(String m) {
		DiscordUtil.sendMessage(DiscordSRV.getPlugin().getMainTextChannel(), DiscordUtil.strip(m));
	}
}
