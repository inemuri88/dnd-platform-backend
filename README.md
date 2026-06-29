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
| Spring Security + OAuth2 Resource Server | — | Autenticazione/autorizzazione JWT (gateway + tutti i servizi) |
| Flyway | — | Versionamento e migrazioni dello schema DB |
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

## 🔌 Porte dei servizi

| Servizio | Porta | Path esposto dal gateway |
|---|---|---|
| `eureka` | 8761 | — (dashboard su http://localhost:8761) |
| `gateway` | 8080 | punto d'ingresso unico |
| `user-service` | 8081 | `/api/auth/**`, `/api/users/**` |
| `game-elements-service` | 8082 | `/api/game-elements/**` |
| `character-service` | 8083 | `/api/characters/**` |
| `campaign-service` | 8084 | `/api/campaigns/**` |
| `chat-service` | 8085 | `/api/chat/**` |
| `map-service` | 8086 | `/api/maps/**` |

## ⚙️ Configurazione (variabili d'ambiente)

I parametri sensibili o legati all'ambiente vivono in un file **`.env` per ogni servizio** (non versionato,
escluso via `.gitignore`). Negli `application.yaml` sono referenziati come `${VAR}` **senza default**: se il
`.env` non è valorizzato, il servizio non parte (comportamento voluto, niente segreti hardcoded nel codice).

Variabili lette dal `.env`:

| Variabile | Dove | Note |
|---|---|---|
| `DB_HOST` / `DB_PORT` | servizi con DB | Host e porta MySQL |
| `DB_NAME` | servizi con DB | Nome del database |
| `DB_USER` / `DB_PASSWORD` | servizi con DB | Credenziali MySQL |
| `JWT_SECRET` | tutti i servizi + gateway | Chiave HMAC (min 32 byte), **identica ovunque** |

Variabili con default sensato nello `application.yaml` (sovrascrivibili, non obbligatorie nel `.env`):

| Variabile | Default | Note |
|---|---|---|
| `EUREKA_URI` | `http://localhost:8761/eureka/` | URL del registro Eureka |
| `JWT_EXPIRATION` | `3600` | Durata del token di accesso, in secondi (solo user-service) |

> ⚠️ Il `JWT_SECRET` deve essere **identico su tutti i servizi**: lo user-service firma i token con
> quella chiave HMAC e tutti gli altri (gateway compreso) la usano per validarli.
>
> 📄 In ogni servizio è presente un `.env` da compilare (DB e `JWT_SECRET`). Il gateway ha solo `JWT_SECRET`;
> `eureka` non richiede `.env`.

**Ordine di avvio consigliato:** `eureka` → `gateway` → gli altri servizi (in qualunque ordine).

## 🔐 Sicurezza (JWT)

Autenticazione **stateless** basata su JSON Web Token firmati con HMAC (HS256):

1. Il client chiama `POST /api/auth/register` o `POST /api/auth/login` sullo **user-service**.
2. Lo user-service verifica le credenziali (password con hash **BCrypt**) ed emette un **JWT** firmato.
3. Per le richieste successive il client invia l'header `Authorization: Bearer <token>`.
4. Il **gateway** valida il token e inoltra la richiesta; **ogni servizio** lo rivalida a sua volta
   (difesa in profondità / zero-trust interno).

I ruoli viaggiano nel claim `roles` del token e diventano authorities Spring Security (`ROLE_USER`, `ROLE_ADMIN`),
utilizzabili con `@PreAuthorize` grazie a `@EnableMethodSecurity`.

## 🗄️ Migrazioni del database (Flyway)

**Cos'è Flyway.** È uno strumento di *versionamento dello schema del database*: una sorta di "Git per il DB".
Invece di modificare le tabelle a mano (o lasciare che Hibernate le crei/alteri da solo, comportamento
imprevedibile e pericoloso in produzione), lo schema si descrive in **script SQL versionati** che Flyway
applica **in ordine e una sola volta** a ogni avvio.

**Come funziona.**
- Gli script stanno in `src/main/resources/db/migration` e seguono la convenzione di naming
  `V<versione>__<descrizione>.sql` (es. `V1__create_users_table.sql`, `V2__add_avatar_column.sql`).
  Il doppio underscore `__` separa la versione dalla descrizione.
- All'avvio Flyway controlla la tabella di storico `flyway_schema_history`: applica solo le migrazioni
  nuove, nell'ordine di versione, e registra quelle eseguite. Lo stesso script non viene mai rieseguito.
- Una volta applicata, **una migration non si modifica più**: ogni cambiamento è un *nuovo* file
  (`V2`, `V3`, …). Questo rende lo schema riproducibile e identico in ogni ambiente (dev, test, prod).
- Si abbina a `spring.jpa.hibernate.ddl-auto: validate`: Hibernate **non tocca** lo schema, ma all'avvio
  verifica che le entità Java combacino con le tabelle reali (se divergono, l'app non parte → errore subito).

**Stato attuale nel progetto.**
- ✅ **user-service**: Flyway attivo con la migration `V1__create_users_table.sql` + `ddl-auto: validate`.
  È l'esempio completo e funzionante.
- ⏳ **character/campaign/chat/map-service**: Flyway attivo ma senza migration (non hanno ancora entità);
  basterà aggiungere i file `V1__...sql` quando nasceranno le tabelle.
- ⚠️ **game-elements-service**: schema ancora in evoluzione (decine di entità), quindi temporaneamente
  con `ddl-auto: update` e Flyway disattivato. Quando lo schema sarà stabile va generata la baseline
  `V1__baseline.sql` (dal dump del DB già creato da `update`) e si passerà a `validate` + Flyway attivo.

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

## 🚧 Stato del progetto

In sviluppo attivo — fase di implementazione del layer entity e della struttura base dei microservizi.