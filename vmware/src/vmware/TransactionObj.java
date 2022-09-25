package vmware;

import java.util.Stack;

public class TransactionObj {
	
	private Stack<String> inputHistoryStack;
	private ActivityMap activityObj;
	
	public TransactionObj(ActivityMap activityObj){
		this.activityObj = activityObj;
		inputHistoryStack = new Stack<>();
	}
	
	// To store all the inputs in a transition for the banckUp in STACK.
	public void addSetCommand(String[] input,String oldValue){
		
		if (oldValue == null){ 
			inputHistoryStack.push(input[1]);
		}else {
			inputHistoryStack.push(input[1]+" "+oldValue);
		}
		
	}
	
	//Since, we are aborting to the transition, 
	//will UNDO all the commands from commandStack
	public void revertTrans(){
		while (!inputHistoryStack.isEmpty()){   
			// fetching the history of the inputs in the given transition
			String[] history = inputHistoryStack.pop().split(" ");
			if (history.length==1){
				// UNDO for added data in transition
				activityObj.map.remove(history[0]);
			}else {
				// UNDO for overwritten and removed data
				activityObj.map.put(history[0],history[1]);
			}
		}
	}

}