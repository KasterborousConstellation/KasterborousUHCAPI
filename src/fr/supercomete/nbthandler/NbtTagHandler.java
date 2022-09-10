package fr.supercomete.nbthandler;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
public final class NbtTagHandler {
	public static String uuidtag="supercomete.uuidtag";
	public static ItemStack createItemStackWithUUIDTag(ItemStack item,int uuid) {
		net.minecraft.server.v1_8_R3.ItemStack itemnms=CraftItemStack.asNMSCopy(item);
		ItemMetadata.setMetadata(itemnms,uuidtag,uuid);
		return CraftItemStack.asBukkitCopy(itemnms);
	}
	public static int getUUIDTAG(ItemStack item) {
		if(ItemMetadata.hasMetadataByBukkit(item, uuidtag)) {
			return (int) ItemMetadata.getMetadataByBukkit(item, uuidtag);
		}else return 0;
	}
	public static boolean hasUUIDTAG(ItemStack item) {
		return ItemMetadata.hasMetadataByBukkit(item, uuidtag);
	}
	public static ItemStack addAnyTag(ItemStack item,String tag,Object tagvalue) {
		net.minecraft.server.v1_8_R3.ItemStack itemnms=CraftItemStack.asNMSCopy(item);
		ItemMetadata.setMetadata(itemnms,tag,tagvalue);
		return CraftItemStack.asBukkitCopy(itemnms);
	}
	public static boolean hasAnyTAG(ItemStack item,String tag) {
		return ItemMetadata.hasMetadataByBukkit(item, tag);
	}
	public static Object getAnyTag(ItemStack item,String tag) {
		if(ItemMetadata.hasMetadataByBukkit(item, tag)) {
			return ItemMetadata.getMetadataByBukkit(item, tag);
		}else return 0;
	}
/*	
	Table of UUID value

	1| Vastra speed boost
	2| River Vortex
	3| Infection Tool 
	4| Supreme Exchange Tool
 	5| Cyber-Brouilleur
	6| DalekCaan Temporal Rift
	7| Captain Trap
	8| Cyberium Tracer
	9| GreatIntelligence Freeze
   10| Bill Potts Potion
   11| Confusion Karvanista
   12| Stasis Karvanista
   13| Cage Karvanista
   14| Flux item Tecteun
   15| Temporal FlashBack WeapingAngel



   101 Joke Flavio
   102 Mango Flavio
   103 Mein kampf Lucien
*/	
}
