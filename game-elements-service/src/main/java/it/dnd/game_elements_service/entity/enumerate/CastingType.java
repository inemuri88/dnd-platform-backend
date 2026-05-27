package it.dnd.game_elements_service.entity.enumerate;

/**
 * Modalità di lancio degli incantesimi di una {@link it.dnd.game_elements_service.entity.ClassCharacter}.
 *
 * PREPARED (caster preparato):
 *   Il personaggio deve scegliere ogni mattina quali spell memorizzare negli slot
 *   disponibili (consultando il suo spellbook per il Wizard, oppure pregando
 *   per Cleric e Druid). Non può cambiare la scelta fino al giorno successivo.
 *   → Classi: Wizard, Cleric, Druid, Paladin (dal 4°), Ranger (dal 4°)
 *   → SpellsKnownRow non viene usata (conosce tutti gli spell della lista/spellbook)
 *
 * SPONTANEOUS (caster spontaneo):
 *   Il personaggio conosce un numero fisso di spell (SpellsKnownRow) e può
 *   scegliere quale lanciare al momento, finché ha slot disponibili.
 *   Non ha bisogno di prepararsi al mattino.
 *   → Classi: Sorcerer, Bard
 *   → SpellsKnownRow viene popolata
 *
 * USO: ClassCharacter.castingType
 */
public enum CastingType {
    PREPARED,
    SPONTANEOUS
}