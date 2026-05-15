package com.velocity_flow.api.dto.response.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataMap {
	private List<String> keysToShow = new ArrayList<>();
	private Map<String, String> ignoreCamelCase = new HashMap<>();
	private Integer ribbonCount;
}
