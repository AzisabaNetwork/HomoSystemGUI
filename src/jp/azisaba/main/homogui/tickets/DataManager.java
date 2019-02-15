package jp.azisaba.main.homogui.tickets;

import java.util.UUID;

import org.bukkit.entity.Player;

import jp.azisaba.main.homos.Homos;
import jp.azisaba.main.homos.classes.PlayerData;
import jp.azisaba.main.homos.database.PlayerDataManager;
import jp.azisaba.main.homos.database.TicketManager;

public class DataManager {

	public static boolean addTicket(Player p, int amount) {
		return TicketManager.addTicket(p, amount);
	}

	public static boolean removeTicket(Player p, int amount) {
		return  TicketManager.removeTicket(p, amount);
	}

	public static long getPlayerTicketRealValue(Player p) {
		return PlayerDataManager.getPlayerData(p).getMoney();
	}

	public static int getTicketAmount(Player p) {
		return PlayerDataManager.getPlayerData(p).getTickets();
	}

	public static int getTicketValue() {
		return Homos.getMedianManager().getCurrentMedian();
	}

	public static PlayerData getPlayerData(Player p) {
		return PlayerDataManager.getPlayerData(p);
	}

	public static PlayerData getPlayerData(UUID uuid) {
		return PlayerDataManager.getPlayerData(uuid);
	}
}
