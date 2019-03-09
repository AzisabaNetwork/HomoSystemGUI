package jp.azisaba.main.homogui.gui;

import java.util.ArrayList;
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

		if (type == ServerType.MAIN) {
			packageName = parent + ".main";
			main = new jp.azisaba.main.homogui.gui.main.MainGUI();
		} else if (type == ServerType.PARKOUR) {
			packageName = parent + ".parkour";
			// TODO main =
		} else if (type == ServerType.PATA) {
			 packageName = parent + ".pata";
			// TODO main =
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
