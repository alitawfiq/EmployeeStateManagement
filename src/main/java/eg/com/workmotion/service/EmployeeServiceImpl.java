package eg.com.workmotion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eg.com.workmotion.model.Employee;
import eg.com.workmotion.model.EmployeeEvent;
import eg.com.workmotion.model.EmployeeState;
import eg.com.workmotion.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Ali Tawfiq
 */
@SuppressWarnings("deprecation")
@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements IEmployeeService {

	public static final String EMPLOYEE_ID = "Employee_Id";

	private final StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;

	private final EmployeeStateChangeInterceptor employeeStateChangeInterceptor;

	@Autowired
	private final EmployeeRepository employeeRepository;

	/**
	 * Responsible for adding an employee.
	 * 
	 * @param employee An Employee reference with all of it's attributes except
	 *                 state, id.
	 * @return Employee object with all it's attributes
	 * @see Employee Class
	 */
	@Override
	public Employee addEmployee(Employee employee) {
		employee.setState(EmployeeState.ADDED);
		return employeeRepository.save(employee);
	}

	/**
	 * Responsible for fetching an employee from the database through the
	 * employeeId.
	 * 
	 * @param employeeId The identification of the employee you will fetch
	 * @return Employee object with all it's attributes
	 * @see Employee Class
	 */
	@Override
	public Optional<Employee> getEmployee(Integer employeeId) {
		return employeeRepository.findById(employeeId);
	}
	/**
	 * Responsible for fetching an employee from the database through the
	 * employeeId.
	 * 
	 * @param employeeId The identification of the employee you will fetch
	 * @return Employee object with all it's attributes
	 * @see Employee Class
	 */
	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	/**
	 * Responsible for changing the employee state to In_Check.
	 * 
	 * @param employeeId The identification of the employee you wish to change
	 * @return sm The statemachine
	 */
	@Transactional
	@Override
	public StateMachine<EmployeeState, EmployeeEvent> inCheck(Integer employeeId) {
		StateMachine<EmployeeState, EmployeeEvent> sm = build(employeeId);

		sendEvent(employeeId, sm, EmployeeEvent.SHORT_LIST);

		Employee employee = employeeRepository.getById(employeeId);
		employee.setState(EmployeeState.IN_CHECK);
		employeeRepository.save(employee);

		return sm;
	}

	/**
	 * Responsible for changing the employee state to Approved.
	 * 
	 * @param employeeId The identification of the employee you wish to change
	 * @return sm The statemachine
	 */
	@Transactional
	@Override
	public StateMachine<EmployeeState, EmployeeEvent> approved(Integer employeeId) {
		StateMachine<EmployeeState, EmployeeEvent> sm = build(employeeId);

		sendEvent(employeeId, sm, EmployeeEvent.ACCEPTED);

		Employee employee = employeeRepository.getById(employeeId);
		employee.setState(EmployeeState.APPROVED);
		employeeRepository.save(employee);

		return sm;
	}

	/**
	 * Responsible for changing the employee state to Active.
	 * 
	 * @param employeeId The identification of the employee you wish to change
	 * @return sm The statemachine
	 */
	@Transactional
	@Override
	public StateMachine<EmployeeState, EmployeeEvent> active(Integer employeeId) {
		StateMachine<EmployeeState, EmployeeEvent> sm = build(employeeId);

		sendEvent(employeeId, sm, EmployeeEvent.HIRED);

		Employee employee = employeeRepository.getById(employeeId);
		employee.setState(EmployeeState.ACTIVE);
		employeeRepository.save(employee);

		return sm;
	}

	/**
	 * Responsible for changing the employee state to In_Check.
	 * 
	 * @param employeeId The identification of the employee you wish to change
	 * @param sm         The statemachine
	 * @param event      The event which will change the employee from one state to
	 *                   another
	 * @return void Returns nothing
	 */
	private void sendEvent(Integer employeeId, StateMachine<EmployeeState, EmployeeEvent> sm, EmployeeEvent event) {
		Message<EmployeeEvent> msg = MessageBuilder.withPayload(event).setHeader(EMPLOYEE_ID, employeeId).build();
		sm.sendEvent(msg);
	}

	/**
	 * Responsible for building the state machine and retrieving tha last state from
	 * the database through the employee id
	 * 
	 * @param employeeId The identification of the employee you will retrieve from
	 *                   the database
	 * @return sm The statemachine
	 */
	private StateMachine<EmployeeState, EmployeeEvent> build(Integer employeeId) {
		Employee employee = employeeRepository.getOne(employeeId);

		StateMachine<EmployeeState, EmployeeEvent> sm = stateMachineFactory
				.getStateMachine(Integer.toString(employee.getEmployeeId()));

		sm.stop();

		sm.getStateMachineAccessor().doWithAllRegions(sma -> {
			sma.addStateMachineInterceptor(employeeStateChangeInterceptor);
			sma.resetStateMachine(new DefaultStateMachineContext<>(employee.getState(), null, null, null));
		});

		sm.start();

		return sm;
	}
}