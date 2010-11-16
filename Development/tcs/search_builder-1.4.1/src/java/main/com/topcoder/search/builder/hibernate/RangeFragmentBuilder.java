/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.AbstractSimpleFilter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;


/**
 * <p>
 * This class is used for building the HQL Where clause according to the
 * <code>RangeFilter</code> and appends it to the searchContext.
 * </p>
 * <p>
 * Thread Safety: This class is thread-safe. All state information is handled within the
 * searchContext and only one SearchContext is used per thread.
 * </p>
 *
 * @author kurtrips, myxgyy
 * @version 1.4
 * @since 1.4
 */
public class RangeFragmentBuilder implements SearchFragmentBuilder {
    /**
     * Creates a new instance of this class.
     */
    public RangeFragmentBuilder() {
    }

    /**
     * Builds the HQL Where clause according to the RangeFilter and appends it to the
     * searchContext.
     *
     * @param filter
     *            The filter to create the Where clause with.
     * @param searchContext
     *            The searchContext on which to write the resultant HQL string.
     * @throws IllegalArgumentException
     *             If either parameter is null.
     * @throws UnrecognizedFilterException
     *             If the provided Filter is not a GreaterThanFilter, LessThanFilter,
     *             GreaterThanOrEqualToFilter,or LessThanOrEqualToFilter, BetweenFilter.
     */
    public void buildSearch(Filter filter, SearchContext searchContext)
        throws UnrecognizedFilterException {
        if (filter == null) {
            throw new IllegalArgumentException("The filter should not be null.");
        }

        if (searchContext == null) {
            throw new IllegalArgumentException(
                "The searchContext should not be null.");
        }

        // if the filter is between filter, append both the lower and upper
        if (filter instanceof BetweenFilter) {
            BetweenFilter betweenFilter = (BetweenFilter) filter;

            StringBuffer buffer = searchContext.getSearchString();
            buffer.append(SearchBuilderHelper.getRealName(
                    betweenFilter.getName(), searchContext));
            buffer.append(" BETWEEN ?");
            searchContext.getBindableParameters()
                         .add(betweenFilter.getLowerThreshold());
            buffer.append(" AND ?");
            searchContext.getBindableParameters()
                         .add(betweenFilter.getUpperThreshold());

            return;
        }

        String operator = null;

        // determine the operator by the type of the filter
        if (filter instanceof GreaterThanFilter) {
            operator = ">";
        } else if (filter instanceof LessThanFilter) {
            operator = "<";
        } else if (filter instanceof GreaterThanOrEqualToFilter) {
            operator = ">=";
        } else if (filter instanceof LessThanOrEqualToFilter) {
            operator = "<=";
        }

        // if operator is null, means the filter is of an invalid type
        if (operator == null) {
            throw new UnrecognizedFilterException(
                "The filter should be one of [GreaterThanFilter, LessThanFilter,"
                + " LessThanOrEqualToFilter, GreaterThanOrEqualToFilter, BetweenFilter].",
                filter);
        }

        // builder the simple filter with the search context
        AbstractSimpleFilter simpleFilter = (AbstractSimpleFilter) filter;
        SearchBuilderHelper.buildDBSimpleFilter(simpleFilter, operator,
            searchContext, simpleFilter.getValue());
    }
}
