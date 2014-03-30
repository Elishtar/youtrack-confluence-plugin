package youtrack.fields.values;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by egor.malyshev on 30.03.2014.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AttachmentFieldValue extends IssueFieldValue {

	@XmlAttribute(name = "id")
	private String id;
	@XmlAttribute(name = "url")
	private String url;

	public AttachmentFieldValue() {
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}