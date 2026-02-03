package mdaros.langgraph4j.spring.ai.multiagent.training.tools;

import lombok.extern.slf4j.Slf4j;
import mdaros.langgraph4j.spring.ai.multiagent.training.model.Person;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ConnectionFinderTools {

	@Tool ( description = "Find people in a company" )
	public List<Person> findPeopleInCompany ( String companyName ) throws IOException {

		log.info ( "findPeopleInCompany ( {} )", companyName );

		// Fake data simulation
		List<Person> people = new ArrayList<> ();
		people.add ( new Person ( "P001", "Alice Johnson", "Senior Engineer", companyName ) );
		people.add ( new Person ( "P002", "Bob Smith", "Tech Lead", companyName ) );
		people.add ( new Person ( "P003", "Carol White", "Engineering Manager", companyName ) );
		people.add ( new Person ( "P004", "David Brown", "Software Architect", companyName ) );

		return people;
	}
}