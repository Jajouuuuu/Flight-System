# Flight-System ✈️

**Flight-System** est une plateforme de gestion de vols découpée en micro-services Spring Boot (Java 17). Elle couvre tout le cycle de vie d'un billet : recherche, réservation, paiement, comptabilité, inventaire, check-in et reporting.

---

## Sommaire

1. [Architecture](#architecture)
2. [Liste des micro-services](#liste-des-micro-services)
3. [Prérequis](#prerequis)
4. [Mise en route rapide](#mise-en-route-rapide)
   1. [Compilation du projet](#1--compiler-le-projet)
   2. [Démarrage de l'infrastructure (Kafka + Config / Discovery / Gateway)](#2--demarrer-linfrastructure-kafka--config--discovery--gateway)
   3. [Démarrage des micro-services fonctionnels](#3--demarrer-les-micro-services-metier)
5. [Bases de données H2](#bases-de-donnees-h2)
6. [Collections Postman](#collections-postman)
   1. [Mock Data Seeder](#mock-data-seeder)
   2. [Use Case : Booking a Flight](#use-case--booking-a-flight)
7. [Tests & CI](#tests--ci)
8. [Dépannage](#depannage)

---

## Architecture

![Diagramme d'architecture](suivi-projet/archi-flight-system.png)

*API Gateway* → **Services Métier** → *Kafka* pour la communication asynchrone. Tous les services s'enregistrent auprès du *Discovery Server* (Eureka) et récupèrent leur configuration depuis le *Config Server*.

## Liste des micro-services

| Module | Port par défaut | Rôle principal |
|--------|-----------------|---------------|
| configuration | 8888 | Centralisation de la configuration (Spring Cloud Config)
| discovery | 8761 | Service Registry (Eureka)
| gateway | 8080 | Point d'entrée unique (routing + circuit-breaker)
| accounting-service | 8200 | Transactions comptables & reporting de revenus
| booking-service | 8201 | Gestion des réservations
| check-in-service | 8202 | Enregistrement & cartes d'embarquement
| customer-service | 8203 | Gestion des clients
| customer-profile-service | 8204 | Agrégation de données client
| flight-service | 8205 | Vols, avions, routes
| inventory-service | 8206 | Stocks de sièges
| payment-service | 8207 | Paiements & remboursements
| reservation-service | 8208 | Tenue des dossiers passagers
| revenue-service | 8209 | Agrégat de revenus (lecture seule)
| search-service | 8210 | Recherche de vols (agrégat)

> Les ports sont configurables dans `src/main/resources/application.yml` de chaque service.

## Prérequis

* Java 17 JDK
* Maven 3.6 +
* Kafka 3.9.1 (+ ZooKeeper) – ou toute autre distro compatible
* (Linux) **Gnome-Terminal** installé (utilisé par les scripts pour ouvrir de nouveaux onglets)

```bash
# vérifier les versions
java -version
mvn -version
```

## Mise en route rapide

### 1.  Compiler le projet

```bash
cd Flight-System
mvn clean install -DskipTests
```

### 2.  Démarrer l'infrastructure (Kafka + Config / Discovery / Gateway)

Sous **Linux** :

```bash
chmod +x run-app-microservices/linux/*.sh
./run-app-microservices/linux/config-servers.sh
```

Ce script :

1. lance ZooKeeper puis Kafka (répertoire configurable via `KAFKA_HOME` en haut du script) ;
2. ouvre trois nouveaux onglets Terminal et démarre **configuration**, **discovery** et **gateway**.

Sous **macOS** (rendez les scripts exécutables) :

```bash
chmod +x run-app-microservices/macos/*.sh
./run-app-microservices/macos/core-app.sh   # démarre tous les services (voir étape suivante)
```

### 3.  Démarrer les micro-services métier

Toujours sous Linux :

```bash
./run-app-microservices/linux/core-app.sh
```

Chaque service est lancé dans un nouvel onglet Gnome Terminal avec la commande :

```bash
cd <service> && mvn spring-boot:run
```

> Si vous préférez un lancement manuel :
>
> ```bash
> cd booking-service
> mvn spring-boot:run
> ```

---

## Bases de données H2

* Toutes les bases sont en **mémoire** (`jdbc:h2:mem:<service>`). Elles sont donc ré-initialisées à chaque redémarrage.
* Accès à la console H2 : `http://localhost:<port>/h2-console` (login : `sa`, mot de passe vide).

Pour repartir d'un état propre il suffit donc de **redémarrer** le service concerné.

---

## Collections Postman

Les collections se trouvent à la racine du projet :

* `accounting_collection.json`, `booking_collection.json`, … (une par micro-service).
* `mock_data_collection.json` – injecte un jeu de données complet (3 clients, 2 vols, etc.).
* `use_case_booking_collection.json` – workflow *de la recherche au boarding pass*.

### Mock Data Seeder

1. Importez `mock_data_collection.json` dans Postman.
2. Exécutez la collection : cela crée les clients, vols, inventaires, réservations et paiements.

### Use Case : Booking a Flight

1. Importez `use_case_booking_collection.json`.
2. Sélectionnez (ou créez) un environnement `Flight-System` avec la variable : `baseUrl = http://localhost:8080`.
3. Cliquez sur **Run collection** : Postman enchaîne 12 requêtes et affiche la carte d'embarquement finale.

---

## Tests & CI

```bash
mvn test            # lance les tests unitaires
mvn verify          # + tests d'intégration
```

Une pipeline (GitHub Actions) peut être ajoutée pour :

* Build + tests
* Analyse Sonar / Checkstyle
* Publication des images Docker

---

## Dépannage

| Problème | Solution |
|----------|----------|
| `cd .../gateway: No such file or directory` | Vérifiez que `PROJECT_ROOT` pointe bien vers **Flight-System**. Les scripts ont déjà été corrigés (`/../..`). |
| Port déjà utilisé | Modifier le port dans `application.yml` ou tuer le processus occupant le port (`lsof -i :8080`). |
| Kafka ne démarre pas | Mettez à jour `KAFKA_HOME` et assurez-vous que `bin/zookeeper-server-start.sh` & `bin/kafka-server-start.sh` existent. |
| Erreur de compilation `findByBookingNumber` | Vérifiez que les méthodes existent dans `AccountTransactionRepository` et exécutez `mvn clean`. |

---

> *Bon vol !* 🚀
