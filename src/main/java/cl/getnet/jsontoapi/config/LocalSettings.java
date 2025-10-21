package cl.getnet.jsontoapi.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalSettings {
  private String baseUrl;
  private Auth auth;
  @JsonAlias({"ChannelData","channelData"})
  @JsonProperty("ChannelData")
  private List<ChannelInfo> channelData;

  public String getBaseUrl() { return baseUrl; }
  public void setBaseUrl(String v) { this.baseUrl = v; }
  public Auth getAuth() { return auth; }
  public void setAuth(Auth v) { this.auth = v; }
  public List<ChannelInfo> getChannelData() { return channelData; }
  public void setChannelData(List<ChannelInfo> v) { this.channelData = v; }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Auth {
    private String username;
    private String password;
    public String getUsername() { return username; }
    public void setUsername(String v) { this.username = v; }
    public String getPassword() { return password; }
    public void setPassword(String v) { this.password = v; }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ChannelInfo {
    private Integer channel;
    private String folder;
    private String template;
    private String version;
    public Integer getChannel() { return channel; }
    public void setChannel(Integer v) { this.channel = v; }
    public String getFolder() { return folder; }
    public void setFolder(String v) { this.folder = v; }
    public String getTemplate() { return template; }
    public void setTemplate(String v) { this.template = v; }
    public String getVersion() { return version; }
    public void setVersion(String v) { this.version = v; }
  }
}
