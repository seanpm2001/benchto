/*
 * Copyright 2013-2015, Teradata, Inc. All rights reserved.
 */
package com.teradata.benchmark.driver.execution;

import com.teradata.benchmark.driver.Benchmark;
import com.teradata.benchmark.driver.Query;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

public class QueryExecution
{
    private final Benchmark benchmark;
    private final Query query;
    private final int run;

    public QueryExecution(Benchmark benchmark, Query query, int run)
    {
        this.benchmark = checkNotNull(benchmark);
        this.query = checkNotNull(query);
        this.run = run;
    }

    public Benchmark getBenchmark()
    {
        return benchmark;
    }

    public String getQueryName()
    {
        return query.getName();
    }

    public String getSql()
    {
        return query.getSql();
    }

    public int getRun()
    {
        return run;
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("query", query)
                .add("run", run)
                .toString();
    }
}
