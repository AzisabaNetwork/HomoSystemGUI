package jp.azisaba.main.homogui.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.AuthorNagException;

import jp.azisaba.main.homogui.HomoGUI;
import jp.azisaba.main.homogui.utils.GetClassList;

public class ClickableGUIController {

	private static List<ClickableGUI> guiList = new ArrayList<>();
	private static ClickableGUI main = null;

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

	private static final String parent = "jp.azisaba.main.homogui.gui";

	public static void registerAll(ServerType type) {
		List<ClickableGUI> createdGUIList = new ArrayList<>();
		String packageName = null;

		if (Arrays.asList(ServerType.ECO, ServerType.MAIN, ServerType.PATA).contains(type)) {
			packageName = parent + ".eco";
			main = new jp.azisaba.main.homogui.gui.eco.MainGUI();
			createdGUIList.add(new ServerSelectGUI());
		} else if (Arrays.asList(ServerType.OTHERS, ServerType.EVENT, ServerType.LOBBY, ServerType.MINIGAME,
				ServerType.PARKOUR, ServerType.PVP, ServerType.RPG).contains(type)) {
			packageName = parent + ".others";
			main = new jp.azisaba.main.homogui.gui.others.MainGUI();
			createdGUIList.add(new ServerSelectGUI());
		} else if (type != null) {
			throw new AuthorNagException("'" + type.toString() + "'サーバー用のGUIはまだ整備されていません。");
		} else {
			throw new IllegalArgumentException("type mustn't be null.");
		}

		if (packageName != null) {
			try {
				Set<Class<? extends ClickableGUI>> classes = GetClassList.listClasses(packageName);

				for (Class<?> clazz : classes) {
					ClickableGUI gui = (ClickableGUI) clazz.newInstance();
					createdGUIList.add(gui);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			int count = 0;
			for (ClickableGUI gui : createdGUIList) {
				if (!guiList.contains(gui)) {
					gui.onRegister();
					guiList.add(gui);
					count++;
				}
			}

			HomoGUI.getInstance().getLogger().info(count + "個のGUIをロードしました。");
		}
	}

	public static ClickableGUI getGUI(Class<? extends ClickableGUI> clazz) {

		for (ClickableGUI gui : guiList) {
			if (gui.getClass().equals(clazz)) {
				return gui;
			}
		}

		return null;
	}

	public static Inventory getMainInv(Player p) {
		return main.getInventory(p);
	}
}
