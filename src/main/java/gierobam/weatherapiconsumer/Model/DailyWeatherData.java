package gierobam.weatherapiconsumer.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyWeatherData {

    private String day;

    private String sunrise;

    private String sunset;

    private Double rain_sum;

    private Double snowfall_sum;

}
