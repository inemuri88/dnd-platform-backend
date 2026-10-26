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

## 🤖 Sviluppo e percorso formativo assistiti da AI

Il progetto viene sviluppato con il supporto sia di **Claude Code** sia di **OpenAI Codex**.
Gli agenti operano come collaboratori tecnici, senior mentor e reviewer, in base al tipo di
attività.

Vengono impiegati soprattutto per:

- analizzare requisiti e codice esistente;
- discutere alternative architetturali e relativi compromessi;
- assegnare micro-task formativi e fornire indizi progressivi;
- effettuare code review e aiutare nella diagnosi di errori, log e query;
- implementare modifiche circoscritte quando vengono delegate esplicitamente;
- verificare build, comportamento osservabile e criteri di accettazione.

Lo sviluppatore mantiene la responsabilità delle decisioni e dell'apprendimento. Nei task
formativi scrive normalmente l'implementazione e deve saper spiegare flusso, responsabilità,
rischi e verifiche. Un agente può produrre direttamente codice soltanto quando la delega è
esplicita e limitata a uno specifico perimetro.

Il lavoro procede attraverso un solo micro-passo alla volta. I task non vengono considerati
conclusi finché i criteri di accettazione non sono stati realmente verificati; particolare
attenzione viene dedicata a configurazione Spring, persistenza JPA, transazioni, API REST,
sicurezza e debugging sistematico.

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

### Configurazione runtime di game-elements-service

Il servizio usa due profili Spring:

- `local`: valori di riserva per host, porta e nome del database, porta HTTP `8082` ed Eureka
  disabilitato;
- `prod`: host, porte e nome del database devono essere forniti esplicitamente dall'ambiente.

Username e password non hanno valori di riserva. I nomi delle variabili disponibili sono
documentati in `game-elements-service/.env.example`; il file `.env` reale è locale e ignorato da
Git. Spring Boot non carica automaticamente `.env`: deve essere la shell, IntelliJ o il futuro
Docker Compose a esportare le variabili nel processo Java.

#### Avvio da PowerShell

Dalla directory `game-elements-service`:

```powershell
$env:SPRING_PROFILES_ACTIVE='local'
$env:GAME_ELEMENTS_DB_USER='utente-locale'
$env:GAME_ELEMENTS_DB_PASSWORD='password-locale'
.\mvnw.cmd spring-boot:run
```

I valori impostati in questo modo valgono soltanto nella sessione PowerShell corrente e nei
processi avviati da essa.

#### Avvio da IntelliJ IDEA

Nella Run Configuration di `GameElementsServiceApplication` aggiungere le environment variables
`SPRING_PROFILES_ACTIVE=local`, `GAME_ELEMENTS_DB_USER` e `GAME_ELEMENTS_DB_PASSWORD`. Le altre
variabili `GAME_ELEMENTS_*` possono essere omesse per usare i valori locali di riserva descritti in
`.env.example`.

#### Futuro avvio con Docker Compose

Il repository non contiene ancora un file Docker Compose. Quando verrà introdotto, sarà Compose a
leggere `.env` o un file indicato con `env_file` e a passare le variabili al container. La sola
copia di `.env.example` in `.env` non rende i valori disponibili a Spring Boot.

La configurazione non è ancora conclusa: GES-002 resta in corso finché non saranno verificati il
fail-fast sulle credenziali mancanti, l'avvio con valori validi e i relativi smoke check manuali.

## 🚧 Stato del progetto

In sviluppo attivo — fase di implementazione del layer entity e della struttura base dei microservizi.
