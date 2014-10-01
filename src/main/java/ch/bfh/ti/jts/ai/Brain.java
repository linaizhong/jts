package ch.bfh.ti.jts.ai;

import ch.bfh.ti.jts.data.Agent;

public abstract class Brain {
    
    private final Agent agent;
    
    public Brain(final Agent agent) {
        if (agent == null) {
            throw new IllegalArgumentException("agent is null");
        }
        this.agent = agent;
        this.agent.setBrain(this);
    }
    
    public Agent getAgent() {
        return agent;
    }
    
    public abstract Decision think();
}
