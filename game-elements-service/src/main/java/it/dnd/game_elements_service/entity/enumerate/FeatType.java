package it.dnd.game_elements_service.entity.enumerate;

/**
 * Categoria di un Feat in D&D 3.5.
 *
 * La categoria determina chi può selezionarlo come "bonus feat":
 *   FIGHTER      → il Fighter lo ottiene come bonus feat ogni 2 livelli
 *   METAMAGIC    → modifica il livello e gli slot degli incantesimi
 *   ITEM_CREATION→ permette di creare oggetti magici (bacchette, pergamene, ecc.)
 *   DIVINE       → accessibile solo a classi divine (Cleric, Paladin, ecc.)
 *   RACIAL       → riservato a una specifica razza (vedi prerequisito RACE in FeatPrerequisite)
 *   GENERAL      → nessuna restrizione di classe/razza
 *   SPECIAL      → casi particolari (psionic, reserve feat, ecc.)
 */
public enum FeatType {
    GENERAL,
    FIGHTER,
    METAMAGIC,
    ITEM_CREATION,
    DIVINE,
    RACIAL,
    SPECIAL
}