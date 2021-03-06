package ru.andnovikov.sportnow.service;

import liquibase.license.LicenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.andnovikov.sportnow.domain.Event;
import ru.andnovikov.sportnow.domain.Registration;
import ru.andnovikov.sportnow.domain.User;
import ru.andnovikov.sportnow.domain.enumeration.RegStatus;
import ru.andnovikov.sportnow.repository.RegistrationRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final RegistrationRepository registrationRepository;
    private final UserServiceImpl userServiceImpl;
    private final EventService eventService;

    @Autowired
    public RegistrationServiceImpl(RegistrationRepository registrationRepository, UserServiceImpl userServiceImpl, EventService eventService) {
        this.registrationRepository = registrationRepository;
        this.userServiceImpl = userServiceImpl;
        this.eventService = eventService;
    }

    /**
     * Generate new Registration for userId and eventId
     *
     * @param userId for searching user
     * @param eventId for searching event
     * @return the persisted entity.
     */
    @Override
    public Registration newRegistration(Long userId, Long eventId) {
        Registration result = new Registration();
        result.setEvent(eventService.findOne(eventId).orElseThrow(NoDataFoundException::new));
        result.setRegDate(new Date());
        result.setStatus(RegStatus.NEW);
        return result;
    }

    /**
     * Save a userRegistration.
     *
     * @param registration the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Registration save(Registration registration) {
        log.debug("Request to save UserRegistration : {}", registration);
        if (registration.getStatus() == RegStatus.PAYED) {
            int regNumber = getEventRegistrationNumber(registration.getEvent(), RegStatus.PAYED);
            registration.setRegNumber(regNumber);
        }
        return registrationRepository.save(registration);
    }

    /**
     * Get all the userRegistration.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<Registration> findAll(Pageable pageable) {
        log.debug("Request to get all UserRegistration");
        return registrationRepository.findAll(pageable);
    }

    /**
     * Get one userRegistration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Registration> findOne(Long id) {
        log.debug("Request to get UserRegistration : {}", id);
        return registrationRepository.findById(id);
    }

    /**
     * Delete the userRegistration by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserRegistration : {}", id);
        registrationRepository.deleteById(id);
    }

    @Override
    public List<Registration> getAllByUserAndStatus (User user, RegStatus status) {
        log.debug("Request to get UserRegistration : {}", user.getLogin());
        return registrationRepository.getAllByUserAndStatus(user, status);
    }

    private int getEventRegistrationNumber(Event event, RegStatus status) {
        Optional<Registration> registration = registrationRepository.findFirstByEventAndStatusOrderByRegNumberDesc(event, status);
        if (!registration.isPresent()) {
            return 1;
        } else {
            return registration.get().getRegNumber()+1;
        }
    }
}
