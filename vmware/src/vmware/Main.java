package vmware;
import java.io.*;
import java.util.*;
public class Main {
	public static void main(String[] args) throws IOException {
		try (Scanner scan = new Scanner(System.in)) {
			ActivityMap newDB = new ActivityMap();
			System.out.println("WELCOME TO KEY VALUE INMEMORY PROJECT \nTip: 'QUIT' for exiting the project.");
			System.out.println("\n\n----------------------------------------\n\n");
			// Running REPL Loop
			while (true){  
				String input = scan.nextLine();
				
				if (input.equals("QUIT")){
					System.out.println("Exiting...");
					break;    //this will break the loop when we hit END
				}  
				else
				{
					newDB.actionCenter(input);
				}
			} //while exit
		}  // scanner exit

	}  // function exit

}


class ActivityMap {
	
	HashMap<String,String> map;
	Stack<TransactionObj> transactionObjStack;
	String oldValue;
	public ActivityMap(){
		map = new HashMap<>();
		transactionObjStack = new Stack<>();
	}
	
	public void actionCenter(String input){
		String[] inputArr =  input.split(" ");  // split into word array
		switch(inputArr[0]) {
		
		//------------------------------------------------------------------------------
		case "WRITE":
			if(inputArr.length!=3) {
				System.out.println("Please Provide Proper Write Value.");
				return;
			}
			
			oldValue=map.put(inputArr[1], inputArr[2]);
			if (!transactionObjStack.isEmpty()){
				transactionObjStack.peek().addSetCommand(inputArr,oldValue);
			}
			return;
		//------------------------------------------------------------------------------
		case "READ":
			System.out.println(map.getOrDefault(inputArr[1],"Key not found: "+inputArr[1]));
			return;
		//------------------------------------------------------------------------------			
		case "DELETE":
			if(!map.containsKey(inputArr[1])) {
				System.out.println("No Key "+inputArr[1]+" in memory.");
				return;
			}
			oldValue=map.remove(inputArr[1]);
			if (!transactionObjStack.isEmpty()) {
				transactionObjStack.peek().addSetCommand(inputArr,oldValue);
			}
			return;
		//------------------------------------------------------------------------------	
		case "START":
			// To add the current object of TransactionObj class to record the command history
			System.out.println("\n-----------Transition Started---------------\n");
			transactionObjStack.push(new TransactionObj(this));
			return;
		//------------------------------------------------------------------------------	
		case "ABORT":
			this.abort();
			return;
		//------------------------------------------------------------------------------
		case "COMMIT":
			this.commit();
			return;
		//------------------------------------------------------------------------------
		default:
			System.out.println("Worng Input");
		}
		
	}
	
	// This function will undo the all the previous input in the given transition
	public void abort(){
		if (transactionObjStack.isEmpty()){
			System.out.println("No Transactions Available");
			return;
		}
		System.out.println("\n-----------Transition Aborted---------------\n");
		transactionObjStack.pop().revertTrans();
	}
	
	// This function will simply delete the history from stack, can not be recovered after this
	public void commit(){
		if (transactionObjStack.isEmpty()){
			System.out.println("No Transactions Available");
			return;
		}
		System.out.println("\n-----------Transition Saved---------------\n");
		transactionObjStack.clear();
	}
	
}
