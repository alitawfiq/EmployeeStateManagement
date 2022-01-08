package eg.com.workmotion.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import eg.com.workmotion.model.Employee;
import eg.com.workmotion.repository.EmployeeRepository;
/**
 * @author  Ali Tawfiq
*/
@SpringBootTest
class EmployeeServiceImplTest {

	@Autowired
	IEmployeeService employeeService;

	@Autowired
	EmployeeRepository employeeRepository;

	Employee employee;

	@BeforeEach
	void setUpBeforeClass() throws Exception {
		employee = Employee.builder().employeeAge(20).employeeName("Ali Tawfiq").contractExpiry(null).build();
	}

	@Transactional
	@Test
	void testInCheck() {
		Employee savedEmployee = employeeService.addEmployee(employee);
		System.out.println(savedEmployee.getState());

		employeeService.inCheck(savedEmployee.getEmployeeId());
		System.out.println(savedEmployee.getState());

		Employee inCheckEmployee = employeeRepository.getById(savedEmployee.getEmployeeId());
		System.out.println(inCheckEmployee.getState());

		employeeService.approved(savedEmployee.getEmployeeId());

		System.out.println(savedEmployee.getState());

	}

}
