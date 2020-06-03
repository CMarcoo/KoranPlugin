/*
 * KoranPlugin - A Minecraft plugin to interact with the Koran
 * Copyright (C) 2020 TheViperShow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.thevipershow.koranplugin.factory;

import com.google.gson.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import me.thevipershow.koranplugin.structure.*;

public final class GSONKoranDeserializer extends AbstractKoranDeserializer {
    private static GSONKoranDeserializer instance = null;
    private final Gson gson;

    private GSONKoranDeserializer() {
        JsonDeserializer<Koran> koranDeserializer = ((json, typeOfT, context) -> {
            JsonArray surahArray = json.getAsJsonArray();
            List<Surah> suwar = new ArrayList<>();
            List<Aja> ajat = new ArrayList<>();
            int startSurahNumber = 1;
            for (final JsonElement jsonElement : surahArray) {
                JsonObject surahObj = jsonElement.getAsJsonObject();
                Aja aja = new Aja(surahObj.get("text").getAsString());
                int surahNumber = surahObj.get("surah_number").getAsInt();
                if (surahNumber == startSurahNumber) {
                    ajat.add(aja);
                } else {
                    suwar.add(new Surah(ajat));
                    ajat.clear();
                    startSurahNumber++;
                }
            }
            return new KoranImplementation(suwar);
        });
        this.gson = new GsonBuilder().registerTypeAdapter(Koran.class, koranDeserializer).create();
    }

    public static GSONKoranDeserializer getInstance() {
        return instance != null ? instance : (instance = new GSONKoranDeserializer());
    }

    @Override
    public CompletableFuture<Koran> createKoran(String jsonText, LANG lang) {
        return CompletableFuture.supplyAsync(
                () -> {
                    Koran koran = gson.fromJson(jsonText, Koran.class);
                    koran.setLanguage(lang);
                    return koran;
                }
        );
    }
}
