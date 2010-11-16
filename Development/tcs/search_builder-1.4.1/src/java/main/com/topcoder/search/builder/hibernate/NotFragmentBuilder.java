/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate;

import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.NotFilter;


/**
 * <p>
 * This class is used for building the HQL Where clause according to the
 * <code>NotFilter</code> and appends it to the searchContext.
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
public class NotFragmentBuilder implements SearchFragmentBuilder {
    /**
     * Creates a new instance of this class. Does nothing.
     */
    public NotFragmentBuilder() {
    }

    /**
     * Builds the HQL Where clause according to the NotFilter and appends it to the
     * searchContext.
     *
     * @param filter
     *            The filter to create the Where clause with.
     * @param searchContext
     *            The searchContext on which to write the resultant HQL string.
     * @throws IllegalArgumentException
     *             If either parameter is null.
     * @throws UnrecognizedFilterException
     *             If the provided Filter is not an AndFilter or if any inner filter is
     *             not an associated filter.
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

        if (!(filter instanceof NotFilter)) {
            throw new UnrecognizedFilterException("The filter should be an NotFilter.",
                filter);
        }

        NotFilter notFilter = (NotFilter) filter;

        StringBuffer buffer = searchContext.getSearchString();

        buffer.append("NOT (");

        // build the inner filter
        Filter innerfilter = notFilter.getFilter();

        SearchFragmentBuilder builder = searchContext.getFragmentBuilder(innerfilter);

        // if can not look up the filter successfully, then throw
        // UnrecognizedFilterException
        if (builder == null) {
            throw new UnrecognizedFilterException(
                "The filter should be a NotFilter, but a type of "
                    + filter.getClass().getName() + ".", innerfilter);
        }

        builder.buildSearch(innerfilter, searchContext);

        buffer.append(")");
    }
}
