package data.scripts;


import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import exerelin.campaign.SectorManager;

import java.util.List;
import java.util.Map;

public class ModPluginName extends BaseModPlugin {

    private static org.apache.log4j.Logger log = Global.getLogger(ModPluginName.class);

    @Override
    public void onNewGame() {
        Map<String, Object> data = Global.getSector().getPersistentData();
        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
        if (!haveNexerelin || SectorManager.getManager().isCorvusMode()) {
            new legionarry_System().generate(Global.getSector());
            data.put("legionarry_System_generated", "-");
        }
    }

    @Override
    public void onGameLoad(boolean wasEnabledBefore) {
        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");

        Map<String, Object> data = Global.getSector().getPersistentData();
        StarSystemAPI dienta_sys=Global.getSector().getStarSystem("Dienta");

        if (!haveNexerelin || SectorManager.getManager().isCorvusMode()) {
            if (!data.containsKey("legionarry_System_generated")) {
                new legionarry_System().generate(Global.getSector());
                log.info("Generating Dienta.");
                data.put("legionarry_System_generated", "-");
            }
        }

        if (dienta_sys!=null)
        {
            exploreAll(dienta_sys);
        }
    }

    static void exploreAll(StarSystemAPI system) {
        List<SectorEntityToken> allEntities = system.getAllEntities();
        for (SectorEntityToken entity : allEntities) {
            if (entity.getMarket()!=null){
                entity.getMarket().setSurveyLevel(MarketAPI.SurveyLevel.FULL);
            }
        }
    }
}