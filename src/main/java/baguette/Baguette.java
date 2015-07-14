package baguette;

import java.util.Vector;
import java.awt.Color;
import java.awt.geom.Point2D;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.QueryResultsRow;
import robocode.AdvancedRobot;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.RobotStatus;
import robocode.ScannedRobotEvent;
import robocode.StatusEvent;


public class Baguette extends AdvancedRobot {

    public static String RULES_FILE = "baguette/rules/robot_rules.drl";
    public static String CONSULT_ACTIONS = "Consulting actions";
    
    // KBUILDER : knowledge builder. Takes a .drl file in INPUT and OUTPUTS a knowledge package that the kbase can handle
    private KnowledgeBuilder kbuilder;
    
    // KBASE : knowledge base. Contains rules, processes, functions and types models
    // Does not contain runtime infos.
    private KnowledgeBase kbase;
    
    // KSESSION : class used to dialog with the rules engine
    private StatefulKnowledgeSession ksession;
    
    
    private Vector<FactHandle> currentReferencedFacts = new Vector<FactHandle>();

    
    public Baguette(){
    	// Nothing in constructor
    }
    

    public void run() {
    	DEBUG.enableDebugMode(System.getProperty("robot.debug", "true").equals("true"));    	

    	// Creates a knowledge base
    	createKnowledgeBase();
    	
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
            loadRobotState();// HEHRHUHDUHDHUDHUDUHDSHUSUH
            loadBattleState();

            // Fire rules
            DEBUG.message("Facts in active memory");
            DEBUG.printFacts(ksession);           
            ksession.fireAllRules();
            cleanAnteriorFacts();

            // Get Actions
            Vector<Accion> actions = loadActions();
            DEBUG.message("Resulting Actions");
            DEBUG.printActions(actions);

            // Execute Actions
            ejecutarAcciones(acciones);
        	DEBUG.message("fin turno\n");
            execute();  // Informs the robocode that the turn ended (blocking call)

        }

    }


    private void createKnowledgeBase() {
        String rulesFile = System.getProperty("robot.reglas", Baguette.RULES_FILE);

        DEBUG.mensaje("crear base de conocimientos");
        kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        
        DEBUG.mensaje("cargar reglas desde "+ficheroReglas);
        kbuilder.add(ResourceFactory.newClassPathResource(ficheroReglas, Baguette.class), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            System.err.println(kbuilder.getErrors().toString());
        }

        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        DEBUG.mensaje("crear sesion (memoria activa)");
        ksession = kbase.newStatefulKnowledgeSession();
    }



    private void loadRobotState() {
    	EstadoRobot estadoRobot = new EstadoRobot(this);
    	currentReferencedFacts.add(ksession.insert(estadoRobot));
    }

    private void loadBattleState() {
        EstadoBatalla estadoBatalla =
                new EstadoBatalla(getBattleFieldWidth(), getBattleFieldHeight(),
                getNumRounds(), getRoundNum(),
                getTime(),
                getOthers());
        currentReferencedFacts.add(ksession.insert(estadoBatalla));
    }

    private void cleanAnteriorFacts() {
        for (FactHandle referenciaHecho : this.currentReferencedFacts) {
            ksession.retract(referenciaHecho);
        }
        this.currentReferencedFacts.clear();
    }

    private Vector<Accion> loadActions() {
        Accion accion;
        Vector<Accion> listaAcciones = new Vector<Accion>();

        for (QueryResultsRow resultado : ksession.getQueryResults(Baguette.CONSULT_ACTIONS)) {
            accion = (Accion) resultado.get("accion");  // Obtener el objeto accion
            accion.setRobot(this);                      // Vincularlo al robot actual
            listaAcciones.add(accion);
            ksession.retract(resultado.getFactHandle("accion")); // Eliminar el hecho de la memoria activa
        }

        return listaAcciones;
    }

    private void ejecutarAcciones(Vector<Accion> acciones) {
        for (Accion accion : acciones) {
            accion.iniciarEjecucion();
        }
    }

    // Insert in memory the different happening events
    public void onBulletHit(BulletHitEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onBulletHitBullet(BulletHitBulletEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onBulletMissed(BulletMissedEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onHitByBullet(HitByBulletEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onHitRobot(HitRobotEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onHitWall(HitWallEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onRobotDeath(RobotDeathEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }

    public void onScannedRobot(ScannedRobotEvent event) {
    	currentReferencedFacts.add(ksession.insert(event));
    }
}
