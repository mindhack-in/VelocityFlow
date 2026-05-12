package com.velocity_flow.api.dto.response.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BaseResponse<DataMap, DataList> {

	private DataMap dataMap;
	private List<DataList> dataList = new ArrayList<>();
	private Set<String> insufficientPermission = new HashSet<>();
	private Set<String> generalException = new HashSet<>();
	private String widgetName;

	private String message;

	public BaseResponse(DataMap dataMap) {
		this.dataMap = dataMap;

	}

	public void addInsufficientPermission(String accountName, String accountNumber) {
		insufficientPermission.add(accountName + "( " + accountNumber + " )");
	}

	public void addGeneralException(String accountName, String accountNumber) {
		generalException.add(accountName + "( " + accountNumber + " )");
	}

	public String appendNameAndNumber(String accountName, String accountNumber) {
		return accountName + "( " + accountNumber + " )";

	}

}