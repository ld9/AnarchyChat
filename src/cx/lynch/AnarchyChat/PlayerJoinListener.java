package cx.lynch.AnarchyChat;

import java.util.Map.Entry;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	private AnarchyChat plugin;

	public PlayerJoinListener(AnarchyChat plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (!AnarchyChat.pcmap.containsKey(e.getPlayer().getUniqueId())) {
			
			// TESTING! TESTING! TESTING!
			// When complete, this should load from a DB or a YML.
			Chatter f = new Chatter(plugin, e.getPlayer().getUniqueId(), null, false, null);
			AnarchyChat.pcmap.put(e.getPlayer().getUniqueId(), f);

			for (Entry<String, ChatChannel> c : AnarchyChat.channels.entrySet()) {
				c.getValue().addChatter(f);
			}
			// </testing>
		} else {
			Chatter f = AnarchyChat.pcmap.get(e.getPlayer().getUniqueId());
			f.uuid = e.getPlayer().getUniqueId();
			f.player = plugin.getServer().getPlayer(f.uuid);
			f.name = f.player.getName();
		}
	}
}
