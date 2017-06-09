package com.andreabardella.aifaservicesconsumer.presenter;

import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.model.ItemLight;

import java.util.Set;

import io.reactivex.Observable;

public interface SearchPresenter {

    /**
     * Search for items of type <code>what</code> (by a specific type <code>by</code>).
     * <br>
     * It is possible to search for
     * <ul>
     *     <li>
     *         drugs ({@link SearchType#DRUG})
     *         <ul>
     *             <li>by drug name,</li>
     *             <li>by a specific active ingredient name ({@link SearchType#ACTIVE_INGREDIENT}),</li>
     *             <li>by a specific company ID ({@link SearchType#ACTIVE_INGREDIENT}),</li>
     *         </ul>
     *     <li>
     *         active ingredients ({@link SearchType#ACTIVE_INGREDIENT})
     *         <ul><li>by active ingredient name</li></ul>
     *     </li>
     *     <li>
     *         drugs ({@link SearchType#DRUG})
     *         <ul><li>by drug specific active ingredient</li></ul>
     *     </li>
     * </ul>
     *
     * @param searchParam the search parameter
     *                   e.g.:
     *                   "mesulid" (drug),
     *                   "paracetamolo" (active ingredient),
     *                   "laboratorio" (company),
     *                   "020096" (the first 6 digit of the AIC)
     * @param what the type of result to look for
     * @param by which type is referring the searchParam
     * @param refreshCache true to perform a new request,
     *                     false to retrieve the eventually already outgoing/performed request
     * @return the set of found items
     */
    Observable<Set<? extends ItemLight>> search(String searchParam,
                                                 SearchType what,
                                                 SearchType by,
                                                 boolean refreshCache);
}
