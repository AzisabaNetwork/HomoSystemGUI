package jp.azisaba.main.homogui.utils;

import java.util.Set;

import org.reflections.Reflections;

import jp.azisaba.main.homogui.gui.ClickableGUI;

public class GetClassList {

	public static Set<Class<? extends ClickableGUI>> listClasses(String packageName) {
		Reflections reflections = new Reflections(packageName);
		return reflections.getSubTypesOf(ClickableGUI.class);
	}
}