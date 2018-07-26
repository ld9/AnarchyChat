package cx.lynch.AnarchyChat;

import java.util.UUID;

import org.bukkit.entity.Player;

public class Chatter {
	public String name, nickname, color, prefix;
	public UUID uuid;
	public Player player;
	public boolean seeNicks;

	public Chatter(AnarchyChat plugin, UUID uuid, String nick, boolean seeNicks, String color, String prefix) {
		this.uuid = uuid;
		this.prefix = prefix;
		this.nickname = nick;
		this.color = color;
		this.seeNicks = seeNicks;
		this.player = plugin.getServer().getPlayer(uuid);
		this.name = this.player.getName();
		this.prefix = prefix;
	}

	public Player getPlayer() {
		return player;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}
}
