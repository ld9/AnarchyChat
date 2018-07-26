package cx.lynch.AnarchyChat;

import java.sql.SQLException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) throws SQLException {
		AnarchyChat.ph.refreshPlayer(e.getPlayer());
	}
}
