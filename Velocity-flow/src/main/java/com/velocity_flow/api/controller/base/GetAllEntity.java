package com.velocity_flow.api.controller.base;

import org.springframework.web.bind.annotation.GetMapping;

public interface GetAllEntity<CreateRequest, UpdateRequest, APIReponse> {

	@GetMapping
	public APIReponse getAll();

}
