package jp.azisaba.main.homogui.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import jp.azisaba.main.homogui.gui.NumberGUI;
import jp.azisaba.main.homogui.gui.TicketConfirmGUI.ConfirmType;
import jp.azisaba.main.homogui.gui.TicketGUI;

public class TicketGUIListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory() == null || !(e.getWhoClicked() instanceof Player) || e.getCurrentItem() == null
				|| e.getCurrentItem().getItemMeta() == null) {
			return;
		}

		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();

		@SuppressWarnings("deprecation")
		String invTitle = inv.getTitle();
		if (!invTitle.equals(TicketGUI.getInvTitle())) {
			return;
		}

		e.setCancelled(true);

		if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) {
			return;
		}

		ItemStack item = e.getCurrentItem();
		ItemMeta meta = item.getItemMeta();

		Inventory numberInv;

		if (meta.getDisplayName().equals(TicketGUI.getBuyItem().getItemMeta().getDisplayName())) {
			numberInv = NumberGUI.getInv(p, ConfirmType.BUY);
		} else if (meta.getDisplayName().equals(TicketGUI.getSellItem().getItemMeta().getDisplayName())) {
			numberInv = NumberGUI.getInv(p, ConfirmType.SELL);
		} else {
			return;
		}

		p.openInventory(numberInv);
	}
}
