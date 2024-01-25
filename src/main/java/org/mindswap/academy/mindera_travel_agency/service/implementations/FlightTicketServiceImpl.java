package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.FlightTicketConverter;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketDuplicateException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.repository.FlightTicketRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FlightTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.DUPLICATE_FLIGHT_TICKET_NUMBER;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.ID_NOT_FOUND;

@Service
public class FlightTicketServiceImpl implements FlightTicketService {

    @Autowired
    private FlightTicketRepository flightTicketRepository;
    @Autowired
    private FlightTicketConverter flightTicketConverter;

    //TODO verificar propriedades que so sao colocadas na compra do bilhete
    //TODO ir buscar invoice e fare class ao criar

    @Override
    public List<FlightTicketGetDto> getAll() {
        return flightTicketConverter.fromEntityListToGetDtoList(flightTicketRepository.findAll());
    }

    @Override
    public List<FlightTicketGetDto> getAllByUser(String sortBy, Long userId) {
        return flightTicketConverter.fromEntityListToGetDtoList(flightTicketRepository.findAllByUserId(userId));
    }

    @Override
    public List<FlightTicketGetDto> getAllByInvoice(String sortBy, Long invoiceId) {
        return flightTicketConverter.fromEntityListToGetDtoList(flightTicketRepository.findAllByInvoiceId(invoiceId));
    }

    @Override
    public FlightTicketGetDto getById(Long id) throws FlightTicketNotFoundException {
        return flightTicketConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    public FlightTicketGetDto create(FlightTicketCreateDto flightTicket) throws FlightTicketDuplicateException {
        FlightTicket flightTicketToSave = flightTicketConverter.fromCreateDtoToEntity(flightTicket);
        checkDuplicateTicketNumber(flightTicketToSave.getTicketNumber(), 0L);
        return flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(flightTicketToSave));
    }

    @Override
    public FlightTicketGetDto update(Long id, FlightTicketCreateDto flightTicket) throws FlightTicketDuplicateException {
        FlightTicket flightTicketToUpdate = flightTicketConverter.fromCreateDtoToEntity(flightTicket);
        checkDuplicateTicketNumber(flightTicketToUpdate.getTicketNumber(), id);
        flightTicketToUpdate.setId(id);
        return flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(flightTicketToUpdate));
    }

    @Override
    public FlightTicketGetDto updatePartial(Long id, FlightTicketUpdateDto flightTicket) throws FlightTicketNotFoundException, FlightTicketDuplicateException {
        FlightTicket flightTicketToUpdate = findById(id);
        //TODO add properties
        return flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(flightTicketToUpdate));
    }

    @Override
    public void delete(Long id) throws FlightTicketNotFoundException {
        findById(id);
        flightTicketRepository.deleteById(id);
    }

    public FlightTicket findById(Long id) throws FlightTicketNotFoundException {
        return flightTicketRepository.findById(id).orElseThrow(() -> new FlightTicketNotFoundException(ID_NOT_FOUND + id));
    }


    private void checkDuplicateTicketNumber(Long ticketNumber, Long id) throws FlightTicketDuplicateException {
        Optional<FlightTicket> flightTicket = flightTicketRepository.findByTicketNumber(ticketNumber);
        if (flightTicket.isPresent() && !flightTicket.get().getId().equals(id)) {
            throw new FlightTicketDuplicateException(DUPLICATE_FLIGHT_TICKET_NUMBER);
        }
    }
}
