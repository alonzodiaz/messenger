/**
 * 
 */
package mini.miniModel;

/**
 * @author lonzd
 *
 */
public class MiniModel {
	
	/**
	 * The Mini Model 2 Mini View Adapter
	 */
	IMiniModel2MiniViewAdpt miniModel2viewAdpt;

	/**
	 * @param adpt : The Mini Model 2 Mini View Adapter 
	 * 
	 */
	public MiniModel(IMiniModel2MiniViewAdpt adpt) {
		this.miniModel2viewAdpt = adpt;
	}
	

	/**
	 * Start the mini model
	 */
	public void start() {
		System.out.println("Starting MiniModel");
	}

}
