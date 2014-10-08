package ch.bfh.ti.jts.simulation;

import ch.bfh.ti.jts.ai.Decision;
import ch.bfh.ti.jts.data.Element;

/**
 * Interface implemented by {@link Element} which can be simulated.
 *
 * @author ente
 */
@FunctionalInterface
public interface Simulatable {
    
    /**
     * Called in each simulation step
     *
     * @param duration
     *            duration to simulate in seconds
     * @param decision
     *            Decision made by this {@link Simulatable}, {@code null} if the
     *            {@link Simulatable} does not implement the thinkable interface
     */
    public void simulate(final double duration, final Decision decision);
}
