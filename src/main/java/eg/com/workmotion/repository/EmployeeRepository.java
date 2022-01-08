package eg.com.workmotion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eg.com.workmotion.model.Employee;
/**
 * @author  Ali Tawfiq
*/
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}