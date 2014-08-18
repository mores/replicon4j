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
import br.com.thiagomoreira.replicon.model.Project;
import br.com.thiagomoreira.replicon.model.ProjectAllocation;
import br.com.thiagomoreira.replicon.model.Resource;
import br.com.thiagomoreira.replicon.model.Response;
import br.com.thiagomoreira.replicon.model.TaskAllocation;
import br.com.thiagomoreira.replicon.model.User;
import br.com.thiagomoreira.replicon.model.operations.GetProjectDetailsRequest;
import br.com.thiagomoreira.replicon.model.operations.GetResourceAllocationSummaryRequest;
import br.com.thiagomoreira.replicon.model.operations.GetResourceAllocationSummaryResponse;
import br.com.thiagomoreira.replicon.model.operations.GetResourceDetailsRequest;
import br.com.thiagomoreira.replicon.model.operations.GetResourceTaskAllocationDetailsRequest;
import br.com.thiagomoreira.replicon.model.operations.GetResourceTaskAllocationDetailsResponse;
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
	}

	public Project getProject(String projectUri) throws IOException {

		GetProjectDetailsRequest request = new GetProjectDetailsRequest();

		request.setProjectUri(projectUri);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

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

		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

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

		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

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

	public TaskAllocation[] getTaskAllocations(String projectUri,
			String resourceUri) throws IOException {
		GetResourceTaskAllocationDetailsRequest request = new GetResourceTaskAllocationDetailsRequest();

		request.setProjectUri(projectUri);
		request.setResourceUri(resourceUri);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

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

	public User[] getUsers() {
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

		ResponseEntity<Response<User[]>> response = null;
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);

		response = restTemplate.exchange(getBaseServiceUrl()
				+ "/UserService1.svc/GetAllUsers", HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<Response<User[]>>() {
				});

		return response.getBody().getD();
	}

	protected String getBaseServiceUrl() {
		return "https://na2.replicon.com/" + company + "/services";
	}
}
