package fr.supercomete.head.GameUtils.Time;
import fr.supercomete.head.core.Main;
public final class BoundedWatchTime extends WatchTime{
	private final Timer bound;
	public BoundedWatchTime(Timer id,Timer bound) {
		super(id);
		this.bound=bound;
	}
	public boolean InBound() {
		return super.getData() >= super.getId().getMinimal()&& super.getData() > Main.currentGame.getTimer(bound).getData();
	}
	public boolean InBound(int amount) {
		return super.getData()+amount >= super.getId().getMinimal()&& super.getData()+amount > Main.currentGame.getTimer(bound).getData();
	}
	@Override
	public void add(int amount) {
		if(InBound(amount)) {
			super.setData(super.getData() + amount);
		}
	}
}