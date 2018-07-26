package cx.lynch.AnarchyChat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	private AnarchyChat plugin;

	public PlayerJoinListener(AnarchyChat plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) throws SQLException {
		if (AnarchyChat.pcmap.containsKey(e.getPlayer().getUniqueId())) {
			@SuppressWarnings("unused")
			Chatter f = AnarchyChat.pcmap.get(e.getPlayer().getUniqueId());
			AnarchyChat.pcmap.remove(e.getPlayer().getUniqueId());
			f = null;
			System.gc();
		}
		addPlayer(e);
	}

	public void addPlayer(PlayerJoinEvent e) throws SQLException {
		PreparedStatement s = plugin.connection.prepareStatement("select * from chat_players where uuid = ?");
		s.setString(1, e.getPlayer().getUniqueId().toString());
		ResultSet r = s.executeQuery();

		Chatter f = null;

		if (r.next()) {
			String nick = r.getString("nick");
			String cc = ChatColor.translateAlternateColorCodes('&', r.getString("chat_color"));
			String prefix;
			if (r.getString("server_prefix") != null) {
				prefix = ChatColor.translateAlternateColorCodes('&', r.getString("server_prefix")) + " ";
			} else {
				prefix = "";
			}
			if (r.getString("custom_prefix") != null) {
				prefix += ChatColor.translateAlternateColorCodes('&', r.getString("custom_prefix")) + " ";
			}
			f = new Chatter(plugin, e.getPlayer().getUniqueId(), nick, r.getBoolean("see_nicks"), cc, prefix);
		} else {
			createNewPlayerEntry(e);
			return;
		}

		PreparedStatement s2 = plugin.connection.prepareStatement("select * from chat_join where `player_id` = ?");
		s2.setInt(1, r.getInt("id"));
		ResultSet r2 = s2.executeQuery();

		while (r2.next()) {
			PreparedStatement s3 = plugin.connection
					.prepareStatement("select `name` from chat_channels where `id` = ?");
			s3.setInt(1, r2.getInt("channel_id"));
			ResultSet r3 = s3.executeQuery();
			while (r3.next()) {
				AnarchyChat.channels.get(r3.getString("name")).addChatter(f);
			}
			s3.close();
		}

		AnarchyChat.pcmap.put(e.getPlayer().getUniqueId(), f);
		
		s.close();
		s2.close();
	}

	public void createNewPlayerEntry(PlayerJoinEvent e) throws SQLException {
		PreparedStatement s = plugin.connection
				.prepareStatement("insert into `chat_players` values (NULL, ?, null, false, ?, ?, null, ?)", Statement.RETURN_GENERATED_KEYS);
		s.setString(1, e.getPlayer().getUniqueId().toString());
		s.setString(2, "&f");
		s.setString(3, "[&e0.00&7]");
		s.setDouble(4, 0.00);
		s.executeUpdate();
		s.closeOnCompletion();
		
		ResultSet rs = s.getGeneratedKeys();
		rs.next();
		
		PreparedStatement s2 = plugin.connection.prepareStatement("select `id` from `chat_channels` where `default_channel` = true");
		ResultSet r2 = s2.executeQuery();
		
		while (r2.next()) {
			PreparedStatement s3 = plugin.connection.prepareStatement("insert into `chat_join` values (null, ?, ?)");
			s3.setInt(1, rs.getInt(1));
			s3.setInt(2, r2.getInt(1));
			s3.executeUpdate();
			
			s3.close();
		}
		
		
		s.close();
		s2.close();
		addPlayer(e);
	}
}
