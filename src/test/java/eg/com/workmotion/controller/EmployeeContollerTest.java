package eg.com.workmotion.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import eg.com.workmotion.model.Employee;
import eg.com.workmotion.model.EmployeeEvent;
import eg.com.workmotion.model.EmployeeState;
import eg.com.workmotion.service.IEmployeeService;

/**
 * @author Ali Tawfiq
 */
@SpringBootTest
class EmployeeContollerTest {

	@Autowired
	IEmployeeService employeeService;

	Employee employee;

	@BeforeEach
	void setUpBeforeClass() throws Exception {
		employee = Employee.builder().employeeAge(19).employeeName("Ali").employeeSalary(20000).contractExpiry(null)
				.build();
	}

	@Test
	void testAddEmployee() {
		employee = employeeService.addEmployee(employee);
//		System.out.println(employee.getState());
	}

	@Test
	void testUpdateEmployee() {
		employee = employeeService.addEmployee(employee);
		System.out.println(employee.getState());

		StateMachine<EmployeeState, EmployeeEvent> sm = employeeService.inCheck(employee.getEmployeeId());
		System.out.println(employeeService.getEmployee(employee.getEmployeeId()).get().getState());
		System.out.println(sm.getState().getId().name());

	}

}
