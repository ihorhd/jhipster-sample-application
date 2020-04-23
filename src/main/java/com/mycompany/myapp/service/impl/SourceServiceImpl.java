package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.SourceService;
import com.mycompany.myapp.domain.Source;
import com.mycompany.myapp.repository.SourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Source}.
 */
@Service
@Transactional
public class SourceServiceImpl implements SourceService {

    private final Logger log = LoggerFactory.getLogger(SourceServiceImpl.class);

    private final SourceRepository sourceRepository;

    public SourceServiceImpl(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    /**
     * Save a source.
     *
     * @param source the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Source save(Source source) {
        log.debug("Request to save Source : {}", source);
        return sourceRepository.save(source);
    }

    /**
     * Get all the sources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Source> findAll(Pageable pageable) {
        log.debug("Request to get all Sources");
        return sourceRepository.findAll(pageable);
    }

    /**
     * Get one source by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Source> findOne(Long id) {
        log.debug("Request to get Source : {}", id);
        return sourceRepository.findById(id);
    }

    /**
     * Delete the source by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Source : {}", id);
        sourceRepository.deleteById(id);
    }
}
