package jp.azisaba.main.homogui.gui;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIClickedListenerHub implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory() == null || !(e.getWhoClicked() instanceof Player) || e.getCurrentItem() == null
				|| e.getCurrentItem().getItemMeta() == null) {
			return;
		}

		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		ItemStack item = e.getCurrentItem();
		InventoryAction action = e.getAction();

		List<ClickableGUI> guiList = ClickableGUIController.getRegisteredGUI();

		if (guiList.size() <= 0) {
			return;
		}

		for (ClickableGUI gui : guiList) {
			if (!gui.isSameInventory(inv)) {
				continue;
			}

			if (gui.cancelEvent(p, inv, item, action)) {
				e.setCancelled(true);
			}

			gui.onClick(p, inv, item, action);
			break;
		}

		return;
	}
}
