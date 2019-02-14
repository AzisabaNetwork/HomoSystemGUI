package jp.azisaba.main.homogui;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import jp.azisaba.main.homogui.commands.GUICommand;
import jp.azisaba.main.homogui.listeners.DebugListener;
import jp.azisaba.main.homogui.listeners.MainGUIListener;
import jp.azisaba.main.homogui.listeners.NumberGUIListener;
import jp.azisaba.main.homogui.listeners.ServerSelectorGUIListener;
import jp.azisaba.main.homogui.listeners.TicketConfirmGUIListener;
import jp.azisaba.main.homogui.listeners.TicketGUIListener;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class HomoGUI extends JavaPlugin {

	private static PluginConfig config;

	@Override
	public void onEnable() {

		HomoGUI.config = new PluginConfig(this);
		HomoGUI.config.loadConfig();

		setupEconomy();

		Bukkit.getPluginManager().registerEvents(new DebugListener(), this);
		Bukkit.getPluginManager().registerEvents(new MainGUIListener(this), this);
		Bukkit.getPluginManager().registerEvents(new TicketGUIListener(), this);
		Bukkit.getPluginManager().registerEvents(new ServerSelectorGUIListener(), this);
		Bukkit.getPluginManager().registerEvents(new NumberGUIListener(), this);
		Bukkit.getPluginManager().registerEvents(new TicketConfirmGUIListener(), this);

		Bukkit.getPluginCommand("gui").setExecutor(new GUICommand());
		Bukkit.getPluginCommand("gui").setPermissionMessage(ChatColor.RED + "権限がないようです。運営に報告してください。");

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
}
