package fr.supercomete.head.GameUtils;

import org.bukkit.ChatColor;

public class ColorScheme {
	private ChatColor primary;
	private ChatColor secondary;
	private ChatColor tertiary;
	public ColorScheme(ChatColor primary, ChatColor secondary, ChatColor tertiary) {
		this.primary = primary;
		this.secondary = secondary;
		this.tertiary = tertiary;
	}
	public ChatColor getPrimary() {
		return primary;
	}
	public void setPrimary(ChatColor primary) {
		this.primary = primary;
	}
	public ChatColor getSecondary() {
		return secondary;
	}
	public void setSecondary(ChatColor secondary) {
		this.secondary = secondary;
	}
	public ChatColor getTertiary() {
		return tertiary;
	}
	public void setTertiary(ChatColor tertiary) {
		this.tertiary = tertiary;
	}
}
