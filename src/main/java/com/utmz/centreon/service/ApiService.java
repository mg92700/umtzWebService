package com.utmz.centreon.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.utmz.centreon.dto.CentreonDto;
import com.utmz.centreon.dto.CentreonServiceDto;
import com.utmz.centreon.model.User;
import com.utmz.centreon.modelApi.centreon.CentreonHost;
import com.utmz.centreon.modelApi.centreon.CentreonService;
import com.utmz.centreon.modelApi.centreon.CentreonToken;

@Transactional
@Service
public class ApiService {

	public List<CentreonDto> CentreonApi(User user) {
		List<CentreonDto> centreonDto = new ArrayList<>();
		try {
			HttpResponse<String> response = Unirest.post("http://"+user.getIpCentreon()+"/centreon/api/index.php?action=authenticate")
					  .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
					  .header("Cache-Control", "no-cache")
					  .body("------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"username\"\r\n\r\n"+user.getLoginCentreon()+"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"password\"\r\n\r\n"+user.getPasswordCentreon()+"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--")
					  .asString();
			ObjectMapper m = new ObjectMapper();
			CentreonToken token = null;
			try {
				token = m.readValue(response.getBody(), CentreonToken.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			HttpResponse<String> response2 = Unirest
					.get("http://"+user.getIpCentreon()+"/centreon/api/index.php?object=centreon_realtime_hosts&action=list")
					.header("content-type", "application/json").header("centreon-auth-token", token.getAuthToken())
					.header("cache-control", "no-cache").asString();
			ObjectMapper m2 = new ObjectMapper();
			CentreonHost[] hosts = null;
			try {
				hosts = m2.readValue(response2.getBody(), CentreonHost[].class);
				for (int i = 0; i < hosts.length; i++) {
					centreonDto.add(new CentreonDto(hosts[i].getId(), hosts[i].getName(), hosts[i].getAddress(), 1,
							0, 0, 0, 0));
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			HttpResponse<String> response1 = Unirest
					.get("http://"+user.getIpCentreon()+"/centreon/api/index.php?object=centreon_realtime_services&action=list")
					.header("content-type", "application/json").header("centreon-auth-token", token.getAuthToken())
					.header("cache-control", "no-cache").asString();
			ObjectMapper m1 = new ObjectMapper();
			CentreonService[] services = null;
			try {
				services = m1.readValue(response1.getBody(), CentreonService[].class);

				for (int i = 0; i < services.length; i++) {
					for (int j = 0; j < centreonDto.size(); j++) {
						if (centreonDto.get(j).getId() == services[i].getHost_id()) {
							switch (services[i].getState()) {
							case 0:
								centreonDto.get(j).setNbOk(centreonDto.get(j).getNbOk() + 1);
								break;
							case 1:
								centreonDto.get(j).setNbWarning(centreonDto.get(j).getNbWarning() + 1);
								break;
							case 2:
								centreonDto.get(j).setNbCritical(centreonDto.get(j).getNbCritical() + 1);
								break;
							default:
								centreonDto.get(j).setNbUnknown(centreonDto.get(j).getNbUnknown() + 1);
							}
						}
					}
				}

				for (int i = 0; i < centreonDto.size(); i++) {
					if (centreonDto.get(i).getNbUnknown() != 0) {
						centreonDto.get(i).setStatusHost(4);
					} else if (centreonDto.get(i).getNbCritical() != 0) {
						centreonDto.get(i).setStatusHost(3);
					} else if (centreonDto.get(i).getNbWarning() != 0) {
						centreonDto.get(i).setStatusHost(2);
					} else {
						centreonDto.get(i).setStatusHost(1);
					}
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return centreonDto;

	}
	
	public List<CentreonServiceDto> ServiceCentreon(User user, int idHost){
		List<CentreonServiceDto> serviceList = new ArrayList<>();
		CentreonToken token = null;
			HttpResponse<String> response;
			try {
				response = Unirest.post("http://"+user.getIpCentreon()+"/centreon/api/index.php?action=authenticate")
						  .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
						  .header("Cache-Control", "no-cache")
						  .body("------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"username\"\r\n\r\n"+user.getLoginCentreon()+"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"password\"\r\n\r\n"+user.getPasswordCentreon()+"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--")
						  .asString();
		
			ObjectMapper m = new ObjectMapper();
			
			try {
				token = m.readValue(response.getBody(), CentreonToken.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			} catch (UnirestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		HttpResponse<String> response1;
		try {
			response1 = Unirest
					.get("http://"+user.getIpCentreon()+"/centreon/api/index.php?object=centreon_realtime_services&action=list")
					.header("content-type", "application/json").header("centreon-auth-token", token.getAuthToken())
					.header("cache-control", "no-cache").asString();
		
		ObjectMapper m1 = new ObjectMapper();
		CentreonService[] services = null;
		try {
			services = m1.readValue(response1.getBody(), CentreonService[].class);
			for(int i=0; i< services.length; i++) {
				if(services[i].getHost_id()== idHost) {
					serviceList.add(new CentreonServiceDto(services[i].getHost_id(), services[i].getDescription(), services[i].getService_id(), services[i].getState(), services[i].getOutput(), services[i].getLast_check(), services[i].getLast_state_change()));
				}
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		} catch (UnirestException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return serviceList;
		
	}
	
	public boolean checkCentreonExist(User user) {
		boolean exist = false;
		try {
			HttpResponse<String> response = Unirest.post("http://"+user.getIpCentreon()+"/centreon/api/index.php?action=authenticate")
					  .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
					  .header("Cache-Control", "no-cache")
					  .body("------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"username\"\r\n\r\n"+user.getLoginCentreon()+"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"password\"\r\n\r\n"+user.getPasswordCentreon()+"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--")
					  .asString();
			ObjectMapper m = new ObjectMapper();
			CentreonToken token = null;
			
			token = m.readValue(response.getBody(), CentreonToken.class);
			if(token!=null)  {
				exist = true;
			}
			
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
		return exist;
		
	}

	
}