package mdaros.langgraph4j.spring.ai.multiagent.training.agents;

import io.modelcontextprotocol.client.McpSyncClient;
import mdaros.langgraph4j.spring.ai.multiagent.training.graph.nodes.AgentNode;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallback;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static mdaros.langgraph4j.spring.ai.multiagent.training.agents.OpportunityAgent.NAME;

@Component
@Qualifier ( NAME )
public class ConnectionAgent extends AgentNode {

	public static final String NAME = "Connection Agent";

	public static final String SYSTEM_PROMPTS = """
		You are a **Connection Agent**. 
		Your role is to help the user draft personalized connection requests to potential professional contacts. 
		- Use the tools to gather information about the contact’s background and interests. 
		- Craft messages that are concise, respectful, and highlight commonalities or mutual benefits. 
		- Ensure the tone is professional yet approachable.
		""";


	@Autowired
	public ConnectionAgent ( ChatClient chatClient ,List<McpSyncClient> mcpSyncClients ) {

		this.agentName    = NAME;
		this.systemPrompt = SYSTEM_PROMPTS;
		this.chatClient   = chatClient;

		if ( mcpSyncClients == null || mcpSyncClients.isEmpty () ) {

			this.tools = new ToolCallback [ 0 ];
		}
		else {

			var client = mcpSyncClients.get ( 0 );
			this.tools = client.listTools ()
				.tools ()
				.stream ()
				.map ( tool -> new SyncMcpToolCallback ( client, tool ) )
				.toArray ( ToolCallback [] :: new );
		}
	}
}