/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.NullFilter;


/**
 * <p>
 * This class is used for building the HQL Where clause according to the
 * <code>NullFilter</code> and appends it to the searchContext.
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
public class NullFragmentBuilder implements SearchFragmentBuilder {
    /**
     * Creates a new instance of this class.
     */
    public NullFragmentBuilder() {
    }

    /**
     * Builds the HQL Where clause according to the NullFilter and appends it to the
     * searchContext.
     *
     * @param filter
     *            The filter to create the Where clause with.
     * @param searchContext
     *            The searchContext on which to write the resultant HQL string.
     * @throws IllegalArgumentException
     *             If either parameter is null.
     * @throws UnrecognizedFilterException
     *             If the provided Filter is not an NullFilter.
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

        if (!(filter instanceof NullFilter)) {
            throw new UnrecognizedFilterException(
                "The filter should be a NullFilter, but a type of "
                    + filter.getClass().getName() + ".", filter);
        }

        NullFilter nullFilter = (NullFilter) filter;

        StringBuffer buffer = searchContext.getSearchString();

        buffer.append(SearchBuilderHelper.getRealName(nullFilter.getName(),
                searchContext)).append(" IS NULL");
    }
}
