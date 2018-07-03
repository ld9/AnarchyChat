package cx.lynch.AnarchyChat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import cx.lynch.Believaboat.GambleEvent;

public class InboundMessageListener implements Listener{
	@EventHandler
	public void gambleEvent(GambleEvent e) {
		String d = e.getDestinationChannel();
		if (AnarchyChat.channels.containsKey(d)) {
			AnarchyChat.channels.get(d).sendNoSender(e.getMessage());
		}
	}
}
