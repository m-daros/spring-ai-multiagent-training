package mdaros.langgraph4j.spring.ai.multiagent.training.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Person {

	private String personId;
	private String name;
	private String role;
	private String company;
}