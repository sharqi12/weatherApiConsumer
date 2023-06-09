package gierobam.weatherapiconsumer.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gierobam.weatherapiconsumer.Model.DailyWeatherData;
import gierobam.weatherapiconsumer.Model.WeatherEndpointRequestData;
import gierobam.weatherapiconsumer.Repository.WeatherEndpointRequestDataRepository;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Setter
public class WeatherService {

    WeatherEndpointRequestDataRepository weatherEndpointRequestDataRepository;
    private RestTemplate restTemplate;
    public WeatherService(WeatherEndpointRequestDataRepository weatherEndpointRequestDataRepository) {
        this.weatherEndpointRequestDataRepository = weatherEndpointRequestDataRepository;
        this.restTemplate = new RestTemplate();
    }

    private final String baseUri = "https://archive-api.open-meteo.com/v1/archive";

    @Transactional
    public List<DailyWeatherData> getArchiveDataByCoordinates(Double latitude, Double longitude) throws JsonProcessingException, InvalidParameterException {
        if(!isValidCoordinates(latitude, longitude)) {
            throw new InvalidParameterException("The entered coordinates are invalid! Longitude must be in range of -180 to 180° and latitude must be in range of -90 to 90°.");
        }
        URI uri = createURIRequest(latitude, longitude);
        String jsonData = restTemplate.getForObject(uri, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> dataMap = objectMapper.readValue(jsonData, new TypeReference<>() {});

        Map<String, Object> dailyDataMap = (Map<String, Object>) dataMap.get("daily");
        List<String> days = (List<String>) dailyDataMap.get("time");
        List<String> sunrises = (List<String>) dailyDataMap.get("sunrise");
        List<String> sunsets = (List<String>) dailyDataMap.get("sunset");
        List<Double> rainSums = (List<Double>) dailyDataMap.get("rain_sum");
        List<Double> snowfallSums = (List<Double>) dailyDataMap.get("snowfall_sum");

        weatherEndpointRequestDataRepository.save(new WeatherEndpointRequestData(LocalDateTime.now(), latitude, longitude));

        return IntStream.range(0, days.size())
                .mapToObj(i -> {
                    DailyWeatherData dailyWeatherData = new DailyWeatherData();
                    dailyWeatherData.setDay(days.get(i));
                    dailyWeatherData.setSunrise(sunrises.get(i).substring(11,16));
                    dailyWeatherData.setSunset(sunsets.get(i).substring(11,16));
                    dailyWeatherData.setRain_sum(rainSums.get(i) != null ? rainSums.get(i) : 0);
                    dailyWeatherData.setSnowfall_sum(snowfallSums.get(i) != null ? snowfallSums.get(i) : 0);
                    return dailyWeatherData;
                })
                .collect(Collectors.toList());
    }

    public List<WeatherEndpointRequestData> getAllRequestedData() {
        return weatherEndpointRequestDataRepository.findAll();
    }

    private URI createURIRequest(Double latitude, Double longitude) {

        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusDays(6);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateStr = startDate.format(formatter);
        String endDateStr = endDate.format(formatter);

        return UriComponentsBuilder.fromUriString(baseUri)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("start_date", startDateStr)
                .queryParam("end_date", endDateStr)
                .queryParam("daily", "sunrise,sunset,rain_sum,snowfall_sum")
                .queryParam("timezone", "auto")
                .build()
                .encode()
                .toUri();
    }

    private boolean isValidCoordinates(Double latitude, Double longitude) {
        return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
    }
}
