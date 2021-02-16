package org.paguerre.dockeropsvc.models;

import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "employee")
public class Employee {

	@Transient
	public static final String SEQUENCE_NAME = "users_sequence";

	@Id
	private long id;

	@NonNull
	@Indexed(unique = true)
	private String email;

	@NonNull
	@Indexed(unique = true)
	private String name;

	public Employee() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return id + Optional.ofNullable(email).orElse("no-email").concat(", ")
				.concat(Optional.ofNullable(name).orElse("no-name"));
	}
}
