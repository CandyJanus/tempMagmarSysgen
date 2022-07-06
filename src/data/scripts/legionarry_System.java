package data.scripts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.PlanetConditionGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin.AsteroidFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldSource;
import com.fs.starfarer.api.util.Misc;

public class legionarry_System {
    public void generate(SectorAPI sector){

        StarSystemAPI system = sector.createStarSystem("Dienta");
        //note: temporarily setting this to the middle of the sector in case it's an issue
        system.getLocation().set(-23000,-20000);
        //system.setBackgroundTextureFilename("graphics/backgrounds/leg_background.jpg");
        //note: temp change, I don't have the file
        system.setBackgroundTextureFilename("graphics/backgrounds/background2.jpg");

        float jumpFringeDist=4000f;
        float asteroids1Dist=20000f;
        float asteroidBelt1Dist=13000f;

        //note: ...there is no white dwarf entry in planets.json vanilla

        PlanetAPI dienta_Star = system.initStar("leg_dienta", // unique id for this star
        "star_white", // id in planets.json
        500f, // radius (in pixels at default zoom)
        150); // corona radius, from star edge
        dienta_Star.setName("Dienta");
        system.setLightColor(new Color(175, 175, 234));

    SectorEntityToken DietaAF1 = system.addTerrain(Terrain.ASTEROID_FIELD,
        new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                200f, // min radius
                400f, // max radius
                36, // min asteroid count
                68, // max asteroid count
                4f, // min asteroid radius
                16f, // max asteroid radius
                "Asteroids Field")); // null for default name
        DietaAF1.setCircularOrbit(dienta_Star, 130, asteroids1Dist, 240);

        system.addAsteroidBelt(dienta_Star, 1700, asteroidBelt1Dist, 800, 250, 400, Terrain.ASTEROID_BELT, "Inner Band");
        system.addRingBand(dienta_Star, "misc", "rings_asteroids0", 256f, 3, Color.gray, 256f, asteroidBelt1Dist - 200, 250f);
        system.addRingBand(dienta_Star, "misc", "rings_asteroids0", 256f, 0, Color.gray, 256f, asteroidBelt1Dist, 350f);
        system.addRingBand(dienta_Star, "misc", "rings_asteroids0", 256f, 2, Color.gray, 256f, asteroidBelt1Dist + 200, 400f);

    PlanetAPI dienta_1 = system.addPlanet("leg_azura",
            dienta_Star,
        "Azura",
        "water",
        360f * (float) Math.random(),
        220f,
        3000f,
        150f);

    //note: sorry that I commented it out, it shouldn't be buggy if you actually have the csv
    //dienta_1.setCustomDescriptionId("leg_planet_azura"); //reference descriptions.csv

        //note: I also made this an independent market temporarily for my own convenience

    MarketAPI dienta_market = MarketPlaceAdder.addMarketplace("independent", dienta_1, null,
        "Azura",
        6,
        new ArrayList<String>(
                Arrays.asList(Conditions.POPULATION_6,
                Conditions.ORE_ULTRARICH,
                Conditions.RARE_ORE_MODERATE,
                Conditions.HABITABLE,
                Conditions.MILD_CLIMATE,
                Conditions.WATER_SURFACE,
                Conditions.REGIONAL_CAPITAL,
                Conditions.HABITABLE,
                Conditions.ORGANICS_TRACE,
                Conditions.AI_CORE_ADMIN)
        ),
            new ArrayList<String>(
                 Arrays.asList(
                Submarkets.GENERIC_MILITARY,
                Submarkets.SUBMARKET_OPEN,
                Submarkets.SUBMARKET_STORAGE,
                Submarkets.SUBMARKET_BLACK)
                    ),
            new ArrayList<String>(Arrays.asList(
                Industries.POPULATION,
                Industries.MEGAPORT,
                Industries.MINING,
                Industries.STARFORTRESS,
                Industries.HEAVYBATTERIES,
                Industries.HIGHCOMMAND,
                Industries.WAYSTATION,
                //note: I don't think I have aquaculture? that was from indoevo right. probably you want to check if the player has that before trying to add it
                //Industries.AQUACULTURE,
                Industries.ORBITALWORKS)
                ),
            true,false);

        dienta_market.addIndustry(Industries.ORBITALWORKS, Collections.singletonList(Items.PRISTINE_NANOFORGE));

        SectorEntityToken azura_relay = system.addCustomEntity("leg_azura_relay", // unique id
                "Azura Relay", // name - if null, defaultName from custom_entities.json will be used
                "comm_relay", // type of object, defined in custom_entities.json
                "independent"); // faction
        azura_relay.setCircularOrbitPointingDown( dienta_Star, 30f, 5900, 441);
        
        SectorEntityToken azura_buoy = system.addCustomEntity("leg_azura_buoy", // unique id
                "Azura Buoy", // name - if null, defaultName from custom_entities.json will be used
                "nav_buoy", // type of object, defined in custom_entities.json
                "independent"); // faction
        azura_buoy.setCircularOrbitPointingDown( dienta_Star, 210f, 5900, 441);

        JumpPointAPI jumpPoint3 = Global.getFactory().createJumpPoint(
        "fringe_jump",
        "Fringe System Jump");

        //note: ...why wasn't the focus set at the star?
        jumpPoint3.setCircularOrbit(dienta_Star, 2, jumpFringeDist, 4000f);
        jumpPoint3.setStandardWormholeToHyperspaceVisual();

        system.addEntity(jumpPoint3);

        cleanup(system);

    }

        private void cleanup(StarSystemAPI system){
            HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
            NebulaEditor editor = new NebulaEditor(plugin);
            float minRadius = plugin.getTileSize() * 2f;

            float radius = system.getMaxRadiusInHyperspace();
            editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius * 0.5f, 0f, 360f);
            editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0f, 360f, 0.25f);
        }
}
