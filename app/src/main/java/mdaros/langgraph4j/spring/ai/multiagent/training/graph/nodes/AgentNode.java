package mdaros.langgraph4j.spring.ai.multiagent.training.graph.nodes;

import lombok.*;
import mdaros.langgraph4j.spring.ai.multiagent.training.model.State;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public abstract class AgentNode implements NodeAction<State> {

	protected String          	agentName;
	protected String          	systemPrompt;
	protected ToolCallback [] 	tools;
	protected ChatClient      	chatClient;


	@Override
	public Map<String, Object> apply ( State state ) throws Exception {

		String input = state.getCurrentMessage ();

		//Appending previous messages to the context
		StringBuilder inputWithContext = new StringBuilder ();

		if ( state.getPreviousMessages ().size () > 1 ) {

			inputWithContext.append ( "PREVIOUS CONVERSATION:\n" );

			for ( Map<String, String> message : state.getPreviousMessages () ) {

				inputWithContext.append ( message.get ( "role" ) ).append ( ": " ).append ( message.get ( "content" ) ).append ( "\n" );
			}

			inputWithContext.append ( "END OF PREVIOUS CONVERSATION\n\n" );
		}

		//Appending the current input
		inputWithContext.append ( state.getPreviousAgentKey () )
			.append ( ": " )
			.append ( input )
			.append ( "\n" );

		//Invoking the Agent
		String response = chatClient.prompt ( systemPrompt )
			.toolCallbacks ( tools )
			.user ( inputWithContext.toString () )
			.call ()
			.content ();

		System.out.println ( response );

		//Adding the current message and response to the state
		Map<String, String> newMessage = Map.of (
				"role", agentName,
				"content", response
		);

		List<Map<String, String>> previousMessages = state.getPreviousMessages ();
		previousMessages.add ( newMessage );

		return Map.of (
				State.CURRENT_MESSAGE_KEY, response,
				State.PREVIOUS_AGENT_KEY, agentName,
				State.PREVIOUS_MESSAGES_KEY, previousMessages
		);
	}
}