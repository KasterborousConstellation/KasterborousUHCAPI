package fr.supercomete.head.GameUtils.Enchants;

import org.bukkit.enchantments.Enchantment;

public class EnchantLimit {
    private final int enchantid;
    private int max;
    private final String enchantname;
    private EnchantType type;
    public EnchantLimit(String enchantname,Enchantment enchant,EnchantType type,int base){
        this.enchantid=enchant.getId();
        this.enchantname=enchantname;
        this.type=type;
        this.max=base;
    }

    public EnchantType getType() {
        return type;
    }

    public void setType(EnchantType type) {
        this.type = type;
    }

    public Enchantment getEnchant() {
        return Enchantment.getById(enchantid);
    }
    public void add(int add){
        this.max=Math.min(max+add,getEnchant().getMaxLevel());
    }
    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getEnchantname() {
        return enchantname;
    }
}
