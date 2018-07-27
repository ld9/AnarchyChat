package cx.lynch.AnarchyChat;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatChannel {
	public String name, abr, color;
	public int range, cost;
	public boolean default_channel;

	private ChatColor gray = ChatColor.GRAY;

	public ArrayList<Chatter> members = new ArrayList<Chatter>();

	public ChatChannel(String name, String abr, String color, int range, int cost, boolean default_channel) {
		this.name = name;
		this.abr = abr;
		this.default_channel = default_channel;
		this.cost = cost;
		this.color = color;
		this.range = range;
	}
	
	public boolean isDefault() {
		return default_channel;
	}

	public void addChatter(Chatter c) {
		System.out.println("New chatter " + c.getName() + " added to " + name);
		this.members.add(c);
	}

	public void broadcastRaw(String m) {
		System.out.println(m);
		for (Chatter c : members) {
			c.getPlayer().sendMessage(m);
		}
	}
	
	public void broadcast(String mraw, String mNoNick, Chatter b) {
		System.out.println(mNoNick);
		String m;
		
		for (Chatter c : members) {
			if(c.seeNicks()) {
				m = mraw;
			} else {
				m = mNoNick;
			}
			if (c.getPlayer().getLocation().distance(b.getPlayer().getLocation()) <= range) {
				c.getPlayer().sendMessage(m);
			} else if (b.getPlayer() == null) {
				c.getPlayer().sendMessage(m);
			} else if (c.getPlayer().hasPermission("anarchy.omnipotent")) {
				c.getPlayer().sendMessage(ChatColor.RED + "[" + ChatColor.GOLD + "*" + ChatColor.RED + "] " + ChatColor.RESET + m);
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
		return gray + "[" + color + abr + gray + "] " + (c.getPrefix() == null ? "" : c.getPrefix()) + (c.getColor() == null ? "" : c.getColor()) + "^" + c.getNickname() + gray + ": " + ChatColor.RESET + m.trim();
	}
	
	public String buildMessageNoNick(Chatter c, String m) {
		return gray + "[" + color + abr + gray + "] " + (c.getPrefix() == null ? "" : c.getPrefix()) + (c.getColor() == null ? "" : c.getColor()) + c.getName() + gray + ": " + ChatColor.RESET + m.trim();
	}

	public void send(Chatter c, String m) {
		ChannelChatEvent cce = new ChannelChatEvent(c, m, this);
		Bukkit.getPluginManager().callEvent(cce);
		if (!cce.isCancelled())
			broadcast(buildMessage(c, m), buildMessageNoNick(c,m), c);
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
