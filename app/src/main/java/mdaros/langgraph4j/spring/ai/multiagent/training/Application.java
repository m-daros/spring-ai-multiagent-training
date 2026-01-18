package mdaros.langgraph4j.spring.ai.multiagent.training;

import lombok.extern.slf4j.Slf4j;
import mdaros.langgraph4j.spring.ai.multiagent.training.graph.MultiAgentGraph;
import mdaros.langgraph4j.spring.ai.multiagent.training.model.State;
import org.bsc.langgraph4j.StateGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class Application {

	@Autowired
	private MultiAgentGraph agentGraphService;


	public static void main ( String [] args ) {

		SpringApplication.run ( Application.class, args );
	}


	@Bean
	public CommandLineRunner chatbot () {

		return args -> {

			StateGraph<State> stateGraph = agentGraphService.defineGraph ();
			var compiledGraph = stateGraph.compile ();

			List<Map<String, String>> messages = new ArrayList<> ();
			String humanInputMessage = "Get the best job opportunities for XY, he is a software engineer with 5 years of experience in New York";
			messages.add ( Map.of ( "HUMAN INPUT", "" ) );

			log.info ( "Starting asking this to the multiagent App: {}", humanInputMessage );

			compiledGraph.invoke ( Map.of (
				State.CURRENT_MESSAGE_KEY, humanInputMessage,
				State.PREVIOUS_AGENT_KEY, "HUMAN INPUT",
				State.PREVIOUS_MESSAGES_KEY, messages
			) );
		};
	}
}