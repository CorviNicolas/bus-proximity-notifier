package com.corvinicolas.bus_tracker.mappers;

import com.corvinicolas.bus_tracker.application.service.model.BusProximityModel;
import com.corvinicolas.bus_tracker.providers.tmb_app.dto.IBusStopPrevisionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusProximityMapper {
    default BusProximityModel iBusToServiceModel(IBusStopPrevisionResponse busResponse, String line){
        if(busResponse == null) return null;
        if(busResponse.getData() == null) return null;
        if(busResponse.getData().getIbus().isEmpty()) return null;
        IBusStopPrevisionResponse.IBus iBus = busResponse.getData().getIbus().getFirst();

        BusProximityModel busProximityModel = new BusProximityModel();
        busProximityModel.setLine(line);
        busProximityModel.setRouteId(iBus.getRouteId());
        busProximityModel.setTextCa(iBus.getTextCa());
        busProximityModel.setTimeInMinutes(iBus.getTimeInMinutes());
        busProximityModel.setTimeInSeconds(iBus.getTimeInSeconds());
        return busProximityModel;
    }
}
