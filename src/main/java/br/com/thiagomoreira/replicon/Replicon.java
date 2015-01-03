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
package br.com.thiagomoreira.replicon;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import br.com.thiagomoreira.replicon.model.DateRange;
import br.com.thiagomoreira.replicon.model.Department;
import br.com.thiagomoreira.replicon.model.Program;
import br.com.thiagomoreira.replicon.model.Project;
import br.com.thiagomoreira.replicon.model.ProjectAllocation;
import br.com.thiagomoreira.replicon.model.Resource;
import br.com.thiagomoreira.replicon.model.Response;
import br.com.thiagomoreira.replicon.model.Task;
import br.com.thiagomoreira.replicon.model.TaskAllocation;
import br.com.thiagomoreira.replicon.model.TimeOffAllocation;
import br.com.thiagomoreira.replicon.model.User;
import br.com.thiagomoreira.replicon.model.operations.GetDirectReportsForUserRequest;
import br.com.thiagomoreira.replicon.model.operations.GetProjectDetailsRequest;
import br.com.thiagomoreira.replicon.model.operations.GetResourceAllocationSummaryRequest;
import br.com.thiagomoreira.replicon.model.operations.GetResourceAllocationSummaryResponse;
import br.com.thiagomoreira.replicon.model.operations.GetResourceDetailsRequest;
import br.com.thiagomoreira.replicon.model.operations.GetResourceTaskAllocationDetailsRequest;
import br.com.thiagomoreira.replicon.model.operations.GetResourceTaskAllocationDetailsResponse;
import br.com.thiagomoreira.replicon.model.operations.GetTaskDetailsRequest;
import br.com.thiagomoreira.replicon.model.operations.GetTimeOffDetailsForUserAndDateRangeRequest;
import br.com.thiagomoreira.replicon.model.operations.GetUser2Request;
import br.com.thiagomoreira.replicon.model.operations.PutProjectInfoRequest;
import br.com.thiagomoreira.replicon.util.DateUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Replicon {

	protected ClientHttpRequestFactory clientHttpRequestFactory;
	protected RestTemplate restTemplate;
	protected ObjectMapper objectMapper = new ObjectMapper();
	protected String company;

	public Replicon(final String company, final String username,
			final String password) {

		this.company = company.toLowerCase();
		this.clientHttpRequestFactory = new SimpleClientHttpRequestFactory() {
			@Override
			protected void prepareConnection(HttpURLConnection connection,
					String httpMethod) throws IOException {
				super.prepareConnection(connection, httpMethod);

				String authorisation = company + "\\" + username + ":"
						+ password;
				byte[] encodedAuthorisation = Base64.encodeBase64(authorisation
						.getBytes());
				connection.setRequestProperty("Authorization", "Basic "
						+ new String(encodedAuthorisation));
			}
		};

		this.restTemplate = new RestTemplate(clientHttpRequestFactory);
	}

	public Department[] getEnabledDepartments() {
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<Department[]>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);

		response = restTemplate.exchange(getBaseServiceUrl()
				+ "/DepartmentService1.svc/GetEnabledDepartments",
				HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<Response<Department[]>>() {
				});

		return response.getBody().getD();
	}

	public Program[] getPrograms() {
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<Program[]>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);

		response = restTemplate.exchange(getBaseServiceUrl()
				+ "/ProgramService1.svc/GetAllPrograms", HttpMethod.POST,
				httpEntity,
				new ParameterizedTypeReference<Response<Program[]>>() {
				});

		return response.getBody().getD();
	}

	public Project getProject(String projectUri) throws IOException {

		GetProjectDetailsRequest request = new GetProjectDetailsRequest();

		request.setProjectUri(projectUri);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<Project>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(
				objectMapper.writeValueAsString(request), headers);

		response = restTemplate.exchange(getBaseServiceUrl()
				+ "/ProjectService1.svc/GetProjectDetails", HttpMethod.POST,
				httpEntity,
				new ParameterizedTypeReference<Response<Project>>() {
				});

		return response.getBody().getD();
	}

	public Resource putProjectInfo(Project project) throws IOException {
		PutProjectInfoRequest request = new PutProjectInfoRequest();
		request.setTarget(project);
		request.setProjectInfo(project);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<Resource>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(
				objectMapper.writeValueAsString(request), headers);

		response = restTemplate.exchange(getBaseServiceUrl()
				+ "/ProjectService1.svc/PutProjectInfo", HttpMethod.POST,
				httpEntity,
				new ParameterizedTypeReference<Response<Resource>>() {
				});

		return response.getBody().getD();
	}

	public Project[] getProjects() throws IOException {
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<Project[]>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);

		response = restTemplate.exchange(getBaseServiceUrl()
				+ "/ProjectService1.svc/GetAllProjects", HttpMethod.POST,
				httpEntity,
				new ParameterizedTypeReference<Response<Project[]>>() {
				});

		return response.getBody().getD();
	}

	public ProjectAllocation[] getProjectAllocations(Date startDate,
			Date endDate, String resourceUri) throws IOException {

		GetResourceAllocationSummaryRequest request = new GetResourceAllocationSummaryRequest();

		DateRange dateRange = new DateRange();

		dateRange.setStartDate(DateUtil.translateDate(startDate));
		dateRange.setEndDate(DateUtil.translateDate(endDate));

		request.setResourceUri(resourceUri);
		request.setDateRange(dateRange);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<GetResourceAllocationSummaryResponse>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(
				objectMapper.writeValueAsString(request), headers);

		response = restTemplate
				.exchange(
						getBaseServiceUrl()
								+ "/ResourceService1.svc/GetResourceAllocationSummary",
						HttpMethod.POST,
						httpEntity,
						new ParameterizedTypeReference<Response<GetResourceAllocationSummaryResponse>>() {
						});

		return response.getBody().getD().getProjectsAllocatedTo();
	}

	public Resource getResource(String resourceUri) throws IOException {
		GetResourceDetailsRequest request = new GetResourceDetailsRequest();

		request.setResourceUri(resourceUri);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<Resource>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(
				objectMapper.writeValueAsString(request), headers);

		response = restTemplate.exchange(getBaseServiceUrl()
				+ "/ResourceService1.svc/GetResourceDetails", HttpMethod.POST,
				httpEntity,
				new ParameterizedTypeReference<Response<Resource>>() {
				});

		return response.getBody().getD();
	}

	public Task getTask(String taskUri) throws IOException {
		GetTaskDetailsRequest request = new GetTaskDetailsRequest();

		request.setTaskUri(taskUri);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<Task>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(
				objectMapper.writeValueAsString(request), headers);

		response = restTemplate.exchange(getBaseServiceUrl()
				+ "/TaskService1.svc/GetTaskDetails", HttpMethod.POST,
				httpEntity, new ParameterizedTypeReference<Response<Task>>() {
				});

		return response.getBody().getD();
	}

	public TaskAllocation[] getTaskAllocations(String projectUri,
			String resourceUri) throws IOException {
		GetResourceTaskAllocationDetailsRequest request = new GetResourceTaskAllocationDetailsRequest();

		request.setProjectUri(projectUri);
		request.setResourceUri(resourceUri);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<GetResourceTaskAllocationDetailsResponse>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(
				objectMapper.writeValueAsString(request), headers);

		response = restTemplate
				.exchange(
						getBaseServiceUrl()
								+ "/ResourceService1.svc/GetResourceTaskAllocationDetails",
						HttpMethod.POST,
						httpEntity,
						new ParameterizedTypeReference<Response<GetResourceTaskAllocationDetailsResponse>>() {
						});

		return response.getBody().getD().getEntries();
	}

	public TimeOffAllocation[] getTimeOff(String userUri, Date startDate,
			Date endDate) throws IOException {

		GetTimeOffDetailsForUserAndDateRangeRequest request = new GetTimeOffDetailsForUserAndDateRangeRequest();

		DateRange dateRange = new DateRange();

		dateRange.setStartDate(DateUtil.translateDate(startDate));
		dateRange.setEndDate(DateUtil.translateDate(endDate));

		request.setUserUri(userUri);
		request.setDateRange(dateRange);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<TimeOffAllocation[]>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(
				objectMapper.writeValueAsString(request), headers);

		response = restTemplate
				.exchange(
						getBaseServiceUrl()
								+ "/TimeOffService1.svc/GetTimeOffDetailsForUserAndDateRange",
						HttpMethod.POST,
						httpEntity,
						new ParameterizedTypeReference<Response<TimeOffAllocation[]>>() {
						});

		return response.getBody().getD();
	}

	public User getUserByLoginName(String loginName) throws IOException {
		GetUser2Request request = new GetUser2Request();

		request.setLoginName(loginName);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<User>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(
				objectMapper.writeValueAsString(request), headers);

		response = restTemplate.exchange(getBaseServiceUrl()
				+ "/UserService1.svc/GetUser2", HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<Response<User>>() {
				});

		return response.getBody().getD();
	}

	public User[] getUsers() {
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<User[]>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);

		response = restTemplate.exchange(getBaseServiceUrl()
				+ "/UserService1.svc/GetAllUsers", HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<Response<User[]>>() {
				});

		return response.getBody().getD();
	}

	public User[] getUsersBySupervisor(String userUri) throws IOException {
		GetDirectReportsForUserRequest request = new GetDirectReportsForUserRequest();

		request.setUserUri(userUri);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Response<User[]>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(
				objectMapper.writeValueAsString(request), headers);

		response = restTemplate.exchange(getBaseServiceUrl()
				+ "/UserService1.svc/GetDirectReportsForUser", HttpMethod.POST,
				httpEntity, new ParameterizedTypeReference<Response<User[]>>() {
				});

		return response.getBody().getD();
	}

	protected String getBaseServiceUrl() {
		return "https://na2.replicon.com/" + company + "/services";
	}
}
