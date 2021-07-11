package com.socgam.server.model.CombatSystem;

import com.socgam.server.model.CombatSystem.SnapShotSystem.ActionSnapshot;
import com.socgam.server.model.CombatSystem.SnapShotSystem.CombatSnapshot;
import com.socgam.server.model.CombatSystem.SnapShotSystem.CombatSnapshotTimerTask;
import com.socgam.server.model.CombatSystem.SnapShotSystem.PlayerPassedActionSnapshot;

import java.util.*;
import java.util.stream.Collectors;

public class CombatInstance {
    // Attributes
    private String combatID;
    private List<Combatant> combatants;
    private int combatRound;
    private CombatPhases currentPhase;
    private boolean running;
    private HashMap<Combatant, String> combatantToIDs;
    private HashMap<String, Combatant> comIDToCombatants;
    private Faction winner; // Unspecified if still running
    private Timer snapShotTimer;

    // Combatstatus - Client communication
    //private int lastSequence;
    private List<CombatSnapshot> combatSnapshots;
    private final Object lock = new Object();
    private Queue<ActionSnapshot> actionsSinceLastSnapshot;

    // Constructors
    public CombatInstance(String combatID) {
        this.combatID = combatID;
        this.combatantToIDs = new HashMap<>();
        this.combatants = new LinkedList<Combatant>();
        combatRound = 0;
        this.combatSnapshots = new ArrayList<>();
        //lastSequence = 0;
        actionsSinceLastSnapshot = new LinkedList<>();
        comIDToCombatants = new HashMap<>();
    }

    // Methods
    public void addCombatant(Combatant combatant) {
        // ToDo work over
        // ToDo needs to add Combatant from ID
        // ToDo need to load deck from database
        int id = combatants.size();
        combatants.add(combatant);
        combatantToIDs.put(combatant, combatant.getUserId());
        comIDToCombatants.put(combatant.getUserId(), combatant);
    }

    public void addSnapshot(CombatSnapshot combatSnapshot) {
        synchronized (combatSnapshots) {
            //combatSnapshots.add(combatSnapshot.getSequenceNumber(), combatSnapshot);
            combatSnapshots.add(combatSnapshot);
            //System.out.println("Snapshot added! Last Sequence: "+getLastSequence() + " length " + combatSnapshots.size());
        }
    }

    public void addActionToQueue(ActionSnapshot actionSnapshot) {
        // ToDo Actionsnapshotgeneration
        synchronized(actionsSinceLastSnapshot) {
            actionsSinceLastSnapshot.add(actionSnapshot);
        }
    }

    public void resetActionQueue() {
        synchronized (actionsSinceLastSnapshot) {
            actionsSinceLastSnapshot.clear();
        }
    }

    public String getCombatantID(Combatant combatant) {
        return combatantToIDs.get(combatant);
    }

    public Combatant getCombatantFromID(String id) {
        return comIDToCombatants.get(id);
    }

    public List<Combatant> getCombatants() {
        return combatants;
    }

    public List<Combatant> getCombatantByFaction(Faction faction) {
        return combatants.stream().filter(c -> c.getFaction().equals(faction)).collect(Collectors.toList());
    }

    public void endCombat(Faction winner) {
        running = false;
        this.winner = winner;
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        snapShotTimer.cancel();

        if(CONSTANTS.DEBUG) {
            System.out.println("Kampf beendet! Sieger: "+winner);
        }
        return;
    }

    public boolean isRunning() {
        return running;
    }

    public void startCombatLoop() {
        running = true;
        // CombatSnapshotUpdates-Timertask
        snapShotTimer = new Timer();
        snapShotTimer.scheduleAtFixedRate(new CombatSnapshotTimerTask(this)
                , CONSTANTS.TIMETOFIRSTSNAPSHOT, CONSTANTS.SNAPSHOTUPDATETIME);
        // Start Combat Loop
        CombatLoop combatLoop = new CombatLoop(this);
        Thread thread = new Thread(combatLoop);
        thread.start();
    }


    public Queue<ActionSnapshot> getActionsSinceLastSnapshot() {
        synchronized (lock) {
            return actionsSinceLastSnapshot;
        }
    }

    public String getCombatID() {
            return combatID;
        }

    public int getCombatRound() {
        synchronized (lock) {
            return combatRound;
        }}

    public void advanceCombatRound() {
        synchronized (lock) {
            combatRound++;
        }
    }

    public Faction getWinner() {
        return winner;
    }

    public CombatPhases getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(CombatPhases currentPhase) {
        this.currentPhase = currentPhase;
    }



    /////---------------------------/////
    //// ----- Client Methods ----- /////
    /////---------------------------/////

    // Client method to communicate that a player has passed
    public void combatantHasPassed(String combatantID) {
        getCombatantFromID(combatantID).pass(true);
        // Add ActionSnapShot
        addActionToQueue(new PlayerPassedActionSnapshot(combatantID));
    }

    // Client method to communicate that a card was played
    public void addPlayedCard(String cardID, String playerID, String targetID) {
        Combatant player = comIDToCombatants.get(playerID);
        Combatant target = comIDToCombatants.get(targetID);
        player.addPlayedCard(target, cardID);
    }

    // Client method to retrieve CombatSnapShots
    public Optional<CombatSnapshot> retrieveSnapShot(int lastKnownSequenceNumber) {
        Optional<CombatSnapshot> c = Optional.empty();
        if(lastKnownSequenceNumber < getLastSequence()) {
            synchronized (combatSnapshots) {
                if(combatSnapshots.size() > lastKnownSequenceNumber+1)
                    c = Optional.of(combatSnapshots.get(lastKnownSequenceNumber+1));
            }
        }
        return c;
    }

    public int getLastSequence() {
        return combatSnapshots.size()-1;
    }

}
