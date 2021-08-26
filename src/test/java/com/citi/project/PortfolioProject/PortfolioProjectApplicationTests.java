package com.citi.project.PortfolioProject;

import com.citi.project.PortfolioProject.entities.Accounts;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
class PortfolioProjectApplicationTests {

	private RestTemplate template = new RestTemplate();

	@Disabled
	@Test
	public void testFindAll() {
		List<Accounts> acs = template.getForObject("http://localhost:8080", List.class);
		assertThat(acs.size(),  greaterThan(1));
	}

	@Disabled
	@Test
	public void testCdById() {
		Accounts acc = template.getForObject
				("http://localhost:8080/1", Accounts.class);
		assertThat(acc.getName(), equalTo("RBC"));
	}

	@Disabled
	@Test
	void contextLoads() {
	}

}
