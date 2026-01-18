package mdaros.langgraph4j.spring.ai.curriculamcpserver.config;

import mdaros.langgraph4j.spring.ai.curriculamcpserver.services.CurriculaMcpService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public ToolCallbackProvider connectionTools ( CurriculaMcpService curriculaMcpService ) {

		return MethodToolCallbackProvider.builder ().toolObjects ( curriculaMcpService ).build ();
	}
}