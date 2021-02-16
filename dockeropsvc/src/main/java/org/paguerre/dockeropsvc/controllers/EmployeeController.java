package org.paguerre.dockeropsvc.controllers;

import java.util.List;
import java.util.Optional;

import org.paguerre.dockeropsvc.models.Employee;
import org.paguerre.dockeropsvc.repository.EmployeeRepository;
import org.paguerre.dockeropsvc.services.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dockeropsvc/v1")
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private SequenceGeneratorService seqGenService;

	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<?> getEmployeeById(@PathVariable(value = "id") Long employeeId) {
		ResponseEntity<?> rv = ResponseEntity.notFound().build();
		Optional<Employee> opt = employeeRepository.findById(employeeId);
		if (opt.isPresent()) {
			rv = ResponseEntity.ok().body(opt.get());
		}
		return rv;
	}

	@PostMapping("/employees")
	public Employee createEmployee(@Validated @RequestBody Employee employee) {
		employee.setId(seqGenService.generateSequence(Employee.SEQUENCE_NAME));
		return employeeRepository.save(employee);
	}

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable(value = "id") Long employeeId) {
		ResponseEntity<?> rv = ResponseEntity.notFound().build();
		Optional<Employee> opt = employeeRepository.findById(employeeId);
		if (opt.isPresent()) {
			employeeRepository.deleteById(employeeId);
			rv = ResponseEntity.ok().build();
		}
		return rv;
	}
}
