package com.cc.purecloud.client;

import com.http.client.PureCloudOauthSimpleClient;
import com.http.client.SimpleClientException;
import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.Configuration;

public abstract class AbstractClient {
  
  //private String name = null;
  private String clientId = null;
  private String clientSecret = null;
  private String accessToken = null;
  
  private ApiClient apiClient = null;
  
  public AbstractClient(String clientId, String clientSecret) {
    //this.name = name;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.initClient();
  }
  
  public String getClientId() {
    return clientId;
  }
  
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }
  
  public String getClientSecret() {
    return clientSecret;
  }
  
  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
  
  public String getAccessToken() {
    return accessToken;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public void initClient() {
    
    if (this.accessToken == null) {
      this.retrieveAccessToken();
      // 28.0.0
      apiClient = ApiClient.Builder.standard()
          .withAccessToken(this.accessToken)
          .withBasePath("https://api.mypurecloud.ie")
          .build();
      Configuration.setDefaultApiClient(apiClient);
    }
    
    //0.58
    //ApiClient defaultClient = Configuration.getDefaultApiClient();
    //Configuration.getDefaultApiClient().setAccessToken(accessToken);
    //Configuration.getDefaultApiClient().setBasePath("https://api.mypurecloud.ie");
  }
  
  public void retrieveAccessToken() {
    PureCloudOauthSimpleClient client = new PureCloudOauthSimpleClient();
    //String clientId = "0ed53b7f-df14-4fc4-afd0-f80e39ca9532";
    //String clientSecret = "e3NPp8pxQ5VHnS0ZbyAAxj3fiBcZiPe0heVNXPjjawo";
    
    try {
      this.accessToken = client.getAccessTokenFromClientCredentialsGrant(clientId, clientSecret);
      
    } catch(SimpleClientException e) {
      System.out.println("AAA");
      e.printStackTrace();
    }
  }
}
