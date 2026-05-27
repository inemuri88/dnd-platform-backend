package it.dnd.game_elements_service.entity.enumerate;

/**
 * Progressione dei Tiri Salvezza (Saving Throws) per livello di classe.
 *
 * In D&D 3.5 ogni classe ha tre tiri salvezza (Fortitude, Reflex, Will),
 * ciascuno con una progressione GOOD o POOR:
 *
 *   SAVE_GOOD → formula: 2 + livello/2  (arrotondato per difetto)
 *               Es. livello 1 = +2, livello 5 = +4, livello 20 = +12
 *
 *   SAVE_POOR → formula: livello/3  (arrotondato per difetto)
 *               Es. livello 1 = +0, livello 5 = +1, livello 20 = +6
 *
 * ESEMPIO per il Fighter (BAB_GOOD, FORT_GOOD, REF_POOR, WILL_POOR):
 *   Al 5° livello: Fort +4, Ref +1, Will +1
 *
 * ESEMPIO per il Wizard (BAB_POOR, FORT_POOR, REF_POOR, WILL_GOOD):
 *   Al 5° livello: Fort +1, Ref +1, Will +4
 *
 * Il calcolo del valore effettivo avviene nel service, qui si salva solo il tipo.
 *
 * USO: ClassCharacter.fortSaveProgression, .refSaveProgression, .willSaveProgression
 */
public enum BonusSavingThrows {
    SAVE_GOOD,
    SAVE_POOR
}