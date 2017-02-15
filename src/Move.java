
/**
 * Used to store old and new indicies when generating possible children moves
 * @author Mohamed Dahrouj
 *
 */
public class Move {

	int oldIndex;
	int newIndex;
	
	public Move(int newIndex, int oldIndex){
		this.oldIndex = oldIndex;
		this.newIndex = newIndex;
	}
	
	public int getOldIndex() {
		return oldIndex;
	}
	
	public void setOldIndex(int oldIndex) {
		this.oldIndex = oldIndex;
	}
	
	public int getNewIndex() {
		return newIndex;
	}
	
	public void setNewIndex(int newIndex) {
		this.newIndex = newIndex;
	}
	
}
