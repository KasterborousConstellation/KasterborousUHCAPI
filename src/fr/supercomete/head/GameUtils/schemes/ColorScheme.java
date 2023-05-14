package fr.supercomete.head.GameUtils.schemes;

import org.bukkit.ChatColor;

public class ColorScheme {
	private final ChatColor[] colors;
	public ColorScheme(ChatColor primary, ChatColor secondary, ChatColor tertiary) {
		colors = new ChatColor[]{primary,secondary,tertiary};
	}
	public ChatColor getPrimary() {
		return colors[0];
	}
	public ChatColor getSecondary() {
		return colors[1];
	}
	public ChatColor getTertiary() {
		return colors[2];
	}
}
