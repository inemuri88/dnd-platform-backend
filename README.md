# D&D 3.5 Platform

Piattaforma per giocare online alla versione 3.5 di Dungeons & Dragons insieme ad altre persone.
L'obiettivo è offrire: mappe personalizzabili, chat di gruppo e private, gestione delle campagne,
creazione dei personaggi, generatori di NPC casuali e molto altro.

---

## 🛠 Tecnologie

### Backend
| Tecnologia | Versione | Ruolo |
|---|---|---|
| Java | 21 | Linguaggio principale |
| Spring Boot | 3.4.12 | Framework applicativo |
| Spring Cloud | 2024.0.2 | Infrastruttura microservizi |
| Spring Web | — | REST API |
| Spring Data JPA + Hibernate | — | Persistenza dati (ORM) |
| Spring Boot Validation | — | Validazione input |
| Spring Boot Actuator | — | Health check ed endpoint di monitoraggio |
| Spring Boot DevTools | — | Hot reload in sviluppo |
| Spring Security | — | Autenticazione e autorizzazione (Gateway) |
| Spring Cloud Gateway MVC | — | API Gateway (punto d'ingresso unico) |
| Netflix Eureka | — | Service Discovery (registro dei microservizi) |
| Micrometer + Prometheus | — | Metriche e monitoraggio |
| MySQL | — | Database relazionale |
| Lombok | — | Riduzione del boilerplate Java |
| Maven | — | Build e gestione dipendenze |
| Docker | — | Containerizzazione dei servizi |

---

## 🧱 Architettura

Il progetto segue un'**architettura a microservizi** con i seguenti moduli:

| Modulo | Ruolo |
|---|---|
| `gateway` | Punto d'ingresso unico, routing, sicurezza |
| `eureka` | Service Discovery: i servizi si registrano e si scoprono qui |
| `game-elements-service` | Catalogo degli elementi di gioco: razze, classi, spell, feat, oggetti, skill |
| `character-service` | Creazione e gestione dei personaggi |
| `campaign-service` | Gestione delle campagne |
| `chat-service` | Chat di gruppo e private |
| `map-service` | Mappe personalizzabili |
| `user-service` | Gestione degli utenti e dell'autenticazione |

### Struttura interna di ogni servizio
```
controller/   →  REST endpoint
service/      →  logica di business
repository/   →  accesso al DB (Spring Data JPA)
entity/       →  modello JPA (tabelle)
dto/          →  oggetti di trasferimento dati (request/response)
```

### Comunicazione
- I client esterni accedono esclusivamente tramite il **Gateway**
- I microservizi si scoprono tra loro tramite **Eureka**
- La comunicazione inter-servizio avviene via **REST**

---

## 🤖 Sviluppo assistito da AI

Questo progetto viene sviluppato anche come percorso di apprendimento di **Claude Code**
(l'agente AI di Anthropic), utilizzato tramite il plugin per **IntelliJ IDEA**.

L'agente viene impiegato per attività come:
- Completamento e documentazione delle entity JPA
- Brainstorming sulle scelte architetturali
- Revisione del codice

Non tutto il codice è generato dall'agente: la progettazione, le decisioni architetturali
e la scrittura manuale restano parte centrale del processo, con Claude Code usato come
strumento di supporto e apprendimento.

---

## Compilazione di game-elements-service

Questa sezione permette a un nuovo sviluppatore di compilare `game-elements-service`
con la toolchain scelta dal progetto e individuare il JAR prodotto.

### Prerequisiti

- JDK 21, verificabile con:

  ```powershell
  java --version
  ```

### Comando di compilazione

Dalla directory `game-elements-service` eseguire:

  ```powershell
  .\mvnw.cmd clean package -DskipTests
  ```

Al termine, Maven deve mostrare `BUILD SUCCESS`.

Rimanendo nella stessa directory, il JAR eseguibile viene creato in:

  ```text
  target/game-elements-service-0.0.1-SNAPSHOT.jar
  ```

Il file con suffisso `.jar.original` è l'artefatto precedente alla riorganizzazione
eseguita dal plugin Spring Boot.

### Limiti della verifica

Questa procedura verifica la compilazione e la creazione del JAR, ma non avvia
l'applicazione né verifica la connessione al database.

### Configurazione runtime osservata

La configurazione attuale prevede:

- un'istanza MySQL raggiungibile tramite `DB_HOST`, `DB_PORT`, `DB_NAME`,
  `DB_USER` e `DB_PASSWORD`;
- la porta applicativa `8082`.

MySQL può essere eseguito localmente oppure in un container. Docker non è necessario
per la compilazione e il repository non fornisce ancora un file Docker Compose.

## 🚧 Stato del progetto

In sviluppo attivo — fase di implementazione del layer entity e della struttura base dei microservizi.
