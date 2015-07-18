package baguette;

import java.util.List;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

public final class DEBUG {
	// DEBUG : used class to pop messages on the debug console
	
	public static boolean enabledDebugMode = false;

	public static void enableDebugMode(boolean b) {
		enabledDebugMode = b;
	}

	public static void message(String string) {
		if (enabledDebugMode) {
			System.out.println("DEBUG:"+string);			
		}		
	}

	public static void printFacts(StatefulKnowledgeSession ksession) {
		if (enabledDebugMode){
			for (FactHandle f: ksession.getFactHandles()){
				System.out.println("  "+ksession.getObject(f));				
			}			
		}		
	}

	public static void printActions(List<Action> acciones) {
		if (enabledDebugMode){
			for (Action a: acciones){
				System.out.println("  "+a.toString());				
			}
		}		
	}
}
