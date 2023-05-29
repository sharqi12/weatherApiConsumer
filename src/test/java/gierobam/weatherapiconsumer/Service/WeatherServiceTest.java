package gierobam.weatherapiconsumer.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gierobam.weatherapiconsumer.Model.DailyWeatherData;
import gierobam.weatherapiconsumer.Repository.WeatherEndpointRequestDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
class WeatherServiceTest {
    @Mock
    private WeatherEndpointRequestDataRepository weatherEndpointRequestDataRepository;

    private WeatherService weatherService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        weatherService = new WeatherService(weatherEndpointRequestDataRepository);
    }

    @Test
    public void testGetArchiveDataByCoordinates() throws JsonProcessingException {
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        when(restTemplate.getForObject(any(URI.class), any(Class.class))).thenReturn(getJsonData());

        weatherService.setRestTemplate(restTemplate);

        List<DailyWeatherData> actualData = weatherService.getArchiveDataByCoordinates(52.5, 13.400009);

        List<DailyWeatherData> shortenedExpectedData = Arrays.asList(
                new DailyWeatherData("2023-05-20", "05:00", "21:04", 0.0, 0.0),
                new DailyWeatherData("2023-05-21", "04:59", "21:06", 0.2, 0.0)
        );

        assertEquals(shortenedExpectedData.size(), actualData.size());
        for (int i = 0; i < shortenedExpectedData.size(); i++) {
            DailyWeatherData expected = shortenedExpectedData.get(i);
            DailyWeatherData actual = actualData.get(i);
            assertEquals(expected.getDay(), actual.getDay());
            assertEquals(expected.getSunrise(), actual.getSunrise());
            assertEquals(expected.getSunset(), actual.getSunset());
            assertEquals(expected.getRain_sum(), actual.getRain_sum());
            assertEquals(expected.getSnowfall_sum(), actual.getSnowfall_sum());
        }
    }

    @Test
    public void testInvalidLatitudeThrowsException() {

        Double invalidLatitude = 100.0;

        Assertions.assertThrows(InvalidParameterException.class, () -> {
            weatherService.getArchiveDataByCoordinates(invalidLatitude, 0.0);
        });
    }

    @Test
    public void testInvalidLongitudeThrowsException() {

        Double invalidLongitude = 1000.0;

        Assertions.assertThrows(InvalidParameterException.class, () -> {
            weatherService.getArchiveDataByCoordinates(50.5, invalidLongitude);
        });
    }

    @Test
    public void testInvalidCoordinatesThrowsException() {

        Double invalidLatitude = 125.5;
        Double invalidLongitude = -502.69;

        Assertions.assertThrows(InvalidParameterException.class, () -> {
            weatherService.getArchiveDataByCoordinates(invalidLatitude, invalidLongitude);
        });
    }

    private String getJsonData() {
        return "{\"latitude\":52.5,\"longitude\":13.400009,\"generationtime_ms\":0.4259347915649414,\"utc_offset_seconds\":7200,\"timezone\":\"Europe/Berlin\",\"timezone_abbreviation\":\"CEST\",\"elevation\":38.0,\"daily_units\":{\"time\":\"iso8601\",\"sunrise\":\"iso8601\",\"sunset\":\"iso8601\",\"rain_sum\":\"mm\",\"snowfall_sum\":\"cm\"},\"daily\":{\"time\":[\"2023-05-20\",\"2023-05-21\"],\"sunrise\":[\"2023-05-20T05:00\",\"2023-05-21T04:59\"],\"sunset\":[\"2023-05-20T21:04\",\"2023-05-21T21:06\"],\"rain_sum\":[0.0,0.2],\"snowfall_sum\":[0.0,0.0]}}";
    }
}