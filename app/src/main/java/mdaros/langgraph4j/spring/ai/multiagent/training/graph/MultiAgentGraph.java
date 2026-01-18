package mdaros.langgraph4j.spring.ai.multiagent.training.graph;

import mdaros.langgraph4j.spring.ai.multiagent.training.AgentRegistry;
import mdaros.langgraph4j.spring.ai.multiagent.training.graph.nodes.HumanApprovalNode;
import mdaros.langgraph4j.spring.ai.multiagent.training.model.State;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.bsc.langgraph4j.GraphDefinition.END;
import static org.bsc.langgraph4j.GraphDefinition.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

@Service
public class MultiAgentGraph {

	@Autowired
	private AgentRegistry agentRegistry;

	public StateGraph<State> defineGraph () throws GraphStateException {

		String opportunityAgent 	 = AgentRegistry.Agents.OPPORTUNITY_AGENT.name ();
		String upskillAgent 		 = AgentRegistry.Agents.UPSKILL_AGENT.name ();
		String connectionFinderAgent = AgentRegistry.Agents.CONNECTION_FINDER_AGENT.name ();
		String connectionAgent 		 = AgentRegistry.Agents.CONNECTION_AGENT.name ();

		var graph = new StateGraph<> ( State.SCHEMA, State :: new )
			.addNode ( opportunityAgent, node_async ( agentRegistry.get ( opportunityAgent ) ) )
			.addNode ( upskillAgent, node_async ( agentRegistry.get ( upskillAgent ) ) )
			.addNode ( connectionFinderAgent, node_async ( agentRegistry.get ( connectionFinderAgent ) ) )
			.addNode ( connectionAgent, node_async ( agentRegistry.get ( connectionAgent ) ) )
			.addNode ( "HUMAN_APPROVER", node_async ( new HumanApprovalNode () ) );

		graph.addEdge ( START, opportunityAgent );
		graph.addEdge ( opportunityAgent, upskillAgent );
		graph.addEdge ( upskillAgent, connectionFinderAgent );
		graph.addEdge ( connectionFinderAgent, "HUMAN_APPROVER" );

		graph.addConditionalEdges (
			"HUMAN_APPROVER",
			state -> CompletableFuture.completedFuture ( state.getCurrentMessage ().equals ( "REJECTED" ) ? connectionFinderAgent : connectionAgent ),
			Map.of ( connectionFinderAgent, connectionFinderAgent, connectionAgent, connectionAgent )
		);

		graph.addEdge ( connectionAgent, END );

		return graph;
	}
}