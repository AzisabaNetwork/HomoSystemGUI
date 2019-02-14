package jp.azisaba.main.homogui.gui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TicketGUI {

	public static Inventory getInv() {
		Inventory inv = Bukkit.createInventory(null, 9 * 3, getInvTitle());

		ItemStack sell = getSellItem();
		ItemStack buy = getBuyItem();

		inv.setItem(12, buy);
		inv.setItem(14, sell);
		return inv;
	}

	public static String getInvTitle() {
		return ChatColor.RED + "Ticket";
	}

	public static ItemStack getSellItem() {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatColor.GREEN + "売却");
		meta.setLore(Arrays.asList(clickToContinue()));

		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getBuyItem() {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "購入");
		meta.setLore(Arrays.asList(clickToContinue()));
		item.setItemMeta(meta);
		return item;
	}

	private static String clickToContinue() {
		return ChatColor.YELLOW + "クリックで継続...";
	}
}
