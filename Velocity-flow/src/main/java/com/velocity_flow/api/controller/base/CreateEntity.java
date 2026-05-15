package com.velocity_flow.api.controller.base;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CreateEntity<CreateRequest, UpdateRequest, APIReponse>
		extends GetAllEntity<CreateRequest, UpdateRequest, APIReponse>{

	@PostMapping
	public APIReponse create(@RequestBody CreateRequest createRequest);

	@PutMapping
	public APIReponse update(@RequestBody UpdateRequest createRequest);

	@GetMapping("/{id}")
	public APIReponse get(@PathVariable("id") Long id);

	@DeleteMapping("/{id}")
	public APIReponse Delete(@PathVariable("id") Long id);

}
