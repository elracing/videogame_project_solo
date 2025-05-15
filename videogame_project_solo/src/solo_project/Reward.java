package solo_project;

public class Reward {
	String name;
	Runnable chosenReward1;; //runnable allows for the reward to be run on selection
	Runnable chosenReward2; //optional for specific changes that require 2 functions
	
	Reward(String name, Runnable chosenReward) {
		this.name = name;
		this.chosenReward1 = chosenReward;
	}
	
    public Reward(String name, Runnable chosenReward1, Runnable chosenReward2) {
        this.name = name;
        this.chosenReward1 = chosenReward1;
        this.chosenReward2 = chosenReward2;
    }
}
