package solo_project;

public class Reward {
	String name;
	Runnable chosenReward; //runnable allows for the reward to be run on selection
	
	Reward(String name, Runnable chosenReward) {
		this.name = name;
		this.chosenReward = chosenReward;
	}
	
}
