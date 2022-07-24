package fr.supercomete.head.role.KarvanistaPacte;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.supercomete.head.Inventory.InventoryUtils;
import fr.supercomete.head.core.Main;

public abstract class Proposal{
	protected UUID karvanista;
	protected UUID ally;
	public Proposal(UUID karvanista,UUID ally) {
		this.ally=ally;
		this.karvanista=karvanista;
	}
	/*
	Implementable: OnOtherKill, OnEpisodeTime
	 */
	protected abstract int AskCost();

	protected abstract String AskName();

	protected abstract String AskDescription();

	public abstract void tick(Player karvanista, Player ally); // Called every second

	public abstract void start(Player karvanista, Player ally); // Called once on start
	
	protected abstract Material AskMaterial();
	public String getName() {
		return AskName();
	}
	public String getDescription() {
		return AskDescription();
	}
	public int getCost() {
		return AskCost();
	}
	public boolean IsActivated = false;

	private String drawCost() {
		if (AskCost() == 0)
			return "§6Coût: §f0";
		return ((AskCost() > 0) ? "§6Coût: §c" + AskCost() : "§6Coût: §a" + AskCost());
	}

	public ItemStack getItemStack() {
		ArrayList<String> arr = Main.SplitCorrectlyString(AskDescription(), 45, "§7");
		arr.add(drawCost());
		final ItemStack stack = InventoryUtils.getItem(AskMaterial(), "§7" + AskName(), arr);
		ItemMeta meta = stack.getItemMeta();
		if (isIsActivated()) {
			meta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		stack.setItemMeta(meta);
		return stack;
	}

	public boolean isIsActivated() {
		return IsActivated;
	}

	public void setActivated(boolean isActivated) {
		IsActivated = isActivated;
	}
}