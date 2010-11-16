/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.hibernate;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;

import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * This class is used for building the HQL Where clause according to the
 * <code>InFilter</code> and appends it to the searchContext.
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
public class InFragmentBuilder implements SearchFragmentBuilder {
    /**
     * Creates a new instance of this class.
     */
    public InFragmentBuilder() {
    }

    /**
     * Builds the HQL Where clause according to the InFilter and appends it to the
     * searchContext.
     *
     * @param filter
     *            The filter to create the Where clause with.
     * @param searchContext
     *            The searchContext on which to write the resultant HQL string.
     * @throws IllegalArgumentException
     *             if either parameter is null.
     * @throws UnrecognizedFilterException
     *             If the provided Filter is not an InFilter.
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

        if (!(filter instanceof InFilter)) {
            throw new UnrecognizedFilterException(
                "The filter should be an InFilter, but a type of "
                    + filter.getClass().getName() + ".", filter);
        }

        InFilter inFilter = (InFilter) filter;

        StringBuffer buffer = searchContext.getSearchString();
        List params = searchContext.getBindableParameters();

        // get the real name and append to buffer
        buffer.append(SearchBuilderHelper.getRealName(inFilter.getName(),
                searchContext));

        buffer.append(" IN (");

        List values = inFilter.getList();
        boolean isFirst = true;

        // set the String, also the parameter, the add should be same
        for (Iterator it = values.iterator(); it.hasNext();) {
            params.add(it.next());

            if (isFirst) {
                isFirst = false;
            } else {
                buffer.append(", ");
            }

            buffer.append("?");
        }

        buffer.append(")");
    }
}
