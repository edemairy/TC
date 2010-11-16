/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;


/**
 * <p>
 * This class is used for building the HQL Where clause according to the EqualsFilter and
 * appends it to the searchContext.
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
public class EqualsFragmentBuilder implements SearchFragmentBuilder {
    /**
     * Creates a new instance of this class.
     */
    public EqualsFragmentBuilder() {
    }

    /**
     * Builds the HQL Where clause according to the EqualsFilter and appends it to the
     * searchContext.
     *
     * @param filter
     *            The filter to create the Where clause with.
     * @param searchContext
     *            The searchContext on which to write the resultant HQL string.
     * @throws UnrecognizedFilterException
     *             If the provided Filter is not an EqualsFilter.
     * @throws IllegalArgumentException
     *             if filter or searchContext is null.
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

        if (!(filter instanceof EqualToFilter)) {
            throw new UnrecognizedFilterException(
                "The filter should be an EqualToFilter, but a type of "
                    + filter.getClass().getName() + ".", filter);
        }

        EqualToFilter equalToFilter = (EqualToFilter) filter;

        SearchBuilderHelper.buildDBSimpleFilter(equalToFilter, "=",
            searchContext, equalToFilter.getValue());
    }
}
