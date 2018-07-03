package cx.lynch.AnarchyChat;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Chatter {
	public String name, nickname;
	public UUID uuid;
	public Player player;
	public boolean seeNicks;
	public ChatColor color;

	public Chatter(AnarchyChat plugin, UUID uuid, String nick, boolean seeNicks, ChatColor color) {
		this.uuid = uuid;
		this.nickname = nick;
		this.color = color;
		this.seeNicks = seeNicks;
		this.player = plugin.getServer().getPlayer(uuid);
		this.name = this.player.getName();
	}

	public Player getPlayer() {
		return player;
	}

	public String getName() {
		return name;
	}

	public ChatColor getColor() {
		return color;
	}
}
