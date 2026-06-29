package it.dnd.game_elements_service.repository;

import it.dnd.game_elements_service.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository per l'entity {@link Race}: è il layer che parla col database.
 *
 * COME FUNZIONA (per chi parte da zero):
 * Non scrivi NESSUNA implementazione. Dichiari solo questa interfaccia ed
 * estendi JpaRepository: Spring Data JPA, all'avvio, genera per te la classe
 * concreta con tutti i metodi CRUD già pronti. È lo stesso principio del mapper
 * MapStruct (tu dichiari, il framework implementa), ma qui a generare è Spring.
 *
 * I DUE PARAMETRI DI JpaRepository<Race, Long>:
 *   - Race → l'entity gestita da questo repository
 *   - Long → il tipo della sua chiave primaria (il campo @Id, qui Race.id)
 *
 * METODI EREDITATI GRATIS (i più usati):
 *   save(race)        → INSERT o UPDATE
 *   findById(id)      → SELECT per id, restituisce Optional<Race>
 *   findAll()         → SELECT di tutte le razze
 *   deleteById(id)    → DELETE
 *   existsById(id)    → true/false
 * ...e molti altri. Non devi scriverli.
 *
 * QUERY DERIVATE (i due metodi qui sotto):
 * Spring Data sa generare query leggendo il NOME del metodo. Non scrivi SQL:
 * il nome stesso descrive la query. È utile per ricerche semplici.
 *
 * NESSUNA ANNOTAZIONE (perché qui non serve):
 * Questa interfaccia non ha annotazioni, eppure diventa un bean iniettabile.
 * Il motivo: Spring Data JPA, all'avvio, scansiona i package, trova le
 * interfacce che estendono Repository/JpaRepository, crea per ognuna un PROXY
 * (una classe concreta generata a runtime) e lo registra da solo nel container.
 * Quindi NON è un'annotazione a creare il bean: lo fa l'infrastruttura di
 * Spring Data. L'interfaccia in sé non viene mai istanziata; ad essere
 * istanziato e iniettato è il proxy che la implementa.
 *
 * NOTA — differenza col mapper:
 * Sul RaceMapper serviva componentModel = "spring" perché MapStruct genera solo
 * una classe "muta" che, senza @Component, Spring non vedrebbe. Il repository
 * invece ha uno "scopritore" dedicato (Spring Data) che lo registra comunque:
 * per questo qui non serve nessun @Repository / @Component.
 */
public interface RaceRepository extends JpaRepository<Race, Long> {

    /**
     * Cerca una razza per nome, ignorando maiuscole/minuscole.
     *
     * Spring traduce il nome del metodo in: WHERE LOWER(name) = LOWER(?).
     * Restituisce Optional perché la razza potrebbe non esistere: Optional ti
     * costringe a gestire esplicitamente il caso "non trovato" (niente null
     * silenziosi che esplodono dopo con NullPointerException).
     */
    Optional<Race> findByNameIgnoreCase(String name);

    /**
     * Restituisce solo le razze BASE, cioè quelle senza razza madre.
     *
     * Nell'entity Race, parent == null significa "razza base" (es. Elf), mentre
     * parent valorizzato significa "sottoRaza" (es. Wood Elf). Il pezzo di nome
     * "ParentIsNull" viene tradotto da Spring in: WHERE parent_race_id IS NULL.
     * Comodo per un endpoint che elenca le razze principali senza le varianti.
     */
    List<Race> findByParentIsNull();
}