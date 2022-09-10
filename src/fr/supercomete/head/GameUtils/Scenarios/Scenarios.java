package fr.supercomete.head.GameUtils.Scenarios;
import org.bukkit.Material;
public enum Scenarios implements KasterborousScenario {
	CutClean("CutClean", Compatibility.allModes,Material.IRON_INGOT),
	CatEyes("CatEyes", Compatibility.allModes,Material.EYE_OF_ENDER),
	HasteyBoys("Hastey Boys", Compatibility.allModes,Material.IRON_PICKAXE),
	Timber("Timber", Compatibility.allModes,Material.LOG),
	NoFire("No Fire", Compatibility.allModes,Material.FLINT_AND_STEEL),
	NoSnowBall("No SnowBall", Compatibility.allModes,Material.SNOW_BALL),
	NoHorse("No Horse", Compatibility.allModes,Material.SADDLE),
	NoRod("No Rod", Compatibility.allModes,Material.FISHING_ROD),
	FastSmelting("FastSmelting", Compatibility.allModes,Material.FURNACE),
	FireEnchantLess("FireEnchantLess", Compatibility.allModes,Material.BLAZE_POWDER),
	NoFall("NoFall", Compatibility.allModes,Material.IRON_BOOTS),
	FinalHeal("Final Heal", Compatibility.allModes,Material.APPLE),
	MasterLevel("Master Level", Compatibility.allModes,Material.EXP_BOTTLE),
	NoBow("NoBow", Compatibility.allModes,Material.BOW),
	NoSword("NoSword", Compatibility.allModes,Material.IRON_SWORD),
	KillSwitch("KillSwitch", Compatibility.allModes,Material.ENDER_PEARL),
	GoldenHead("GoldenHead", Compatibility.allModes,Material.GOLDEN_APPLE),
	BetaZombie("BetaZombie", Compatibility.allModes,Material.ROTTEN_FLESH),
	DiamondLimit("DiamondLimit", Compatibility.allModes,Material.DIAMOND),
	BloodDiamond("BloodDiamond", Compatibility.allModes,Material.REDSTONE),
	NoFood("NoFood", Compatibility.allModes,Material.GOLDEN_CARROT)
    ;
	private final String name;
	private final Compatibility compatiblity;
	private final Material mat;
	Scenarios(String name, Compatibility compatibilty, Material mat){
		this.name=name;
		this.compatiblity=compatibilty;
		this.mat=mat;
	}
	@Override
	public String getName(){
	    return this.name;
	}
    @Override
	public Compatibility getCompatiblity() {
		return compatiblity;
	}
    @Override
	public Material getMat() {
		return mat;
	}
}