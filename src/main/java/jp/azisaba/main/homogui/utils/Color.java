package jp.azisaba.main.homogui.utils;

import org.bukkit.Material;

public enum Color {

	WHITE(Material.WHITE_WOOL),
	ORANGE(Material.ORANGE_WOOL),
	MAGENTA(Material.MAGENTA_WOOL),
	LIGHT_BLUE(Material.LIGHT_BLUE_WOOL),
	YELLOW(Material.YELLOW_WOOL),
	LIME(Material.LIME_WOOL),
	PINK(Material.PINK_WOOL),
	GRAY(Material.GRAY_WOOL),
	LIGHT_GRAY(Material.LIGHT_GRAY_WOOL),
	CYAN(Material.CYAN_WOOL),
	PURPLE(Material.PURPLE_WOOL),
	BLUE(Material.BLUE_WOOL),
	BROWN(Material.BROWN_WOOL),
	GREEN(Material.GREEN_WOOL),
	RED(Material.RED_WOOL),
	BLACK(Material.BLACK_WOOL);

	private final Material material;

	private Color(Material material){
		this.material = material;
	}

	public Material getMaterial(){
		return material;
	}

	public static Color getColor(int data){
		switch(data){
		case 0:
			return WHITE;
		case 1:
			return ORANGE;
		case 2:
			return MAGENTA;
		case 3:
			return LIGHT_BLUE;
		case 4:
			return YELLOW;
		case 5:
			return LIME;
		case 6:
			return PINK;
		case 7:
			return GRAY;
		case 8:
			return LIGHT_GRAY;
		case 9:
			return CYAN;
		case 10:
			return PURPLE;
		case 11:
			return BLUE;
		case 12:
			return BROWN;
		case 13:
			return GREEN;
		case 14:
			return RED;
		case 15:
			return BLACK;
		default:
			return LIGHT_GRAY;
		}
	}

}
