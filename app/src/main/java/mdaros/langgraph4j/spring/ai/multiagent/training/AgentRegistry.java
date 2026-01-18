package mdaros.langgraph4j.spring.ai.multiagent.training;

import io.modelcontextprotocol.client.McpSyncClient;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import mdaros.langgraph4j.spring.ai.multiagent.training.graph.nodes.AgentNode;
import mdaros.langgraph4j.spring.ai.multiagent.training.tools.ConnectionFinderTools;
import mdaros.langgraph4j.spring.ai.multiagent.training.tools.JobsAndOpportunityTools;
import mdaros.langgraph4j.spring.ai.multiagent.training.tools.UpskillerTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallback;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AgentRegistry {

	public enum Agents {

		OPPORTUNITY_AGENT,
		UPSKILL_AGENT,
		CONNECTION_FINDER_AGENT,
		CONNECTION_AGENT
	}

	private List<AgentNode> agents;
	private ChatClient      chatClient;
	private List<McpSyncClient> mcpSyncClients;

	@PostConstruct
	public void initialize () {

		this.agents = new ArrayList<> ();

		AgentNode opportunityAgent = new AgentNode ( Agents.OPPORTUNITY_AGENT.name (),
				"""
				You are an **Opportunity Discovery Agent**. 
				Your role is to act as a career advisor who identifies job and project opportunities that match a user’s existing skills and interests. 
				- Always gather relevant opportunities using the available tools. 
				- Find a single opportunity that best aligns with the user’s profile and goals.
				- Return only one opportunity suggestion, along with a brief explanation of why it is a good fit.
				- DO NOT TALK ABOUT NEXT STEPS OR OTHER SUGGESTIONS, your role is only to find the best opportunity match.
				""",
				ToolCallbacks.from ( new JobsAndOpportunityTools () ),
				chatClient
		);

		AgentNode upskillAgent = new AgentNode ( Agents.UPSKILL_AGENT.name (),
				"""
				You are an **Upskilling Advisor Agent**. 
				Your role is to analyze the gap between a person’s current skills and the requirements of a given opportunity. 
				- Use the tools to compare the candidate’s skills with required skills. 
				- Recommend clear, practical next steps for learning. 
				- If possible, suggest related skills that will future-proof the candidate’s profile.
				- DO NOT TALK ABOUT NEXT STEPS OR OTHER SUGGESTIONS, your role is only to find the best upskilling advice.
				""",
				ToolCallbacks.from ( new UpskillerTools () ),
				chatClient
		);

		AgentNode connectionFinderAgent = new AgentNode ( Agents.CONNECTION_FINDER_AGENT.name (),
				"""
				You are a **Connection Finder Agent**. 
				Your role is to help the user identify useful professional connections in companies or roles related to their desired opportunities. 
				- Use the tools to search for relevant people by company and role. 
				- Provide their names, roles, and IDs for possible next actions. 
				- Keep suggestions professional and realistic, as if guiding real career networking.
				- Consider all scenarios and return only one connection suggestion. 
				- This will then be either approved or rejected by a human before proceeding. If rejected, you will need to suggest another connection.
				""",
				ToolCallbacks.from ( new ConnectionFinderTools () ),
				chatClient
		);

		//Connection Agent tools is added from MCP server

		ToolCallback [] toolCallbacks = mcpSyncClients.get ( 0 ).listTools ()
			.tools ()
			.stream ()
			.map ( tool -> new SyncMcpToolCallback ( mcpSyncClients.get ( 0 ), tool ) )
			.toArray ( ToolCallback [] :: new );

		AgentNode connectionAgent = new AgentNode ( Agents.CONNECTION_AGENT.name (),
				"""
				You are a **Connection Agent**. 
				Your role is to help the user draft personalized connection requests to potential professional contacts. 
				- Use the tools to gather information about the contact’s background and interests. 
				- Craft messages that are concise, respectful, and highlight commonalities or mutual benefits. 
				- Ensure the tone is professional yet approachable.
				""",
				toolCallbacks,
				chatClient
		);

		agents.add ( opportunityAgent );
		agents.add ( upskillAgent );
		agents.add ( connectionFinderAgent );
		agents.add ( connectionAgent );
	}

	public AgentNode get ( String agentName ) {

		Optional<AgentNode> agent = agents.stream ()
			.filter ( agentNode -> agentNode.getAgentName ().equals ( agentName ) )
			.findFirst ();

		return agent.orElseThrow ( () -> new RuntimeException ( "Agent not found: " + agentName ) );
	}
}