package it.dnd.game_elements_service.entity.enumerate;

/**
 * Tipo di lingua associata a una razza tramite {@link it.dnd.game_elements_service.entity.relation.RaceLanguage}.
 *
 * AUTOMATIC:
 *   La lingua è acquisita gratuitamente al 1° livello, senza spendere punti abilità.
 *   Es. per un Elfo: Common e Elvish sono automatiche.
 *   Tutti i personaggi della razza la parlano per default.
 *
 * BONUS:
 *   La lingua è nel "menu" delle scelte disponibili per la razza.
 *   Il personaggio può apprenderla investendo punti in "Speak Language"
 *   (che costa 1 punto skill per lingua aggiuntiva).
 *   Es. per un Umano: può scegliere tra Dwarven, Elvish, Orc, Goblin, ecc.
 *   come lingue bonus in base alla sua INT.
 *
 * NOTA: i personaggi con INT alta ottengono lingue bonus extra.
 * La formula è: lingue bonus = max(0, modificatore_INT).
 * Il service deve usare questo enum per distinguere quali lingue proporre come scelta.
 */
public enum RaceLanguageType {
    AUTOMATIC,
    BONUS
}