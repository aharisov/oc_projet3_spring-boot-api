package com.openclassrooms.api.configuration;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openclassrooms.api.dto.RentalInfoDto;
import com.openclassrooms.api.dto.UserInfoDto;
import com.openclassrooms.api.model.Rental;
import com.openclassrooms.api.model.User;

@Configuration
public class MapperConfig {

	@Bean
	ModelMapper modelMapperBean() {
	    ModelMapper modelMapper = new ModelMapper();

	    // Converter for transforming mysql data to the formatted date string	    
	    Converter<Instant, String> instantToString =
	    		ctx -> ctx.getSource() == null ? null :
	                   DateTimeFormatter.ofPattern("yyyy/MM/dd")
	                   		.withZone(ZoneId.systemDefault())
                            .format(ctx.getSource());

	    modelMapper.typeMap(User.class, UserInfoDto.class)
	    	.addMappings(mapper -> {
	    		mapper.using(instantToString).map(User::getCreated_at, UserInfoDto::setCreated_at);
                mapper.using(instantToString).map(User::getUpdated_at, UserInfoDto::setUpdated_at);
            });
	    
	    modelMapper.typeMap(Rental.class, RentalInfoDto.class)
    	.addMappings(mapper -> {
    		mapper.using(instantToString).map(Rental::getCreated_at, RentalInfoDto::setCreated_at);
            mapper.using(instantToString).map(Rental::getUpdated_at, RentalInfoDto::setUpdated_at);
        });

	    return modelMapper;
	}
}
