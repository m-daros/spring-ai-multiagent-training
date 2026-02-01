package mdaros.langgraph4j.spring.ai.multiagent.training.graph;

import mdaros.langgraph4j.spring.ai.multiagent.training.agents.ConnectionAgent;
import mdaros.langgraph4j.spring.ai.multiagent.training.agents.ConnectionFinderAgent;
import mdaros.langgraph4j.spring.ai.multiagent.training.agents.OpportunityAgent;
import mdaros.langgraph4j.spring.ai.multiagent.training.agents.UpskillAgent;
import mdaros.langgraph4j.spring.ai.multiagent.training.graph.nodes.HumanApprovalNode;
import mdaros.langgraph4j.spring.ai.multiagent.training.model.State;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.bsc.langgraph4j.GraphDefinition.END;
import static org.bsc.langgraph4j.GraphDefinition.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

@Service
public class MultiAgentGraph {

	@Autowired
	//@Qualifier ( OpportunityAgent.NAME )
	private OpportunityAgent opportunityAgent;

	@Autowired
	//@Qualifier ( UpskillAgent.NAME )
	private UpskillAgent upskillAgent;

	@Autowired
	//@Qualifier ( ConnectionFinderAgent.NAME )
	private ConnectionFinderAgent connectionFinderAgent;

	@Autowired
	//@Qualifier ( ConnectionAgent.NAME )
	private ConnectionAgent connectionAgent;


	public StateGraph<State> defineGraph () throws GraphStateException {

		var graph = new StateGraph<> ( State.SCHEMA, State :: new )
			.addNode ( OpportunityAgent.NAME, node_async ( opportunityAgent ) )
			.addNode ( UpskillAgent.NAME, node_async ( upskillAgent ) )
			.addNode ( ConnectionFinderAgent.NAME, node_async ( connectionFinderAgent ) )
			.addNode ( ConnectionAgent.NAME , node_async ( connectionAgent ) )
			.addNode ( "HUMAN_APPROVER", node_async ( new HumanApprovalNode () ) );

		graph.addEdge ( START, OpportunityAgent.NAME );
		graph.addEdge ( OpportunityAgent.NAME, UpskillAgent.NAME );
		graph.addEdge ( UpskillAgent.NAME, ConnectionFinderAgent.NAME );
		graph.addEdge ( ConnectionFinderAgent.NAME, "HUMAN_APPROVER" );

		graph.addConditionalEdges (
			"HUMAN_APPROVER",
			state -> CompletableFuture.completedFuture ( state.getCurrentMessage ().equals ( "REJECTED" ) ? ConnectionFinderAgent.NAME : ConnectionAgent.NAME ),
			Map.of ( ConnectionFinderAgent.NAME, ConnectionFinderAgent.NAME, ConnectionAgent.NAME, ConnectionAgent.NAME )
		);

		graph.addEdge ( ConnectionFinderAgent.NAME, END );

		return graph;
	}
}