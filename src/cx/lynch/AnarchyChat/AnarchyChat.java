package cx.lynch.AnarchyChat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import github.scarsz.discordsrv.DiscordSRV;

public class AnarchyChat extends JavaPlugin {
	public ChatListener dsl = new ChatListener(this);

	public static HashMap<String, ChatChannel> channels = new HashMap<String, ChatChannel>();
	public static HashMap<UUID, Chatter> pcmap = new HashMap<UUID, Chatter>();
	
	public ArrayList<ChatChannel> defaultChannels = new ArrayList<ChatChannel>();
	public static ChatChannel defaultChannel;
	
	public static Connection connection;
	public Statement statement;
	public FileConfiguration config;
	public String sqlserver, sqlport, sqluser, sqldb, sqlpass, serverName;
	
	public static PlayerHandler ph = new PlayerHandler();

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		config = this.getConfig();
		
		this.sqlserver = config.getString("sqlhost");
		this.sqlport = config.getString("sqlport");
		this.sqluser = config.getString("sqluser");
		this.sqlpass = config.getString("sqlpass");
		this.sqldb = config.getString("sqldb");
		this.serverName = config.getString("servername");
		
		try {
			openConnection();
			this.statement = connection.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		getServer().getPluginManager().registerEvents(dsl, this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new InboundMessageListener(), this);

		try {
			registerDatabaseChannels();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		DiscordSRV.api.unsubscribe(dsl);
	}
	
	public static HashMap<String, ChatChannel> getChannels() {
		return channels;
	}
	
	public static void updatePlayerScore(Player p, double amount) throws SQLException {
		ph.updatePlayerScore(p, amount);
	}
	
	public static ArrayList<Chatter> getChattersInChannel(ChatChannel c) {
		ArrayList<Chatter> res = new ArrayList<Chatter>();
		for(Entry<UUID, Chatter> e : pcmap.entrySet()) {
			res.add(e.getValue());
		}
		return res;
	}

	
	protected void registerDatabaseChannels() throws SQLException {
		// query for channels.
		PreparedStatement s = AnarchyChat.connection.prepareStatement("select * from chat_channels");
		ResultSet rs = s.executeQuery();
		
		while(rs.next()) {
			ChatChannel newChannel = new ChatChannel(
					rs.getString("name"),
					rs.getString("short_name"),
					ChatColor.translateAlternateColorCodes('&', rs.getString("color")),
					rs.getInt("chat_range"),
					rs.getInt("cost"),
					rs.getBoolean("default_channel"));
			channels.put(rs.getString("name"), newChannel);
			System.out.println("New channel made (Total):" + channels.size());
			
			if(rs.getBoolean("default_channel")) {
				defaultChannels.add(newChannel);
			}
		}
		AnarchyChat.defaultChannel = channels.get("Local");
		
		s.close();
	}
	
	public void openConnection() throws SQLException, ClassNotFoundException {
		if (connection != null && !connection.isClosed()) {
			return;
		}

		synchronized (this) {
			if (connection != null && !connection.isClosed()) {
				return;
			}
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + this.sqlserver + ":" + this.sqlport + "/" + this.sqldb,
					this.sqluser, this.sqlpass);
		}
	}
}
