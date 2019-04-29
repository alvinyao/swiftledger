package com.higgschain.trust.consensus.term;

import java.util.List;
import java.util.Optional;

/**
 * The interface Term manager.
 *
 * @author suimi
 * @date 2019 /4/29
 */
public interface ITermManager {
    /**
     * Reset terms.
     *
     * @param infos the infos
     */
    void resetTerms(List<TermInfo> infos);

    /**
     * Gets term info.
     *
     * @param term the term
     * @return the term info
     */
    Optional<TermInfo> getTermInfo(long term);

    /**
     * Start new term.
     *
     * @param term       the term
     * @param masterName the master name
     */
    void startNewTerm(long term, String masterName);

    /**
     * Start new term.
     *
     * @param term        the term
     * @param masterName  the master name
     * @param startHeight the start height
     */
    void startNewTerm(long term, String masterName, long startHeight);

    /**
     * Is term height boolean.
     *
     * @param term          the term
     * @param masterName    the master name
     * @param packageHeight the package height
     * @return the boolean
     */
    boolean isTermHeight(long term, String masterName, long packageHeight);

    /**
     * Reset end height.
     *
     * @param packageHeight the package height
     */
    void resetEndHeight(long packageHeight);

    /**
     * End term.
     */
    void endTerm();

    /**
     * Gets terms.
     *
     * @return the terms
     */
    List<TermInfo> getTerms();
}
