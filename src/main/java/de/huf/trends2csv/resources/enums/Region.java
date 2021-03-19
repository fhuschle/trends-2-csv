package de.huf.trends2csv.resources.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Region implements CountryRegion {
    THUERINGEN("DE-TH", "Th\u00fcringen"),
    SCHLESWIG_HOLSTEIN("DE-SH", "Schleswig-Holstein"),
    BAYERN("DE-BY", "Bayern"),
    NIEDERSACHSEN("DE-NI", "Niedersachsen"),
    SACHSEN("DE-SN", "Sachsen"),
    MECKLENBURG_VORPOMMERN("DE-MV", "Mecklenburg-Vorpommern"),
    BADEN_WUERTENBERG("DE-BW", "Baden-W\u00fcrttemberg"),
    NORDRHEIN_WESTFALEN("DE-NW", "Nordrhein-Westfalen"),
    HAMBURG("DE-HH", "Hamburg"),
    BRANDENBURG("DE-BB", "Brandenburg"),
    RHEINLAND_PFALZ("DE-RP", "Rheinland-Pfalz"),
    SAARLAND("DE-SL", "Saarland"),
    BREMEN("DE-HB", "Bremen"),
    SACHSEN_ANHALT("DE-ST", "Sachsen-Anhalt"),
    HESSEN("DE-HE", "Hessen");

    private final String code;
    private final String name;
}
