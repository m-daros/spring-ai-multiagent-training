package mdaros.langgraph4j.spring.ai.multiagent.training.agents;

import mdaros.langgraph4j.spring.ai.multiagent.training.graph.nodes.AgentNode;
import mdaros.langgraph4j.spring.ai.multiagent.training.tools.ConnectionFinderTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static mdaros.langgraph4j.spring.ai.multiagent.training.agents.OpportunityAgent.NAME;

@Component
@Qualifier ( NAME )
public class ConnectionFinderAgent extends AgentNode {

	public static final String NAME = "ConnectionFinder Agent";

	public static final String SYSTEM_PROMPTS = """
		You are a **Connection Finder Agent**. 
		Your role is to help the user identify useful professional connections in companies or roles related to their desired opportunities. 
		- Use the tools to search for relevant people by company and role. 
		- Provide their names, roles, and IDs for possible next actions. 
		- Keep suggestions professional and realistic, as if guiding real career networking.
		- Consider all scenarios and return only one connection suggestion. 
		- This will then be either approved or rejected by a human before proceeding. If rejected, you will need to suggest another connection.
		""";



	@Autowired
	public ConnectionFinderAgent ( ConnectionFinderTools connectionFinderTools, ChatClient chatClient ) {

		this.agentName    = NAME;
		this.systemPrompt = SYSTEM_PROMPTS;
		this.chatClient   = chatClient;
		this.tools 		  = ToolCallbacks.from ( connectionFinderTools );
	}
}