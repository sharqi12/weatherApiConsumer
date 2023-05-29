package gierobam.weatherapiconsumer.Repository;

import gierobam.weatherapiconsumer.Model.WeatherEndpointRequestData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherEndpointRequestDataRepository extends JpaRepository<WeatherEndpointRequestData, Long> {
}
