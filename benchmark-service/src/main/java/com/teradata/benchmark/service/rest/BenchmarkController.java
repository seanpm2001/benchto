/*
 * Copyright 2013-2015, Teradata, Inc. All rights reserved.
 */
package com.teradata.benchmark.service.rest;

import com.teradata.benchmark.service.BenchmarkService;
import com.teradata.benchmark.service.model.BenchmarkRun;
import com.teradata.benchmark.service.rest.requests.BenchmarkStartRequest;
import com.teradata.benchmark.service.rest.requests.ExecutionStartRequest;
import com.teradata.benchmark.service.rest.requests.FinishRequest;
import com.teradata.benchmark.service.rest.requests.GenerateBenchmarkNamesRequestItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

import static com.teradata.benchmark.service.utils.CollectionUtils.failSafeEmpty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class BenchmarkController
{

    @Autowired
    private BenchmarkService benchmarkService;

    @RequestMapping(value = "/v1/benchmark/generate-unique-names", method = POST)
    public List<String> generateUniqueBenchmarkNames(List<GenerateBenchmarkNamesRequestItem> generateItems)
    {
        return generateItems.stream()
                .map(requestItem -> benchmarkService.generateUniqueBenchmarkName(requestItem.getBenchmarkName(), requestItem.getVariables()))
                .collect(toList());
    }

    @RequestMapping(value = "/v1/benchmark/{uniqueName}/{benchmarkSequenceId}/start", method = POST)
    public String startBenchmark(
            @PathVariable("uniqueName") String uniqueName,
            @PathVariable("benchmarkSequenceId") String benchmarkSequenceId,
            @RequestBody @Valid BenchmarkStartRequest startRequest)
    {
        return benchmarkService.startBenchmarkRun(uniqueName,
                startRequest.getName(),
                benchmarkSequenceId,
                ofNullable(startRequest.getEnvironmentName()),
                failSafeEmpty(startRequest.getVariables()),
                failSafeEmpty(startRequest.getAttributes()));
    }

    @RequestMapping(value = "/v1/benchmark/{uniqueName}/{benchmarkSequenceId}/finish", method = POST)
    public void finishBenchmark(
            @PathVariable("uniqueName") String uniqueName,
            @PathVariable("benchmarkSequenceId") String benchmarkSequenceId,
            @RequestBody @Valid FinishRequest finishRequest)
    {
        benchmarkService.finishBenchmarkRun(uniqueName,
                benchmarkSequenceId,
                finishRequest.getStatus(),
                failSafeEmpty(finishRequest.getMeasurements()),
                failSafeEmpty(finishRequest.getAttributes()));
    }

    @RequestMapping(value = "/v1/benchmark/{uniqueName}/{benchmarkSequenceId}/execution/{executionSequenceId}/start", method = POST)
    public void startExecution(
            @PathVariable("uniqueName") String uniqueName,
            @PathVariable("benchmarkSequenceId") String benchmarkSequenceId,
            @PathVariable("executionSequenceId") String executionSequenceId,
            @RequestBody @Valid ExecutionStartRequest startRequest)
    {
        benchmarkService.startExecution(uniqueName,
                benchmarkSequenceId,
                executionSequenceId,
                failSafeEmpty(startRequest.getAttributes()));
    }

    @RequestMapping(value = "/v1/benchmark/{uniqueName}/{benchmarkSequenceId}/execution/{executionSequenceId}/finish", method = POST)
    public void finishExecution(
            @PathVariable("uniqueName") String uniqueName,
            @PathVariable("benchmarkSequenceId") String benchmarkSequenceId,
            @PathVariable("executionSequenceId") String executionSequenceId,
            @RequestBody @Valid FinishRequest finishRequest)
    {
        benchmarkService.finishExecution(uniqueName,
                benchmarkSequenceId,
                executionSequenceId,
                finishRequest.getStatus(),
                failSafeEmpty(finishRequest.getMeasurements()),
                failSafeEmpty(finishRequest.getAttributes()));
    }

    @RequestMapping(value = "/v1/benchmark/{uniqueName}/{benchmarkSequenceId}", method = GET)
    public BenchmarkRun findBenchmark(
            @PathVariable("uniqueName") String uniqueName,
            @PathVariable("benchmarkSequenceId") String benchmarkSequenceId)
    {
        return benchmarkService.findBenchmarkRun(uniqueName, benchmarkSequenceId);
    }

    @RequestMapping(value = "/v1/benchmark/{uniqueName}", method = GET)
    public List<BenchmarkRun> findBenchmarks(
            @PathVariable("uniqueName") String uniqueName)
    {
        return benchmarkService.findBenchmark(uniqueName);
    }

    @RequestMapping(value = "/v1/benchmark/latest", method = GET)
    public List<BenchmarkRun> findLatestBenchmarkRuns()
    {
        return benchmarkService.findLatest();
    }
}
