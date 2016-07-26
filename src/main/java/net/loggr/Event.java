package net.loggr;

import java.util.ArrayList;

public class Event {

    public String getText() {
		return _text;
	}

	public void setText(String _text) {
		this._text = _text;
	}

	public String getLink() {
		return _link;
	}

	public void setLink(String _link) {
		this._link = _link;
	}

	public String getSource() {
		return _source;
	}

	public void setSource(String _source) {
		this._source = _source;
	}

	public ArrayList<String> getTags() {
		return _tags;
	}

	public void setTags(ArrayList<String> _tags) {
		this._tags = _tags;
	}

	public String getData() {
		return _data;
	}

	public void setData(String _data) {
		this._data = _data;
	}

	public Double getValue() {
		return _value;
	}

	public void setValue(Double _value) {
		this._value = _value;
	}

	public String getGeo() {
		return _geo;
	}

	public void setGeo(String _geo) {
		this._geo = _geo;
	}

	public DataType getDataType() {
		return _dataType;
	}

	public void setDataType(DataType _dataType) {
		this._dataType = _dataType;
	}

	public String getUser() {
		return _user;
	}

	public void setUser(String _user) {
		this._user = _user;
	}


	private String _text = "";
	private String _link = "";
	private String _source = "";
	private ArrayList<String> _tags = new ArrayList<String>();
	private String _data = "";
	private Double _value;
	private String _geo;
	private DataType _dataType = DataType.plaintext;
	private String _user;


}
