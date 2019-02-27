package jp.azisaba.main.homogui.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import jp.azisaba.main.homogui.utils.ItemHelper;

public class ServerSelectGUI extends ClickableGUI {

	private Inventory inv = null;

	@Override
	public void onClick(Player p, Inventory inv, ItemStack item, InventoryAction action) {
		return;
	}

	@Override
	public boolean cancelEvent(Player p, Inventory inv, ItemStack item, InventoryAction action) {
		return true;
	}

	@Override
	public Inventory getInventory(Player p, Object... objects) {

		if (inv == null) {
			inv = Bukkit.createInventory(null, getInvSize(), getInvTitle());
			ItemStack item = ItemHelper.createItem(Material.BARRIER, ChatColor.RED + "未実装...");
			inv.setItem(13, item);
		}

		return this.inv;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isSameInventory(Inventory inv) {

		boolean sameName = inv.getTitle().equals(getInvTitle());
		boolean sameSize = inv.getSize() == getInvSize();

		return sameName && sameSize;
	}

	private static String getInvTitle() {
		return ChatColor.RED + "Server Selector";
	}

	private int getInvSize() {
		return 9 * 3;
	}
}
