package com.usa.ri.gov.ies.co.batches.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usa.ri.gov.ies.co.batches.main.CoPlanStmtGenDlyBatch;

@RestController
public class BatchController {

	@Autowired
	private CoPlanStmtGenDlyBatch coPlanStmtBatch;
	@GetMapping(value="/startBatch/{tb}/{ci}")
	public String  starBatch(@PathVariable("tb") String tb,@PathVariable("ci") String ci) {
		
		coPlanStmtBatch.init(Integer.parseInt(tb),Integer.parseInt(ci));
		return "Batch Completed";
	}
}
