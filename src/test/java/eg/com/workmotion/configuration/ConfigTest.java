package eg.com.workmotion.configuration;

import java.util.UUID;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import eg.com.workmotion.model.EmployeeEvent;
import eg.com.workmotion.model.EmployeeState;

/**
 * @author Ali Tawfiq
 */
@SpringBootTest
public class ConfigTest {
	@Autowired
	StateMachineFactory<EmployeeState, EmployeeEvent> factory;

	@SuppressWarnings("deprecation")
	@Test
	void testStateMachine() {
		StateMachine<EmployeeState, EmployeeEvent> machine = factory.getStateMachine(UUID.randomUUID());
		machine.start();

		System.out.println(machine.getState().toString());
		machine.sendEvent(EmployeeEvent.SHORT_LIST);
		System.out.println(machine.getState().toString());
		machine.sendEvent(EmployeeEvent.ACCEPTED);
		System.out.println(machine.getState().toString());

	}

}
