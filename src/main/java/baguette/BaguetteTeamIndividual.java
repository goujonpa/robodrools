package baguette;

import java.util.Vector;
import java.awt.Color;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.QueryResultsRow;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;


public class BaguetteTeamIndividual extends TeamRobot {

    public static String RULES_FILE = "baguette/rules/robot_rules.drl";
    public static String CONSULT_ACTIONS = "consult_actions";
    
    // KBUILDER : knowledge builder. Takes a .drl file in INPUT and OUTPUTS a knowledge package that the kbase can handle
    private KnowledgeBuilder kbuilder;
    
    // KBASE : knowledge base. Contains rules, processes, functions and types models
    // Does not contain runtime infos.
    private KnowledgeBase kbase;
    
    // KSESSION : class used to dialog with the rules engine
    private StatefulKnowledgeSession ksession;
    
    private Vector<FactHandle> currentReferencedFacts = new Vector<FactHandle>();
    		
    public BaguetteTeamIndividual(){
    }
    
    public void run() {
    	DEBUG.enableDebugMode(System.getProperty("robot.debug", "true").equals("true"));

    	// Creates a knowledge base
    	createKnowledgeBase();
    	DEBUG.message("KBase created");
    	
        // COLORS
        setBodyColor(Color.blue);
        setGunColor(Color.white);
        setRadarColor(Color.red);
        setScanColor(Color.white);
        setBulletColor(Color.red);

        // Make any movement from tank, radar or gun independent
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);

        while (true) {
        	DEBUG.message("TURN BEGINS");
            loadRobotState();
            loadBattleState();

            // Fire rules
            DEBUG.message("Facts in active memory");
            DEBUG.printFacts(ksession); 
            DEBUG.message("firing rules");
            ksession.fireAllRules();
            cleanAnteriorFacts();

            // Get Actions
            Vector<Action> actions = loadActions();
            DEBUG.message("Resulting Actions");
            DEBUG.printActions(actions);

            // Execute Actions
            DEBUG.message("firing actions");
            executeActions(actions);
        	DEBUG.message("TURN ENDS\n");
            execute();  

        }

    }


    private void createKnowledgeBase() {
        String rulesFile = System.getProperty("robot.rules", BaguetteTeamIndividual.RULES_FILE);

        DEBUG.message("Creating knowledge base");
        kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        
        DEBUG.message("Loading rules since "+rulesFile);
        kbuilder.add(ResourceFactory.newClassPathResource(rulesFile, BaguetteTeamIndividual.class), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            System.err.println(kbuilder.getErrors().toString());
        }

        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        DEBUG.message("Creating session)");
        ksession = kbase.newStatefulKnowledgeSession();
    }



    private void loadRobotState() {
    	DEBUG.message("load robot state");
    	RobotState robotState = new RobotState(this);
    	currentReferencedFacts.add(ksession.insert(robotState));
    }

    private void loadBattleState() {
    	DEBUG.message("load battle state");
        BattleState battleState =
                new BattleState(
                		getBattleFieldWidth(), 
                		getBattleFieldHeight(),
                		getNumRounds(), 
                		getRoundNum(),
                		getTime(),
                		getOthers()
        );
        currentReferencedFacts.add(ksession.insert(battleState));
    }
    
    private void cleanAnteriorFacts() {
    	DEBUG.message("clean anterior facts");
        for (FactHandle referencedFact : this.currentReferencedFacts) {
            ksession.retract(referencedFact);
        }
        this.currentReferencedFacts.clear();
    }

    private Vector<Action> loadActions() {
    	DEBUG.message("load actions");
        Action action;
        Vector<Action> actionsList = new Vector<Action>();

        for (QueryResultsRow result : ksession.getQueryResults(BaguetteTeamIndividual.CONSULT_ACTIONS)) {
            action = (Action) result.get("action");  			// get the Action object
            action.setRobot(this);                      		// link it to the current robot
            actionsList.add(action);
            ksession.retract(result.getFactHandle("action")); 	// clears the fact from the active memory
        }

        return actionsList;
    }

    private void executeActions(Vector<Action> actions) {
    	DEBUG.message("execute actions");
        for (Action action : actions) {
            action.initExecution();
        }
    }

    // Insert in memory the different happening events
    public void onBulletHit(BulletHitEvent event) {
    	DEBUG.message("received onbullethit");
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onBulletHitBullet(BulletHitBulletEvent event) {
    	DEBUG.message("received on bullet hit another bullet");
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onBulletMissed(BulletMissedEvent event) {
    	DEBUG.message("received bullet miss");
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onHitByBullet(HitByBulletEvent event) {
    	DEBUG.message("received hit by bullet");
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onHitRobot(HitRobotEvent event) {
    	DEBUG.message("received on hit robot");
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onHitWall(HitWallEvent event) {
    	DEBUG.message("received on hit wall");
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onRobotDeath(RobotDeathEvent event) {
    	DEBUG.message("received on robot death");
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onScannedRobot(ScannedRobotEvent event) {
    	DEBUG.message("received on scanned robot");
    	currentReferencedFacts.add(ksession.insert(event));
    }
    
}
