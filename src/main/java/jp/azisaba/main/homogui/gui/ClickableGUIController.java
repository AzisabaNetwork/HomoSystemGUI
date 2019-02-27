package jp.azisaba.main.homogui.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ClickableGUIController {

	private static List<ClickableGUI> guiList = new ArrayList<>();

	protected static void addGUI(ClickableGUI gui) {
		if (!guiList.contains(gui)) {
			gui.onRegister();
			guiList.add(gui);
		}
	}

	protected static void removeGUI(ClickableGUI gui) {
		if (guiList.contains(gui)) {
			gui.onUnRegister();
			guiList.remove(gui);
		}
	}

	protected static List<ClickableGUI> getRegisteredGUI() {
		return guiList;
	}

	public static void registerAll() {
		List<ClickableGUI> createdGUIList = new ArrayList<>();

		createdGUIList.add(new MainGUI());
		createdGUIList.add(new ServerSelectGUI());
		createdGUIList.add(new TicketGUI());
		createdGUIList.addAll(Arrays.asList(new NumberGUIForBuy(), new NumberGUIForSell()));
		createdGUIList.addAll(Arrays.asList(new TicketConfirmGUIForBuy(), new TicketConfirmGUIForSell()));

		for (ClickableGUI gui : createdGUIList) {
			if (!guiList.contains(gui)) {
				gui.onRegister();
				guiList.add(gui);
			}
		}
	}

	protected static ClickableGUI getGUI(Class<? extends ClickableGUI> clazz) {

		for (ClickableGUI gui : guiList) {
			if (gui.getClass().equals(clazz)) {
				return gui;
			}
		}

		return null;
	}

	public static Inventory getMainInv(Player p) {
		return getGUI(MainGUI.class).getInventory(p);
	}
}
