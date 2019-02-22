package jp.azisaba.main.homogui.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class ClickableGUI {

	abstract public void onClick(Player p, Inventory inv, ItemStack item, InventoryAction action);

	abstract public boolean cancelEvent(Player p, Inventory inv, ItemStack item, InventoryAction action);

	abstract public Inventory getInventory(Player p, Object... objects);

	abstract public boolean isSameInventory(Inventory inv);

	public void onRegister() {

	}

	public void onUnRegister() {

	}
}
