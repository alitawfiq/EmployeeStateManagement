package eg.com.workmotion.configuration;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import eg.com.workmotion.model.EmployeeEvent;
import eg.com.workmotion.model.EmployeeState;

/**
 * @author Ali Tawfiq
 */

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<EmployeeState, EmployeeEvent> {

	private static final Logger log = LoggerFactory.getLogger(StateMachineConfig.class);

	/**
	 * Responsible for transitioning the state machine from one state to another
	 * through passing certain events
	 * 
	 * @param transitions State Machine transition configurer
	 */

	@Override
	public void configure(StateMachineTransitionConfigurer<EmployeeState, EmployeeEvent> transitions) throws Exception {
		transitions.withExternal()
		.source(EmployeeState.ADDED).target(EmployeeState.IN_CHECK).event(EmployeeEvent.SHORT_LIST)
		.and().withExternal()
		.source(EmployeeState.ADDED).target(EmployeeState.APPROVED).event(EmployeeEvent.ACCEPTED)
		.and().withExternal()
		.source(EmployeeState.ADDED).target(EmployeeState.ACTIVE).event(EmployeeEvent.HIRED)
		.and().withExternal()
		.source(EmployeeState.IN_CHECK).target(EmployeeState.APPROVED).event(EmployeeEvent.ACCEPTED)
		.and().withExternal()
		.source(EmployeeState.IN_CHECK).target(EmployeeState.ACTIVE).event(EmployeeEvent.HIRED)
		.and().withExternal()
		.source(EmployeeState.IN_CHECK).target(EmployeeState.ADDED).event(EmployeeEvent.SHORT_LIST)
		.and().withExternal()
		.source(EmployeeState.APPROVED).target(EmployeeState.IN_CHECK).event(EmployeeEvent.SHORT_LIST)
		.and().withExternal()
		.source(EmployeeState.APPROVED).target(EmployeeState.ACTIVE).event(EmployeeEvent.HIRED);
	}
	/**
	 * Responsible for setting the primary state of the Employee when 
	 * added
	 * 
	 * @param states State Machine state configurer 
	*/
	@Override
	public void configure(StateMachineStateConfigurer<EmployeeState, EmployeeEvent> states) throws Exception {
		states.withStates()
		.initial(EmployeeState.ADDED)
		.states(EnumSet.allOf(EmployeeState.class))
		.end(EmployeeState.ACTIVE);
	}

	/**
	 * Responsible for listening to the state machine states and logging the states
	 * to check the which state did the employee go from and to.
	 * 
	 * @param transitions State Machine Configuration configurer
	 */
	@Override
	public void configure(StateMachineConfigurationConfigurer<EmployeeState, EmployeeEvent> config) throws Exception {
		StateMachineListenerAdapter<EmployeeState, EmployeeEvent> adapter = new StateMachineListenerAdapter<EmployeeState, EmployeeEvent>() {
			@Override
			public void stateChanged(State<EmployeeState, EmployeeEvent> from, State<EmployeeState, EmployeeEvent> to) {
				log.info(
						String.format("Employee state changed from %s, to %s", from.getId().name(), to.getId().name()));
			}
		};
		config.withConfiguration().listener(adapter);
	}

}