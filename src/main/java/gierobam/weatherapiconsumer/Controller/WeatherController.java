package gierobam.weatherapiconsumer.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gierobam.weatherapiconsumer.Model.DailyWeatherData;
import gierobam.weatherapiconsumer.Model.WeatherEndpointRequestData;
import gierobam.weatherapiconsumer.Service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {this.weatherService = weatherService;}

    @GetMapping("weather/archive")
    public List<DailyWeatherData> getWeatherData(@RequestParam Double latitude, @RequestParam Double longitude) throws JsonProcessingException, InvalidParameterException {
        return weatherService.getArchiveDataByCoordinates(latitude, longitude);
    }

    @GetMapping("getAllRequestsData")
    public List<WeatherEndpointRequestData> getAllRequestedData() {
        return weatherService.getAllRequestedData();
    }
}
