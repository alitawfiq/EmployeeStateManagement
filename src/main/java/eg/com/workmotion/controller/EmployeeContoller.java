package eg.com.workmotion.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eg.com.workmotion.model.Employee;
import eg.com.workmotion.model.EmployeeEvent;
import eg.com.workmotion.model.EmployeeState;
import eg.com.workmotion.service.IEmployeeService;
import io.swagger.annotations.Api;

/**
 * @author Ali Tawfiq
 */

@RestController
@Validated
@RequestMapping("/employees/")
@Api(description = "Employee endpoints", tags = { "Employee" })
public class EmployeeContoller {
	@Autowired
	private IEmployeeService employeeService;

	/**
	 * Responsible for fetching an employee.
	 * 
	 * @param employeeId The employee identification you wish to change
	 * @return Employee object with all it's attributes
	 * @see Employee Class
	 */
	@GetMapping("{employeeId}")
	public ResponseEntity<Employee> getEmployee(@PathVariable Integer employeeId) {
		return ResponseEntity.status(HttpStatus.FOUND)
				.body(employeeService.getEmployee(employeeId)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
								"Please provide a correct employee identification or state transition.")));
	}

	/**
	 * Responsible for fetching all employees.
	 * 
	 * @return List of all Employee objects with all it's attributes
	 * @see Employee Class
	 */
	@GetMapping
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employees = employeeService.getAllEmployees();
		if (employees.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		else
			return ResponseEntity.status(HttpStatus.FOUND).body(employees);
	}

	/**
	 * Responsible for adding an employee.
	 * 
	 * @param employee An Employee reference with all of it's attributes except
	 *                 state, id.
	 * @return Employee object with all it's attributes
	 * @see Employee Class
	 */
	@PostMapping
	public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee) {
		return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.addEmployee(employee));
	}

	/**
	 * Responsible for changing the employee state.
	 * 
	 * @param employeeId    The employee identification you wish to change
	 * @param employeeState The state to which the employee will change to
	 * @return resultMap with the success or error
	 */
	@PatchMapping("{employeeId}/{employeeState}")
	public Map<String, String> updateEmployee(@PathVariable Integer employeeId, @PathVariable String employeeState) {
		Optional<Employee> employee = employeeService.getEmployee(employeeId);
		if (employeeId < 0 || employee.isEmpty() || employeeState == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Please provide a correct employee identification or state transition.");
		}
		if(employee.get().getState().toString().equalsIgnoreCase(employeeState))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Employee is already in state "+employeeState);
		}

		Map<String, String> resultMap = new HashMap<String, String>();
		StateMachine<EmployeeState, EmployeeEvent> sm;

		String employeeName = employee.get().getEmployeeName();
		String employeeStateDB = employee.get().getState().name();
		if(employeeStateDB.equalsIgnoreCase("APPROVED") && employeeState.equalsIgnoreCase(employeeStateDB))
		{
			
		}
		if (employeeState.equalsIgnoreCase(String.valueOf(EmployeeState.IN_CHECK))) {
			sm = employeeService.inCheck(employeeId);
			resultMap.put("success",
					String.format("Employee (%s) is now in state: %s.", employeeName, sm.getState().getId().name()));
		} else if (employeeState.equalsIgnoreCase(String.valueOf(EmployeeState.APPROVED))) {
			sm = employeeService.approved(employeeId);
			resultMap.put("success",
					String.format("Employee (%s) is now in state: %s.", employeeName, sm.getState().getId().name()));
		} else if (employeeState.equalsIgnoreCase(String.valueOf(EmployeeState.ACTIVE))) {
			sm = employeeService.active(employeeId);
			resultMap.put("success",
					String.format("Employee (%s) is now in state: %s.", employeeName, sm.getState().getId().name()));
		} else
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Please provide a proper state transition, like " + "IN_CHECK or " + "APPROVED or " + "ACTIVE.");
		return resultMap;
	}
}