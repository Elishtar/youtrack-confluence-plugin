package youtrack.fields;

import youtrack.fields.values.AttachmentFieldValue;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by egor.malyshev on 28.03.2014.
 */
@XmlRootElement
public class AttachmentField extends IssueField {

	@XmlElement(name = "value")
	private AttachmentFieldValue value;

	public AttachmentField() {
	}

	@Override
	public AttachmentFieldValue getValue() {
		return value;
	}

}
