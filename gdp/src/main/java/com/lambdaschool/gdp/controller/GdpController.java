package com.lambdaschool.gdp.controller;

import com.lambdaschool.gdp.GdpApplication;
import com.lambdaschool.gdp.exception.ResourceNotFoundException;
import com.lambdaschool.gdp.model.GDP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class GdpController {
    private static final Logger logger = LoggerFactory.getLogger(GdpController.class);


    //localhost:2019/names
    @GetMapping(value = "/names", produces = {"application/json"})
    public ResponseEntity<?> getAllCountries(HttpServletRequest request) {
        logger.info(request.getRequestURI() + " accessed");

        GdpApplication.ourGdpList.gdpList.sort((e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()));
        return new ResponseEntity<>(GdpApplication.ourGdpList.gdpList, HttpStatus.OK);
    }

    //localhost:2019/names
    @GetMapping(value = "/economy", produces = {"application/json"})
    public ResponseEntity<?> getAllGdp(HttpServletRequest request) {
        logger.info(request.getRequestURI() + " accessed");

        List<GDP> sorted = GdpApplication.ourGdpList.gdpList.stream()
                .sorted(Comparator.comparing(GDP::getGdp).reversed())
                .collect(Collectors.toList());
        return new ResponseEntity<>(sorted, HttpStatus.OK);
    }


    // localhost:2019/country/{id}
    @GetMapping(value = "/country/{id}", produces = {"application/json"})
    public ResponseEntity<?> getCountryByID(HttpServletRequest request, @PathVariable long id) throws ResourceNotFoundException {
        logger.trace(request.getRequestURI() + " accessed");

        GDP rtnGDP;
        if (GdpApplication.ourGdpList.findGdp(b -> (b.getId() == id)) == null)
        {
            throw new ResourceNotFoundException("Country with id " + id + " not found");
        } else
        {
            rtnGDP = GdpApplication.ourGdpList.findGdp(b -> (b.getId() == id));
        }
        return new ResponseEntity<>(rtnGDP, HttpStatus.OK);
    }

    //localhost:2019/country/stats/median
    @GetMapping(value = "/country/stats/median", produces = {"application/json"})
    public ResponseEntity<?> getAllCountryStatsMedian(HttpServletRequest request) {
        logger.info(request.getRequestURI() + " accessed");

        List<GDP> sorted = GdpApplication.ourGdpList.gdpList.stream()
                .sorted(Comparator.comparing(GDP::getGdp))
                .collect(Collectors.toList());

        return new ResponseEntity<>(sorted.get(sorted.size()/2), HttpStatus.OK);
    }

    // localhost:2019/economy/table
    @GetMapping(value = "/economy/table")
    public ModelAndView displayGdpTable(HttpServletRequest request) {
        logger.trace(request.getRequestURI() + " accessed");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("gdp");
        mav.addObject("gdpList", GdpApplication.ourGdpList.gdpList);

        return mav;
    }
}
