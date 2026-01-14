package cl.getnet.jsontoapi.config;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalSettings {
  private String baseUrl;
  @JsonAlias({ "ChannelData", "channelData" })
  @JsonProperty("ChannelData")
  private List<ChannelInfo> channelData;

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String v) {
    this.baseUrl = v;
  }

  public List<ChannelInfo> getChannelData() {
    return channelData;
  }

  public void setChannelData(List<ChannelInfo> v) {
    this.channelData = v;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ChannelInfo {
    private Integer channel;
    private String folder;
    private String template;
    private String version;

    public Integer getChannel() {
      return channel;
    }

    public void setChannel(Integer v) {
      this.channel = v;
    }

    public String getFolder() {
      return folder;
    }

    public void setFolder(String v) {
      this.folder = v;
    }

    public String getTemplate() {
      return template;
    }

    public void setTemplate(String v) {
      this.template = v;
    }

    public String getVersion() {
      return version;
    }

    public void setVersion(String v) {
      this.version = v;
    }
  }
}
