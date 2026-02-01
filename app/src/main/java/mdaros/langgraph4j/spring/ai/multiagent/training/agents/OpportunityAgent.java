package mdaros.langgraph4j.spring.ai.multiagent.training.agents;

import mdaros.langgraph4j.spring.ai.multiagent.training.graph.nodes.AgentNode;
import mdaros.langgraph4j.spring.ai.multiagent.training.tools.JobsAndOpportunityTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class OpportunityAgent extends AgentNode {

	public static final String NAME = "Opportunity Agent";

	public static final String SYSTEM_PROMPTS = """
		You are an **Opportunity Discovery Agent**. 
		Your role is to act as a career advisor who identifies job and project opportunities that match a user’s existing skills and interests. 
		- Always gather relevant opportunities using the available tools. 
		- Find a single opportunity that best aligns with the user’s profile and goals.
		- Return only one opportunity suggestion, along with a brief explanation of why it is a good fit.
		- DO NOT TALK ABOUT NEXT STEPS OR OTHER SUGGESTIONS, your role is only to find the best opportunity match.
		""";


	@Autowired
	public OpportunityAgent ( JobsAndOpportunityTools jobsAndOpportunityTools, ChatClient chatClient ) {

		this.agentName 	  = NAME;
		this.systemPrompt = SYSTEM_PROMPTS;
		this.chatClient   = chatClient;
		this.tools 		  = ToolCallbacks.from ( jobsAndOpportunityTools );
	}
}