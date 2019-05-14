package com.coe.oom.core.base.util.vali.excel;


public class ValiHelp {

	private Integer index;

	private ValiHelpEnum type;

	private String msg;

	private Boolean isAllowNull;

	private String regular;
	
	private Integer minLength=-1;
	
	private Integer maxLength=-1;

	public ValiHelpEnum getType() {
		return type;
	}

	public ValiHelp setType(ValiHelpEnum type) {
		this.type = type;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public ValiHelp setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Integer getIndex() {
		return index;
	}

	public ValiHelp setIndex(Integer index) {
		this.index = index;
		return this;
	}

	public Boolean getIsAllowNull() {
		return isAllowNull;
	}

	public ValiHelp setIsAllowNull(Boolean isAllowNull) {
		this.isAllowNull = isAllowNull;
		return this;
	}

	public ValiHelp(Integer index, ValiHelpEnum type, String msg, Boolean isAllowNull) {
		super();
		this.index = index;
		this.type = type;
		this.msg = msg;
		this.isAllowNull = isAllowNull;
	}
	
	public ValiHelp(Integer index,String msg, Boolean isAllowNull) {
		super();
		this.index = index;
		this.msg = msg;
		this.isAllowNull = isAllowNull;
	}
	
	public ValiHelp(Integer index,String msg) {
		super();
		this.index = index;
		this.msg = msg;
	}

	public String getRegular() {
		return regular;
	}

	public ValiHelp setRegular(String regular) {
		this.regular = regular;
		return this;
	}

	public ValiHelp(Integer index, ValiHelpEnum type, String msg, Boolean isAllowNull, String regular) {
		super();
		this.index = index;
		this.type = type;
		this.msg = msg;
		this.isAllowNull = isAllowNull;
		this.regular = regular;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public ValiHelp setMinLength(Integer minLength) {
		this.minLength = minLength;
		return this;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public ValiHelp setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
		return this;
	}
	

}
