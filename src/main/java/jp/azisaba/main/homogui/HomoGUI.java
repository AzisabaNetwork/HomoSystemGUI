package jp.azisaba.main.homogui;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import jp.azisaba.main.homogui.commands.GUICommand;
import jp.azisaba.main.homogui.gui.ClickableGUIController;
import jp.azisaba.main.homogui.gui.GUIClickedListenerHub;
import jp.azisaba.main.homogui.gui.ServerType;
import jp.azisaba.main.homogui.listeners.BalanceCommandListener;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class HomoGUI extends JavaPlugin {

	private static PluginConfig config;

	public static final String voteUrl = "https://minecraft.jp/servers/azisaba.net/vote";

	@Override
	public void onEnable() {

		instance = this;

		HomoGUI.config = new PluginConfig(this);
		HomoGUI.config.loadConfig();

		setupEconomy();

		ServerType type = null;
		try {
			type = ServerType.valueOf(config.serverType.toUpperCase());
		} catch (Exception e) {
			getLogger().warning("GUI for server called '" + config.serverType + "' is not supported.");
		}
		if (type == ServerType.UNDEFINED) {
			getLogger().info("You have to set server value in config.yml");
		} else if (type != null) {
			ClickableGUIController.registerAll(type);
		}

		Bukkit.getPluginManager().registerEvents(new GUIClickedListenerHub(), this);
		Bukkit.getPluginManager().registerEvents(new BalanceCommandListener(this), this);

		Bukkit.getPluginCommand("gui").setExecutor(new GUICommand());
		Bukkit.getPluginCommand("gui").setPermissionMessage(ChatColor.RED + "権限がないようです。運営に報告してください。");

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		ServerSelectConfig.load(this);

		Bukkit.getLogger().info(getName() + " enabled.");
	}

	@Override
	public void onDisable() {
		Bukkit.getLogger().info(getName() + " disabled.");
	}

	public void reloadPluginConfig() {

		this.reloadConfig();

		HomoGUI.config = new PluginConfig(this);
		HomoGUI.config.loadConfig();
	}

	public static PluginConfig getPluginConfig() {
		return config;
	}

	private static Economy econ;

	public static boolean setupEconomy() {
		try {
			if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
				return false;
			}
			RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
			if (rsp == null) {
				return false;
			}
			econ = rsp.getProvider();
			return econ != null;
		} catch (Exception e) {
			return false;
		}
	}

	public static Economy getEconomy() {
		return econ;
	}

	private static HomoGUI instance;

	public static HomoGUI getInstance() {
		return instance;
	}
}
