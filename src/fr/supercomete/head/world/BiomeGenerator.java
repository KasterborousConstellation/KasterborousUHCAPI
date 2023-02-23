package fr.supercomete.head.world;
import java.util.ArrayList;
import fr.supercomete.enums.BiomeGeneration;
public class BiomeGenerator {
    public static boolean generation =false;
	private int 
			diamondboost=1,
			goldboost=1,
			ironboost=1,
			coalboost=1,
			lapisboost=1
			;
	private boolean lavalake=true;
	public void setLavaLake(boolean lavalake) {
		this.lavalake = lavalake;
	}
	public boolean getLavaLake() {
		return lavalake;
	}
	public int getLapisboost() {
		return lapisboost;
	}
	public void setLapisboost(int lapisboost) {
		this.lapisboost = lapisboost;
	}
	public int getDiamondboost() {
		return diamondboost;
	}
	public void setDiamondboost(int diamondboost) {
		this.diamondboost = diamondboost;
	}
	public int getGoldboost() {
		return goldboost;
	}
	public void setGoldboost(int goldboost) {
		this.goldboost = goldboost;
	}
	public int getIronboost() {
		return ironboost;
	}
	public void setIronboost(int ironboost) {
		this.ironboost = ironboost;
	}
	public int getCoalboost() {
		return coalboost;
	}
	public void setCoalboost(int coalboost) {
		this.coalboost = coalboost;
	}
	private BiomeGeneration biome = BiomeGeneration.Roofed;
	public BiomeGenerator() {}

    public BiomeGeneration getBiome() {
		return biome;
	}
	public BiomeGenerator setBiome(BiomeGeneration biome) {
		this.biome = biome;
		return this;
	}
	public String generateWorldSetting() {
		StringBuilder str = new StringBuilder("{");
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("\"coordinateScale\":684.412");
		arr.add("\"heightScale\":684.412");
		arr.add("\"lowerLimitScale\":512.0");
		arr.add("\"upperLimitScale\":512.0");
		arr.add("\"depthNoiseScaleX\":200.0");
		arr.add("\"depthNoiseScaleZ\":200.0");
		arr.add("\"depthNoiseScaleExponent\":0.5");
		arr.add("\"mainNoiseScaleX\":80.0");
		arr.add("\"mainNoiseScaleY\":160.0");
		arr.add("\"mainNoiseScaleZ\":80.0");
		arr.add("\"baseSize\":8.5");
		arr.add("\"stretchY\":12.0");
		arr.add("\"biomeDepthWeight\":1.0");
		arr.add("\"biomeDepthOffset\":0.0");
		arr.add("\"biomeScaleWeight\":1.0");
		arr.add("\"biomeScaleOffset\":0.0");
		arr.add("\"seaLevel\":63");
		arr.add("\"useCaves\":true");
		arr.add("\"useDungeons\":true");
		arr.add("\"dungeonChance\":8");
		arr.add("\"useStrongholds\":true");
		arr.add("\"useVillages\":true");
		arr.add("\"useMineShafts\":true");
		arr.add("\"useTemples\":true");
		arr.add("\"useMonuments\":true");
		arr.add("\"useRavines\":true");
		arr.add("\"useWaterLakes\":true");
		arr.add("\"waterLakeChance\":100");
		arr.add("\"useLavaLakes\":"+lavalake);
		arr.add("\"lavaLakeChance\":80");
		arr.add("\"useLavaOceans\":false");
		arr.add("\"fixedBiome\":"+(biome.getBiomeInt())+"");
		arr.add("\"biomeSize\":10");
		arr.add("\"riverSize\":4");
		arr.add("\"dirtSize\":33");
		arr.add("\"dirtCount\":10");
		arr.add("\"dirtMinHeight\":0");
		arr.add("\"dirtMaxHeight\":256");
		arr.add("\"gravelSize\":33");
		arr.add("\"gravelCount\":8");
		arr.add("\"gravelMinHeight\":0");
		arr.add("\"gravelMaxHeight\":256");
		arr.add("\"graniteSize\":33");
		arr.add("\"graniteCount\":10");
		arr.add("\"graniteMinHeight\":0");
		arr.add("\"graniteMaxHeight\":80");
		arr.add("\"dioriteSize\":33");
		arr.add("\"dioriteCount\":10");
		arr.add("\"dioriteMinHeight\":0");
		arr.add("\"dioriteMaxHeight\":80");
		arr.add("\"andesiteSize\":33");
		arr.add("\"andesiteCount\":10");
		arr.add("\"andesiteMinHeight\":0");
		arr.add("\"andesiteMaxHeight\":80");
		arr.add("\"coalSize\":17");
		arr.add("\"coalCount\":"+(20 * coalboost));
		arr.add("\"coalMinHeight\":0");
		arr.add("\"coalMaxHeight\":128");
		arr.add("\"ironSize\":9");
		arr.add("\"ironCount\":"+(20 * ironboost));
		arr.add("\"ironMinHeight\":0");
		arr.add("\"ironMaxHeight\":64");
		arr.add("\"goldSize\":9");
		arr.add("\"goldCount\":" +(2 *goldboost));
		arr.add("\"goldMinHeight\":0");
		arr.add("\"goldMaxHeight\":32");
		arr.add("\"redstoneSize\":8");
		arr.add("\"redstoneCount\":8");
		arr.add("\"redstoneMinHeight\":0");
		arr.add("\"redstoneMaxHeight\":16");
		arr.add("\"diamondSize\":8");
		arr.add("\"diamondCount\":" +(diamondboost));
		arr.add("\"diamondMinHeight\":0");
		arr.add("\"diamondMaxHeight\":16");
		arr.add("\"lapisSize\":7");
		arr.add("\"lapisCount\":" + (lapisboost));
		arr.add("\"lapisCenterHeight\":16");
		arr.add("\"lapisSpread\":16");
		for(String i : arr) {
			str.append(i).append(",");
		}
		str = new StringBuilder(str.substring(0, str.length() - 1));
		str.append("}");
		
		return str.toString();
	}
	public boolean isLavalake() {
		return lavalake;
	}
	public void setLavalake(boolean lavalake) {
		this.lavalake = lavalake;
	}
}