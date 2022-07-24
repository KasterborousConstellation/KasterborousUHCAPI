package fr.supercomete.head.role.RoleModifier;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;

public interface InvisibleRoleWithArmor {
	
	default void hide(Player player) {
		if (player == null || player.getInventory() == null)
			return;
		int id = player.getEntityId();
		PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(id, 1, null);
		PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(id, 2, null);
		PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(id, 3, null);
		PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(id, 4, null);
		PacketPlayOutEntityEquipment itemHand = new PacketPlayOutEntityEquipment(id, 0, null);

		for (Player online : Bukkit.getOnlinePlayers()) {
			if (online == player)
				continue;

			try {
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(helmet);
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(chestplate);
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(leggings);
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(boots);
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(itemHand);

			} catch (Exception e) {
				System.out.println("(PacketUtil) Error with hideArmor(Player)");
				e.printStackTrace();
			}
		}
	}
	default void show(Player player) {
		if (player == null || player.getInventory() == null)
			return;
		int id = player.getEntityId();
		PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(id, 1,
				CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
		PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(id, 2,
				CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
		PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(id, 3,
				CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
		PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(id, 4,
				CraftItemStack.asNMSCopy(player.getInventory().getBoots()));
		PacketPlayOutEntityEquipment itemHand = new PacketPlayOutEntityEquipment(id, 0,
				CraftItemStack.asNMSCopy(player.getItemInHand()));
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (online == player)
				continue;

			try {
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(helmet);
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(chestplate);
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(leggings);
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(boots);
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(itemHand);

			} catch (Exception e) {
				System.out.println("(PacketUtil) Error with showArmor(Player)");
				e.printStackTrace();
			}
		}
	}
}
