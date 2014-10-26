package ch.bfh.ti.jts.simulation;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import ch.bfh.ti.jts.ai.agents.FullSpeedAgent;
import ch.bfh.ti.jts.console.Console;
import ch.bfh.ti.jts.console.commands.Command;
import ch.bfh.ti.jts.data.Net;
import ch.bfh.ti.jts.data.Route;

/**
 * Simulates traffic on a @{link ch.bfh.ti.jts.data.Net}
 *
 * @author ente
 */
public class Simulation {
    
    /**
     * Net for which to simulate traffic.
     */
    private final Net                    simulateNet;
    /**
     * Factor by which the simulation should take place. 1 means real time
     * speed.
     */
    private final double                 timeFactor      = 1;
    /**
     * Factor by which the spawning should take place. 1 means real time speed.
     */
    private final double                 spawnTimeFactor = 1440;                       // 1
                                                                                        // day
                                                                                        // in
                                                                                        // one
                                                                                        // minute
    /**
     * Absolute time at which the simulation started (nanoseconds).
     */
    private long                         startTime;
    /**
     * Absolute time at which the the lastest simulation step took place
     * (nanoseconds).
     */
    private long                         lastTick;
    /**
     * Time that has passed since the last simulation step [s].
     */
    private double                       timeDelta;
    /**
     * Total time that has passed since the begin of the simulation [s].
     */
    private double                       timeTotal;
    /**
     * Commands the simulation should execute.
     */
    private final BlockingQueue<Command> commands        = new LinkedBlockingQueue<>();
    
    private Console                      console;
    
    public Simulation(final Net simulateNet) {
        this.simulateNet = simulateNet;
        start();
    }
    
    public void start() {
        startTime = System.nanoTime();
        lastTick = startTime;
    }
    
    /**
     * Do a simulation step
     */
    public void tick() {
        // do time calculations
        final long now = System.nanoTime();
        timeDelta = (now - lastTick) * 1E-9 * timeFactor;
        timeTotal = (now - startTime) * 1E-9 * timeFactor;
        
        // execute commands
        while (!commands.isEmpty()) {
            Command command = commands.poll();
            String output = command.execute(this);
            getConsole().write(output);
        }
        
        // do agent spawning
        spawn(timeTotal * spawnTimeFactor);
        
        // think
        simulateNet.think();
        
        // simulate
        simulateNet.simulate(timeDelta);
        
        // set lastTick for timediff
        lastTick = now;
    }
    
    /**
     * Handles spawning of agents in the correct moment of the simulation.
     * 
     * @param totalDurationSeconds
     *            total amount of time passed since begin of the simulation
     */
    // TODO: move into simulation method of Net class?
    private void spawn(final double totalDurationSeconds) {
        List<Route> routes = simulateNet.getRoutes().stream().sequential().filter(x -> x.getDepartureTime() < totalDurationSeconds).collect(Collectors.toList());
        for (Route route : routes) {
            simulateNet.addAgent(new FullSpeedAgent(), route);
            simulateNet.getRoutes().remove(route);
            Logger.getGlobal().log(Level.INFO, "agent spawned");
        }
    }
    
    public double getTimeTotal() {
        return timeTotal;
    }
    
    public BlockingQueue<Command> getCommands() {
        return commands;
    }
    
    public Console getConsole() {
        return console;
    }
    
    public void setConsole(Console console) {
        this.console = console;
    }
    
    public Net getNet() {
        return simulateNet;
    }
}
