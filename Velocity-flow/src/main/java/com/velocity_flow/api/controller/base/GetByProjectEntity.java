package com.velocity_flow.api.controller.base;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface GetByProjectEntity<CreateRequest, UpdateRequest, APIReponse>
		extends CreateEntity<CreateRequest, UpdateRequest, APIReponse> {

	@GetMapping("/project/{projectId}")
	public APIReponse getByProjectId(@PathVariable("projectId") Long projectId);

}
