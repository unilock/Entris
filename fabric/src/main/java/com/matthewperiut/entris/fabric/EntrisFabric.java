package com.matthewperiut.entris.fabric;

import com.matthewperiut.entris.Entris;
import net.fabricmc.api.ModInitializer;

public class EntrisFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Entris.init();
    }
}
