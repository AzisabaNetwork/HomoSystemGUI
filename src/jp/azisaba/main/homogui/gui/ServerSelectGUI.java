package jp.azisaba.main.homogui.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ServerSelectGUI {

	public static Inventory getInv() {
		Inventory inv = Bukkit.createInventory(null, 9 * 3, getInvTitle());

		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "未実装...");
		item.setItemMeta(meta);

		inv.setItem(13, item);
		return inv;
	}

	public static String getInvTitle() {
		return ChatColor.RED + "Server Selector";
	}
}
