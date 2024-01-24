package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.FlightTicketConverter;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.repository.FlightTicketRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FlightTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.ID_NOT_FOUND;

@Service
public class FlightTicketServiceImpl implements FlightTicketService {

    @Autowired
    private FlightTicketRepository flightTicketRepository;
    @Autowired
    private FlightTicketConverter flightTicketConverter;

    //TODO verificar propriedades que so sao colocadas na compra do bilhete

    @Override
    public List<FlightTicketGetDto> getAll() {
        return flightTicketConverter.fromEntityListToGetDtoList(flightTicketRepository.findAll());
    }

    @Override
    public FlightTicketGetDto getById(Long id) throws FlightTicketNotFoundException {
        return flightTicketConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    public FlightTicketGetDto create(FlightTicketCreateDto flightTicket) {
        FlightTicket flightTicketToSave = flightTicketConverter.fromCreateDtoToEntity(flightTicket);
        return flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(flightTicketToSave));
    }

    @Override
    public FlightTicketGetDto update(Long id, FlightTicketCreateDto flightTicket) {
        FlightTicket flightTicketToUpdate = flightTicketConverter.fromCreateDtoToEntity(flightTicket);
        flightTicketToUpdate.setId(id);
        return flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(flightTicketToUpdate));
    }

    @Override
    public FlightTicketGetDto updatePartial(Long id, FlightTicketUpdateDto flightTicket) throws FlightTicketNotFoundException {
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

}
