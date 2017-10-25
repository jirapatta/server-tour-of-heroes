package com.heroes.tour

import groovy.util.logging.Slf4j
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.support.SpringBootServletInitializer

@Slf4j
class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		log.debug "ServletInitializer.configure"
		application.sources(TourApplication)
	}

}
