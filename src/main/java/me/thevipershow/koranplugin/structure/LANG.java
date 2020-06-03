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

package me.thevipershow.koranplugin.structure;

public enum LANG {
    ENGLISH("https://gist.githubusercontent.com/TheViperShow/d75245882ef00d47bf7838306038c96c/raw/916ab4c152da433f67a8fe6062a4c66f61395a89/KORAN_EN.json", "english"),
    ARABIC("https://gist.githubusercontent.com/TheViperShow/107fafc03abaea24edca3113b03afe62/raw/4aa132f1aa8967b4127ea9b367c1168801305723/KORAN_ARABIC.json", "arabic");

    private final String url, abbrev;

    LANG(String url, String abbrev) {
        this.url = url;
        this.abbrev = abbrev;
    }

    public String getUrl() {
        return url;
    }

    public String getAbbrev() {
        return abbrev;
    }
}
