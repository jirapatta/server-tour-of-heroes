package com.heroes.tour.controller

import com.heroes.tour.domain.Hero
import groovy.util.logging.Slf4j
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/heroes')
@CrossOrigin('http://localhost:4200')
@Slf4j
class HeroController {

    def heroMap = []

    HeroController() {
        def hero = new Hero(0, 'Zero')
        heroMap.add(hero)

        hero = new Hero(1, 'Aeloriadru')
        heroMap.add(hero)

        hero = new Hero(2, 'Blilu')
        heroMap.add(hero)

        hero = new Hero(3, 'Cairinura')
        heroMap.add(hero)

        hero = new Hero(4, 'Dynam')
        heroMap.add(hero)
    }

    @GetMapping
    def getHeroes() {
        log.debug 'in getHeroes()'
        return heroMap
    }

    @GetMapping('/search')
    def searchHeroes(@RequestParam('name') String name) {
        log.debug("in searchHeroes(), term: ${name}")
        if (name != null) {
            heroMap.findAll { it.name.toLowerCase().contains(name.toLowerCase()) }
        }
        else {
            []
        }
    }

    @GetMapping('/{id}')
    def getHero(@PathVariable('id') int id) {
        log.debug "in getHero(), id: ${id}"
        heroMap.find { it.id == id }
    }

    @PostMapping
    def addHero(@RequestBody Hero hero) {
        log.debug "in addHero(): heor: ${hero}"
        def latestHero = heroMap.max { it.id }
//        println "max ${latestHero}"
        hero.id = latestHero.id + 1

        log.debug "add with id: ${hero.id}"
        heroMap.add(hero)

        return hero
    }

    @PutMapping
    def updateHero(@RequestBody Hero hero) {
        log.debug "in updateHero(), hero: ${hero}"
        def repositoryHero = heroMap.find { it.id == hero.id }
        repositoryHero.name = hero.name

        return repositoryHero
    }

    @DeleteMapping('/{id}')
    def deleteHero(@PathVariable('id') int id) {
        log.debug "in deleteHero(), id: ${id}"
        def hero = heroMap.find { it.id == id }
        heroMap.remove(hero)
    }
}
