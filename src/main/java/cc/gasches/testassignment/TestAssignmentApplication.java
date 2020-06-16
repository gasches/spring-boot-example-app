package cc.gasches.testassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackageClasses = TestAssignmentApplication.class)
public class TestAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestAssignmentApplication.class, args);
	}
}
