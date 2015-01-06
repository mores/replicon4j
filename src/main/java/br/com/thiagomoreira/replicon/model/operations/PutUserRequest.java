/**
 * Copyright (C) 2014 Thiago Moreira (tmoreira2020@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.thiagomoreira.replicon.model.operations;

import br.com.thiagomoreira.replicon.model.Department;
import br.com.thiagomoreira.replicon.model.EmployeeType;
import br.com.thiagomoreira.replicon.model.EmploymentDateRange;
import br.com.thiagomoreira.replicon.model.SupervisorAssignmentSchedule;
import br.com.thiagomoreira.replicon.model.User;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("user")
public class PutUserRequest {

	protected User target;
	protected String loginName;
	protected String firstname;
	protected String lastname;
	protected String emailAddress;
	protected String employeeId;
	protected Department department;
	protected SupervisorAssignmentSchedule supervisorAssignmentSchedule;
	protected String workWeekStartDayUri;
	protected EmploymentDateRange employmentDateRange;
	protected EmployeeType employeeType;
	protected String[] customFieldValues;
}
