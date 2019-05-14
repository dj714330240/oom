package com.coe.oom.core.base.entity;

/**
 * 
 * 操作类型
 * @author lqg
 *
 */
public class OperationType {

	//来自于客户
	private boolean forCustomer;
	
	//來自於商家
	private boolean forBusiness;
	
	//来自于下游系统
	private boolean forDownstream;
	
	//来自于用户
	private boolean forUser;

	public boolean isForCustomer() {
		return forCustomer;
	}

	public void setForCustomer(boolean forCustomer) {
		this.forCustomer = forCustomer;
	}

	public boolean isForDownstream() {
		return forDownstream;
	}

	public void setForDownstream(boolean forDownstream) {
		this.forDownstream = forDownstream;
	}
	 

	public boolean isForUser() {
		return forUser;
	}

	public void setForUser(boolean forUser) {
		this.forUser = forUser;
	}
	
	public static OperationType generateForCustomer(){
		OperationType operationType = new OperationType();
		operationType.setForCustomer(true);
		return operationType;
	}
	
	public static OperationType generateForDownstream(){
		OperationType operationType = new OperationType();
		operationType.setForDownstream(true);
		return operationType;
	}
	
	public static OperationType generateForBusiness(){
		OperationType operationType = new OperationType();
		operationType.setForBusiness(true);
		return operationType;
	}
	
	public static OperationType generateForUser(){
		OperationType operationType = new OperationType();
		operationType.setForUser(true);
		return operationType;
	}

	public boolean isForBusiness() {
		return forBusiness;
	}

	public void setForBusiness(boolean forBusiness) {
		this.forBusiness = forBusiness;
	}
	
	
	
	
}
