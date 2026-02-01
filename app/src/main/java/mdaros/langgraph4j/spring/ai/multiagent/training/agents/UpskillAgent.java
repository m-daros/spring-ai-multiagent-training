package mdaros.langgraph4j.spring.ai.multiagent.training.agents;

import mdaros.langgraph4j.spring.ai.multiagent.training.graph.nodes.AgentNode;
import mdaros.langgraph4j.spring.ai.multiagent.training.tools.UpskillerTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UpskillAgent extends AgentNode {

	public static final String NAME = "Upskill Agent";

	public static final String SYSTEM_PROMPTS = """
			You are an **Upskilling Advisor Agent**. 
			Your role is to analyze the gap between a person’s current skills and the requirements of a given opportunity. 
			- Use the tools to compare the candidate’s skills with required skills. 
			- Recommend clear, practical next steps for learning. 
			- If possible, suggest related skills that will future-proof the candidate’s profile.
			- DO NOT TALK ABOUT NEXT STEPS OR OTHER SUGGESTIONS, your role is only to find the best upskilling advice.
			""";


	@Autowired
	public UpskillAgent ( UpskillerTools upskillerTools, ChatClient chatClient ) {

		this.agentName    = NAME;
		this.systemPrompt = SYSTEM_PROMPTS;
		this.chatClient   = chatClient;
		this.tools 		  = ToolCallbacks.from ( upskillerTools );
	}
}