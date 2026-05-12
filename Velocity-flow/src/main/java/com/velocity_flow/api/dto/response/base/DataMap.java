package com.velocity_flow.api.dto.response.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataMap {
	private Set<String> keysToShow = new HashSet<>();
	private Map<String, String> ignoreCamelCase = new HashMap<>();
	private Integer ribbonCount;
}
