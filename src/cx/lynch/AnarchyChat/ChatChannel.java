package cx.lynch.AnarchyChat;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatChannel {
	public String name, abr;
	public int range, cost;

	private ChatColor color, gray = ChatColor.GRAY;

	public ArrayList<Chatter> members = new ArrayList<Chatter>();

	public ChatChannel(String name, String abr, ChatColor color, int range, int cost) {
		this.name = name;
		this.abr = abr;
		this.cost = cost;
		this.color = color;
		this.range = range;
	}

	public void addChatter(Chatter c) {
		this.members.add(c);
	}

	public void broadcastRaw(String m) {
		System.out.println(m);
		for (Chatter c : members) {
			c.getPlayer().sendMessage(m);
		}
	}
	
	public void broadcast(String m, Chatter b) {
		for (Chatter c : members) {
			if (c.getPlayer().getLocation().distance(b.getPlayer().getLocation()) <= range) {
				c.getPlayer().sendMessage(m);
			} else if (b.getPlayer() == null) {
				c.getPlayer().sendMessage(m);
			}
		}
	}
	
	public void sendNoSender(String m) {
		broadcastRaw(buildMessageNoPlayer(m));
	}
	
	public String buildMessageNoPlayer(String m) {
		return gray + "[" + color + abr + gray + "] " +  ChatColor.RESET + m.trim();
	}

	public String buildMessage(Chatter c, String m) {
		return gray + "[" + color + abr + gray + "] " + (c.getColor() == null ? "" : c.getColor()) + c.getName() + gray + ": " + ChatColor.RESET + m.trim();
	}

	public void send(Chatter c, String m) {
		ChannelChatEvent cce = new ChannelChatEvent(c, m, this);
		Bukkit.getPluginManager().callEvent(cce);
		if (!cce.isCancelled())
			broadcast(buildMessage(c, m), c);
	}

	public void chat(Chatter c, String m) {
		int h = c.getPlayer().getFoodLevel();
		if (h >= cost) {
			c.getPlayer().setFoodLevel(h - cost);
			send(c, m);
		} else {
			c.getPlayer().sendMessage(ChatColor.RED + "Sorry, you'll need to eat up before chatting in that channel.");
		}
	}
}
