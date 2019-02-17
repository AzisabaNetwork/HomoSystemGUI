package jp.azisaba.main.homogui.tickets;

import java.math.BigInteger;
import java.util.UUID;

import org.bukkit.entity.Player;

import jp.azisaba.main.homos.Homos;
import jp.azisaba.main.homos.classes.PlayerData;
import jp.azisaba.main.homos.database.PlayerDataManager;
import jp.azisaba.main.homos.database.TicketManager;

public class DataManager {

	public static boolean addTicket(Player p, BigInteger amount) {
		return TicketManager.addTicket(p, amount);
	}

	public static boolean removeTicket(Player p, BigInteger amount) {
		return TicketManager.removeTicket(p, amount);
	}

	public static BigInteger getPlayerTicketRealValue(Player p) {
		return PlayerDataManager.getPlayerData(p).getMoney();
	}

	public static BigInteger getTicketAmount(Player p) {
		return PlayerDataManager.getPlayerData(p).getTickets();
	}

	public static BigInteger getTicketValue() {
		return Homos.getMedianManager().getCurrentMedian();
	}

	public static PlayerData getPlayerData(Player p) {
		return PlayerDataManager.getPlayerData(p);
	}

	public static PlayerData getPlayerData(UUID uuid) {
		return PlayerDataManager.getPlayerData(uuid);
	}
}
