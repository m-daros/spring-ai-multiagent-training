package mdaros.langgraph4j.spring.ai.multiagent.training.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UpskillerTools {

	@Tool ( description = "Get required skills for a specific opportunity" )
	public List<String> getSkillsRequiredForOpportunity ( String opportunity ) throws IOException {

		// Fake data simulation (using IDs as keys)
		List<String> requiredSkills = new ArrayList<> ();
		requiredSkills.add ( "SQL" );
		requiredSkills.add ( "Docker" );
		requiredSkills.add ( "Python" );
		requiredSkills.add ( "Java" );

		return requiredSkills;
	}

	@Tool ( description = "Get skills of a person (for comparison with required skills)" )
	public List<String> getSkillsOfPerson ( String name ) throws IOException {

		// Fake data simulation
		List<String> skills = new ArrayList<> ();
		skills.add ( "Java" );
		skills.add ( "Spring Boot" );
		skills.add ( "React" );
		skills.add ( "SQL" );
		skills.add ( "Docker" );
		skills.add ( "Microservices" );

		return skills;
	}
}