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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import me.thevipershow.koranplugin.structure.*;

public final class GSONKoranDeserializer extends AbstractKoranDeserializer {
    private static GSONKoranDeserializer instance = null;
    private final Gson gson;

    private final static JsonDeserializer<Koran> koranDeserializer = ((json, typeOfT, context) -> {
        JsonArray array = json.getAsJsonArray();
        Multimap<Integer, Aja> data = ArrayListMultimap.create(114, 100);
        for (JsonElement jsonElement : array) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            int surahNumber = jsonObject.get("surah_number").getAsInt();
            String aja = jsonObject.get("text").getAsString();
            data.put(surahNumber, new Aja(aja));
        }
        List<Surah> r = new ArrayList<>();
        data.keySet().forEach(i -> r.add(new Surah(data.get(i).parallelStream().collect(Collectors.toList()))));
        return new KoranImplementation(r);
    });

    private GSONKoranDeserializer() {
        this.gson = new GsonBuilder().registerTypeAdapter(Koran.class, koranDeserializer).create();
    }

    public static GSONKoranDeserializer getInstance() {
        return instance != null ? instance : (instance = new GSONKoranDeserializer());
    }

    @Override
    public CompletableFuture<Koran> createKoran(String jsonText, LANG lang) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Koran koran = gson.fromJson(jsonText, Koran.class);
                        koran.setLanguage(lang);
                        return koran;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
        );
    }
}
