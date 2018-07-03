package cx.lynch.AnarchyChat;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChannelChatEvent extends Event implements Cancellable{
	private boolean isCancelled;
	private static final HandlerList handlers = new HandlerList();
	
	private Chatter sender;
	private String message;
	private ChatChannel channel;
	
	public ChannelChatEvent(Chatter sender, String message, ChatChannel channel) {
		this.sender = sender;
		this.message = message;
		this.channel = channel;
		
		this.isCancelled = false;
	}
	
	public void cancelMessage() {
		
	}
	
	public ChatChannel getChannel() {
		return channel;
	}

	public Chatter getChatter() {
		return sender;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.isCancelled = arg0;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}

}
