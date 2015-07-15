package baguette;

import java.util.List;
import java.util.Vector;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.QueryResultsRow;

import robocode.*;

public class RulesChecker {

    public static String RULES_FILE = "baguette/rules/robot_rules.drl";
    public static String CONSULT_ACTIONS = "consult_actions";
    private KnowledgeBuilder kbuilder;
    private KnowledgeBase kbase;
    private StatefulKnowledgeSession ksession;
    private Vector<FactHandle> currentReferencedFacts = new Vector<FactHandle>();

    public RulesChecker() {
    	String debugMode = System.getProperty("robot.debug", "true");
    	DEBUG.enableDebugMode(debugMode.equals("true"));
        createKnowledgeBase();
        loadEvents();
    }

	public void loadEvents() {
		ScannedRobotEvent e = new ScannedRobotEvent("pepe", 100, 10, 10, 10, 10);
        FactHandle referencedFact = ksession.insert(e);
        currentReferencedFacts.add(referencedFact);
        // add more facts ....
        
        DEBUG.message("Facts in active memory");
        DEBUG.printFacts(ksession);
        ksession.fireAllRules();
        List<Action> actions = loadActions();
        DEBUG.message("Resulting actions");
        DEBUG.printActions(actions);
        
	}

    private void createKnowledgeBase() {
    	String rulesFile;
    	rulesFile = System.getProperty("robot.reglas", RulesChecker.RULES_FILE);
    	
    	DEBUG.message("Creating knowledge base");
        kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        
    	DEBUG.message("Load rules from "+rulesFile);
        kbuilder.add(ResourceFactory.newClassPathResource(rulesFile, RulesChecker.class), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            System.err.println(kbuilder.getErrors().toString());
        }

        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );

        DEBUG.message("Creating Session");
        ksession = kbase.newStatefulKnowledgeSession();
    }

    public static void main(String args[]) {
        RulesChecker d = new RulesChecker();
    }

    private List<Action> loadActions() {
        Action action;
        Vector<Action> actionsList = new Vector<Action>();

        for (QueryResultsRow result : ksession.getQueryResults(Baguette.CONSULT_ACTIONS)) {
            action = (Action) result.get("action");  
            action.setRobot(null); 
            actionsList.add(action);
            ksession.retract(result.getFactHandle("action"));
        }

        return actionsList;
    }
}
