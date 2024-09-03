package de.workshops.bookshelf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:bookshelf")
@ActiveProfiles("prod")
class BookshelfApplicationTests {

	@Value("${server.port:8080}")
	private int port;

	@Test
	void contextLoads() {
	}

	@Test
//	@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'prod'}", loadContext = true)
	@EnabledIf(expression = "#{environment.acceptsProfiles('prod')}", loadContext = true)
	void testProdPort() {
		int expectedPort = 9090;

		assertThat(port).isEqualTo(expectedPort);
	}
}
