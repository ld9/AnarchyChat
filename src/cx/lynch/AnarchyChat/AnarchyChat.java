package cx.lynch.AnarchyChat;

import org.bukkit.plugin.java.JavaPlugin;

import github.scarsz.discordsrv.DiscordSRV;

public class AnarchyChat extends JavaPlugin{
	public ChatListener dsl = new ChatListener(this);
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(dsl, this);
		//DiscordSRV.api.subscribe(dsl);
	}
	
	@Override
	public void onDisable() {
		DiscordSRV.api.unsubscribe(dsl);
	}
}
