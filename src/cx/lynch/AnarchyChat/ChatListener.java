package cx.lynch.AnarchyChat;

import java.util.Map.Entry;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.md_5.bungee.api.ChatColor;

public class ChatListener implements Listener {
	public AnarchyChat plugin;

	public ChatListener(AnarchyChat plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (e.isCancelled()) {
			return;
		}

		e.setCancelled(true);

		String m = e.getMessage();

		Chatter chatter;
		chatter = AnarchyChat.pcmap.get(e.getPlayer().getUniqueId());
		if (chatter == null) {
			e.getPlayer().sendMessage(ChatColor.RED
					+ "> Unable to send message. Attempting to update your UUID's player instance. Re-log if this message persists.");
			// Maybe do something here.
			return;
		}

		if (m.startsWith(".")) {
			for (Entry<String, ChatChannel> entry : AnarchyChat.channels.entrySet()) {
				ChatChannel c = entry.getValue();
				if (m.startsWith(c.name.toLowerCase(), 1) && c.members.contains(chatter)) {
					c.chat(chatter, m.substring(c.name.length() + 1));
				} else if (m.startsWith(c.abr.toLowerCase(), 1) && c.members.contains(chatter)) {
					c.chat(chatter, m.substring(c.abr.length() + 1));
				}
			}
		} else {
			plugin.defaultChannel.chat(chatter, m);
		}
	}
}
