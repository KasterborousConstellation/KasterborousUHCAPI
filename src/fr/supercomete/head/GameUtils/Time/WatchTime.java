package fr.supercomete.head.GameUtils.Time;
public class WatchTime {
	private final Timer id;
	private int data;
	public WatchTime(Timer id) {
		this.id=id;
		this.data=id.getBaseTime();
	}
	public Timer getId() {
		return id;
	}
	public int getData() {
		return data;
	}
	public void setData(int data) {
		this.data = data;
	}
	public void add(int amount) {
		if(this.data+amount >= id.getMinimal()) {
			this.data+=amount;
		}
	}
}