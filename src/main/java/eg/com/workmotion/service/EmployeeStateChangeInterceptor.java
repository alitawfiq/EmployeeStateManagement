package eg.com.workmotion.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import eg.com.workmotion.model.Employee;
import eg.com.workmotion.model.EmployeeEvent;
import eg.com.workmotion.model.EmployeeState;
import eg.com.workmotion.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Ali Tawfiq
 */
@RequiredArgsConstructor
@Component
public class EmployeeStateChangeInterceptor extends StateMachineInterceptorAdapter<EmployeeState, EmployeeEvent> {

	@Autowired
	private final EmployeeRepository employeeRepository;

	/**
	 * Responsible for intercepting the state machine before a change happens
	 * persisiting the changed state to the database if a message(event) is present
	 * through fetching the employee from the database and change the employee's
	 * state then saving it.
	 * 
	 * @param employeeId The identification of the employee you will fetch
	 * @return Employee object with all it's attributes
	 * @see Employee Class
	 */

	@Override
	public void preStateChange(State<EmployeeState, EmployeeEvent> state, Message<EmployeeEvent> message,
			Transition<EmployeeState, EmployeeEvent> transition,
			StateMachine<EmployeeState, EmployeeEvent> stateMachine,
			StateMachine<EmployeeState, EmployeeEvent> rootStateMachine) {

		Optional.ofNullable(message).ifPresent(msg -> {

			Optional.ofNullable(Integer.class.cast(msg.getHeaders().getOrDefault(EmployeeServiceImpl.EMPLOYEE_ID, -1L)))
					.ifPresent(employeeId -> {
						Employee employee = employeeRepository.getById(employeeId);
						employee.setState(state.getId());
						employeeRepository.save(employee);
					});
		});

	}

}