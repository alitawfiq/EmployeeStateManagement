package eg.com.workmotion.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import eg.com.workmotion.service.IEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

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
	@Operation(summary = "Get a specific employee by his/her ID")
    @ApiResponse(code = 302, message = "Employee Found")
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
	@Operation(summary = "Get all employees")
    @ApiResponse(code = 302, message = "Employees found")
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
	@Operation(summary = "Add an employee")
    @ApiResponse(code = 201, message = "Employee has been added successfully")
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
	@Operation(summary = "Update an employee from one state to another")
    @ApiResponse(code = 200, message = "The employee state has been updated")
	@PatchMapping("{employeeId}/{employeeState}")
	public Map<String, String> changeEmployeeState(@PathVariable Integer employeeId, @PathVariable String employeeState) {
		return employeeService.changeEmployeeState(employeeId, employeeState);
	}
}