package com.coe.oom.core.base.util.vali.excel;


public enum ValiHelpEnum {

	NUMBER_TYPE("Number类型", "number"), STRING_TYPE("String类型", "string"),BIGDECIMAL_TYPE("BigDecimal类型","bigDecimal");

	private String typeName;

	private String typeCode;

	private ValiHelpEnum(String typeName, String typeCode) {
		this.typeName = typeName;
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

}
