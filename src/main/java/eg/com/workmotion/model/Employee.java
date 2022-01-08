package eg.com.workmotion.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  Ali Tawfiq
*/

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Employee {
	@Id
	@GeneratedValue
	private Integer employeeId;
	
	@NotNull(message = "Please provide an employee name")
	private String employeeName;
	
	@NotNull(message = "Please provide an employee age")
	@Positive(message = "Age can't be a negative number")
	@Min(value = 18, message = "Please provide a legal work age")
	private Integer employeeAge;
	
	@NotNull(message = "Please provide an employee salary")
	@Positive(message = "Salary can't be a negative number")
	private Integer employeeSalary;

	@JsonFormat(pattern = "dd-MM-yyyy",shape = JsonFormat.Shape.STRING)
	private Date contractExpiry;

	@Enumerated(EnumType.STRING)
	private EmployeeState state;
}