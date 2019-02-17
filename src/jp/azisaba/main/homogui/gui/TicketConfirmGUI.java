package jp.azisaba.main.homogui.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TicketConfirmGUI {

	public static Inventory getInv(ConfirmType type, long tickets) {
		Inventory inv = Bukkit.createInventory(null, 9 * 1, getInvTitle(type));

		ItemStack ok = getConfirmItem();
		ItemStack no = getCancelItem();

		ItemStack sign = new ItemStack(Material.SIGN);
		ItemMeta signMeta = sign.getItemMeta();
		signMeta.setDisplayName(ChatColor.YELLOW + "" + tickets + "チケットを"
				+ type.toString().replace("BUY", ChatColor.RED + "購入").replace("SELL", ChatColor.GREEN + "売却"));
		sign.setItemMeta(signMeta);

		inv.setItem(0, no);
		inv.setItem(1, no);
		inv.setItem(2, no);
		inv.setItem(3, no);

		inv.setItem(4, sign);

		inv.setItem(5, ok);
		inv.setItem(6, ok);
		inv.setItem(7, ok);
		inv.setItem(8, ok);
		return inv;
	}

	public static String getInvTitle(ConfirmType type) {
		if (type == ConfirmType.BUY) {
			return ChatColor.RED + "Confirm - " + ChatColor.RED + "Buy";
		} else {
			return ChatColor.RED + "Confirm - " + ChatColor.GREEN + "Sell";
		}
	}

	private static ItemStack getConfirmItem() {
		ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "確定");
		item.setItemMeta(meta);
		return item;
	}

	private static ItemStack getCancelItem() {
		ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "キャンセル");
		item.setItemMeta(meta);
		return item;
	}

	public enum ConfirmType {
		BUY, SELL;
	}
}
