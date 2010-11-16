/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;


/**
 * <p>
 * This class is used for building the HQL Where clause according to the
 * <code>OrFilter</code> and appends it to the searchContext.
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
public class OrFragmentBuilder implements SearchFragmentBuilder {
    /**
     * Creates a new instance of this class.
     */
    public OrFragmentBuilder() {
    }

    /**
     * Builds the HQL Where clause according to the OrFilter and appends it to the
     * searchContext.
     *
     * @param filter
     *            The filter to create the Where clause with.
     * @param searchContext
     *            The searchContext on which to write the resultant HQL string.
     * @throws IllegalArgumentException
     *             If either parameter is null.
     * @throws UnrecognizedFilterException
     *             If the provided Filter is not an OrFilter or if any inner filter is not
     *             an associated filter.
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

        if (!(filter instanceof OrFilter)) {
            throw new UnrecognizedFilterException(
                "The filter should be an OrFilter, but a type of "
                    + filter.getClass().getName() + ".", filter);
        }

        OrFilter orFilter = (OrFilter) filter;
        SearchBuilderHelper.buildDBAbstractAssociativeFilter(orFilter, "OR",
            searchContext);
    }
}
