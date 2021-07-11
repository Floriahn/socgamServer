package com.socgam.server.model.CombatSystem;

public class StandardValues {
    public static int getStandardAttributeValue(CharacterClasses charClass, Attributes attribute) {
        switch(charClass) {
            case MONK:
                switch(attribute){
                    case VITALITY:          return 100;
                    case ARMOR:             return 0;
                    case ABSORPTION:        return 0;
                    case PHYSICALDAMAGE:    return 6;
                    case MAGICALDAMAGE:     return 4;
                    case ENERGY:            return 10;
                    case ENERGYGAIN:        return 2;
                    case CARDDRAW:          return 2;
                    case REGENERATION:      return 2;
                    default: return 0;
                }
            case BERSERK:
                switch(attribute){
                    case VITALITY:          return 70;
                    case ARMOR:             return 0;
                    case ABSORPTION:        return 0;
                    case PHYSICALDAMAGE:    return 7;
                    case MAGICALDAMAGE:     return 5;
                    case ENERGY:            return 10;
                    case ENERGYGAIN:        return 2;
                    case CARDDRAW:          return 2;
                    case REGENERATION:      return 0;
                    default: return 0;
                }
            case MARTYR:
                switch(attribute){
                    case VITALITY:          return 90;
                    case ARMOR:             return 2;
                    case ABSORPTION:        return 2;
                    case PHYSICALDAMAGE:    return 3;
                    case MAGICALDAMAGE:     return 3;
                    case ENERGY:            return 10;
                    case ENERGYGAIN:        return 2;
                    case CARDDRAW:          return 2;
                    case REGENERATION:      return 0;
                    default: return 0;
                }
            case CLERIC:
                switch(attribute){
                    case VITALITY:          return 120;
                    case ARMOR:             return 2;
                    case ABSORPTION:        return 3;
                    case PHYSICALDAMAGE:    return 2;
                    case MAGICALDAMAGE:     return 4;
                    case ENERGY:            return 10;
                    case ENERGYGAIN:        return 2;
                    case CARDDRAW:          return 2;
                    case REGENERATION:      return 0;
                    default: return 0;
                }
            case ARCANIST:
                switch(attribute){
                    case VITALITY:          return 100;
                    case ARMOR:             return 0;
                    case ABSORPTION:        return 1;
                    case PHYSICALDAMAGE:    return 4;
                    case MAGICALDAMAGE:     return 6;
                    case ENERGY:            return 12;
                    case ENERGYGAIN:        return 3;
                    case CARDDRAW:          return 2;
                    case REGENERATION:      return 0;
                    default: return 0;
                }
            case DRUID:
                switch(attribute){
                    case VITALITY:          return 100;
                    case ARMOR:             return 0;
                    case ABSORPTION:        return 0;
                    case PHYSICALDAMAGE:    return 4;
                    case MAGICALDAMAGE:     return 4;
                    case ENERGY:            return 12;
                    case ENERGYGAIN:        return 3;
                    case CARDDRAW:          return 2;
                    case REGENERATION:      return 2;
                    default: return 0;
                }

            default:
                return 0;
        }
    }
}
