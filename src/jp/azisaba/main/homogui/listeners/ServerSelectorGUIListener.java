package jp.azisaba.main.homogui.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import jp.azisaba.main.homogui.gui.ServerSelectGUI;

public class ServerSelectorGUIListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory() == null || !(e.getWhoClicked() instanceof Player)) {
			return;
		}

		@SuppressWarnings("unused")
		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();

		@SuppressWarnings("deprecation")
		String invTitle = inv.getTitle();
		if (!invTitle.equals(ServerSelectGUI.getInvTitle())) {
			return;
		}

		e.setCancelled(true);

		if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) {
			return;
		}

		ItemStack item = e.getCurrentItem();
		@SuppressWarnings("unused")
		ItemMeta meta = item.getItemMeta();
	}
}
