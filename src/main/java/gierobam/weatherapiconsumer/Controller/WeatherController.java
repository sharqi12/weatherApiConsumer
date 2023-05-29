package gierobam.weatherapiconsumer.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gierobam.weatherapiconsumer.Model.DailyWeatherData;
import gierobam.weatherapiconsumer.Model.WeatherEndpointRequestData;
import gierobam.weatherapiconsumer.Service.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private Service service;

    public RestController(Service service) {this.service = service;}

    @GetMapping("weather/archive")
    public List<DailyWeatherData> getWeatherData(@RequestParam String latitude, @RequestParam String longitude) throws JsonProcessingException {
        return service.getArchiveDataByCoordinates(latitude, longitude);
    }

    @GetMapping("getAllRequestsData")
    public List<WeatherEndpointRequestData> getAllRequestedData() {
        return service.getAllRequestedData();
    }
}
