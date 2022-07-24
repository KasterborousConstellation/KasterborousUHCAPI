package fr.supercomete.head.GameUtils.GameConfigurable;

public class Bound {
	public static Bound BooleanBound = new Bound(-1,2);
	private int max,min;
	public Bound(int min,int max){
		this.max=max;
		this.min=min;
	}
	public int getMax(){
		return max;
	}
	public void setMax(int max){
		this.max = max;
	}
	public int getMin(){
		return min;
	}
	public void setMin(int min){
		this.min = min;
	}
	public boolean Inbound(int x){
		return x<=max &&x>=min; 
	}
}