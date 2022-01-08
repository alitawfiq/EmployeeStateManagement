package eg.com.workmotion.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.statemachine.StateMachine;

import eg.com.workmotion.model.Employee;
import eg.com.workmotion.model.EmployeeEvent;
import eg.com.workmotion.model.EmployeeState;
/**
 * @author  Ali Tawfiq
*/
public interface IEmployeeService {

	Employee addEmployee(Employee employee);

	Optional<Employee> getEmployee(Integer employeeId);

	List<Employee> getAllEmployees();
	
	StateMachine<EmployeeState, EmployeeEvent> inCheck(Integer employeeId);

	StateMachine<EmployeeState, EmployeeEvent> approved(Integer employeeId);

	StateMachine<EmployeeState, EmployeeEvent> active(Integer employeeId);

	Map<String, String> changeEmployeeState(Integer employeeId,String employeeStyate);
}