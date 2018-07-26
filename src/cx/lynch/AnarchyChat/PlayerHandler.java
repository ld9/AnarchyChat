package cx.lynch.AnarchyChat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerHandler {	
	public void updatePlayerScore(Player p, double val) throws SQLException {
		String uuid = p.getUniqueId().toString();
		PreparedStatement s = AnarchyChat.connection.prepareStatement("select `real_level` from `chat_players` where uuid = ?");
		s.setString(1, uuid);
		ResultSet r = s.executeQuery();
		
		double score = -1;
		while (r.next()) {
			score = r.getDouble(1);
		}
		
		if (score < 0)
			return;
		
		score += val;

		s.close();
		PreparedStatement s2 =AnarchyChat.connection.prepareStatement("update `chat_players` set `real_level` = ?, `server_prefix` = ? where `uuid` = ?");
		s2.setDouble(1, score);
		s2.setString(2, "&7[&e" + Double.toString(score) + "&7]");
		s2.setString(3, uuid);
		s2.executeUpdate();
		
		s2.closeOnCompletion();

		refreshPlayer(p);
	}
	
	public void refreshPlayer(Player p) throws SQLException {
		if (AnarchyChat.pcmap.containsKey(p.getUniqueId())) {
			Chatter f = AnarchyChat.pcmap.get(p.getUniqueId());
			AnarchyChat.pcmap.remove(p.getUniqueId());
			
			for (Entry<String, ChatChannel> c : AnarchyChat.channels.entrySet()) {
				if (c.getValue().members.contains(f)) {
					c.getValue().members.remove(f);
				}
			}
			
			f = null;
			System.gc();
			
			System.out.println("CONTAINS PLAYER (Recurse)");
			refreshPlayer(p);
		} else {
			addPlayer(p);
		}
	}

	public void addPlayer(Player p) throws SQLException {
		PreparedStatement s = AnarchyChat.connection.prepareStatement("select * from chat_players where uuid = ?");
		s.setString(1, p.getUniqueId().toString());
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
			f = new Chatter(p, p.getUniqueId(), nick, r.getBoolean("see_nicks"), cc, prefix);
		} else {
			createNewPlayerEntry(p);
			return;
		}

		PreparedStatement s2 = AnarchyChat.connection.prepareStatement("select * from chat_join where `player_id` = ?");
		s2.setInt(1, r.getInt("id"));
		ResultSet r2 = s2.executeQuery();

		while (r2.next()) {
			PreparedStatement s3 = AnarchyChat.connection
					.prepareStatement("select `name` from chat_channels where `id` = ?");
			s3.setInt(1, r2.getInt("channel_id"));
			ResultSet r3 = s3.executeQuery();
			while (r3.next()) {
				AnarchyChat.channels.get(r3.getString("name")).addChatter(f);
			}
			s3.close();
		}

		AnarchyChat.pcmap.put(p.getUniqueId(), f);
		
		s.close();
		s2.close();
	}

	public void createNewPlayerEntry(Player p) throws SQLException {
		PreparedStatement s = AnarchyChat.connection
				.prepareStatement("insert into `chat_players` values (NULL, ?, null, false, ?, ?, null, ?)", Statement.RETURN_GENERATED_KEYS);
		s.setString(1, p.getUniqueId().toString());
		s.setString(2, "&f");
		s.setString(3, "[&e0.00&7]");
		s.setDouble(4, 0.00);
		s.executeUpdate();
		s.closeOnCompletion();
		
		ResultSet rs = s.getGeneratedKeys();
		rs.next();
		
		PreparedStatement s2 = AnarchyChat.connection.prepareStatement("select `id` from `chat_channels` where `default_channel` = true");
		ResultSet r2 = s2.executeQuery();
		
		while (r2.next()) {
			PreparedStatement s3 = AnarchyChat.connection.prepareStatement("insert into `chat_join` values (null, ?, ?)");
			s3.setInt(1, rs.getInt(1));
			s3.setInt(2, r2.getInt(1));
			s3.executeUpdate();
			
			s3.close();
		}
		
		
		s.close();
		s2.close();
		addPlayer(p);
	}
}
