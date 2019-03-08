package jp.azisaba.main.homogui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ServerSelectConfig {

	private static boolean loaded = false;
	private static int size = 0;
	private static HashMap<Integer, ServerItem> servers = new HashMap<>();

	public static void load(HomoGUI plugin) {
		if (loaded) {
			return;
		}

		File file = new File(plugin.getDataFolder(), "servers.yml");
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
		if (!file.exists()) {
			conf.set("InventorySize", 9);
			conf.set("4.Item.Type", Material.DIAMOND_PICKAXE.name());
			conf.set("4.Item.Title", "&a&l&n" + "生活サーバー");
			conf.set("4.Item.Desc", Arrays.asList("&e説明1", "&e説明2"));
			conf.set("4.Item.Enchanted", false);
			conf.set("4.Item.Amount", 1);

			conf.set("4.Server.Name", "main");
			conf.set("4.CloseInventory", true);

			try {
				conf.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		size = conf.getInt("InventorySize", 9 * 6);

		ConfigurationSection sec = conf.getConfigurationSection("");
		if (sec == null) {
			return;
		}

		for (String key : sec.getKeys(false)) {

			if (key.equalsIgnoreCase("InventorySize")) {
				continue;
			}

			String serverName = conf.getString(key + ".Server.Name", null);
			boolean closeInv = conf.getBoolean(key + ".CloseInventory", false);
			int place;

			Material type;

			String title = conf.getString(key + ".Item.Title", null);
			List<String> desc = conf.getStringList(key + ".Item.Desc");
			boolean enchant = conf.getBoolean(key + ".Item.Enchanted", false);
			int amount = conf.getInt(key + ".Item.Amount", 1);

			try {
				place = Integer.parseInt(key);
			} catch (Exception e) {
				plugin.getLogger().warning("Could not load key '" + key + "'");
				continue;
			}

			try {
				type = Material.valueOf(conf.getString(key + ".Item.Type", "STONE").toUpperCase());
			} catch (Exception e) {
				plugin.getLogger()
						.warning("Could not load material '" + conf.getString(key + ".Item.Type", "STONE") + "'");
				continue;
			}

			title = conf.getString(key + ".Item.Title", null);
			desc = conf.getStringList(key + ".Item.Desc");
			enchant = conf.getBoolean(key + ".Item.Enchanted", false);
			amount = conf.getInt(key + ".Item.Amount", 1);

			if (title != null)
				title = ChatColor.translateAlternateColorCodes('&', title);

			List<String> newDesc = new ArrayList<String>();
			for (String s : desc) {
				newDesc.add(ChatColor.translateAlternateColorCodes('&', s));
			}
			desc = newDesc;

			ItemStack item = createItem(type, title, desc, enchant, amount);
			servers.put(place, new ServerItem(item, serverName, closeInv));
		}
	}

	public static HashMap<Integer, ServerItem> getItems() {
		return servers;
	}

	public static int getInventorySize() {
		return size;
	}

	protected static void setLoaded(boolean loaded) {
		ServerSelectConfig.loaded = loaded;
	}

	private static ItemStack createItem(Material type, String title, List<String> desc, boolean enchant, int amount) {
		ItemStack item = new ItemStack(type, amount);
		ItemMeta meta = item.getItemMeta();

		if (title != null) {
			meta.setDisplayName(title);
		}
		if (desc.size() > 0) {
			meta.setLore(desc);
		}
		if (enchant) {
			meta.addEnchant(Enchantment.DURABILITY, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}

		item.setItemMeta(meta);
		return item;
	}
}
