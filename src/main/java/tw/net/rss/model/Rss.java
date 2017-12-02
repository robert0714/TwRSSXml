package tw.net.rss.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "rss")
@JsonInclude(JsonInclude.Include.NON_EMPTY) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rss {
	@JacksonXmlProperty(localName = "version",isAttribute=true)
	private String version;
	@JacksonXmlProperty(localName = "channel" )
	private Channel channel;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	
}
