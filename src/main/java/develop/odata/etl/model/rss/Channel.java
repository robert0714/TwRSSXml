package develop.odata.etl.model.rss;
 

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
 
 

@JacksonXmlRootElement(localName = "channel")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel {
	@JacksonXmlProperty(localName = "title")
	protected String title;
	
	@JacksonXmlProperty(localName = "link")
	protected String link;
	
	@JacksonXmlProperty(localName = "description")
	protected String description;
	
	@JacksonXmlProperty(localName = "language")
	protected String language;
	
	@JacksonXmlProperty(localName = "copyright")
	protected String copyright;
	
	@JacksonXmlProperty(localName = "managingEditor")
	protected String managingEditor;
	
	@JacksonXmlProperty(localName = "webMaster")
	protected String webMaster;
	
	@JacksonXmlProperty(localName = "pubDate")
	protected String pubDate;
	
	@JacksonXmlProperty(localName = "lastBuildDate")
	protected String lastBuildDate; 
	
	@JacksonXmlProperty(localName = "generator")
	protected String generator;
	
	@JacksonXmlProperty(localName = "docs")
	protected String docs;  
	
	@JacksonXmlProperty(localName = "rating")
	protected String rating; 
	
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    private Item[] items;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getManagingEditor() {
		return managingEditor;
	}

	public void setManagingEditor(String managingEditor) {
		this.managingEditor = managingEditor;
	}

	public String getWebMaster() {
		return webMaster;
	}

	public void setWebMaster(String webMaster) {
		this.webMaster = webMaster;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getLastBuildDate() {
		return lastBuildDate;
	}

	public void setLastBuildDate(String lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public String getDocs() {
		return docs;
	}

	public void setDocs(String docs) {
		this.docs = docs;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public Item[] getItems() {
		return items;
	}

	public void setItems(Item[] items) {
		this.items = items;
	}
    
    
}
