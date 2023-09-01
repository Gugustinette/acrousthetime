# ACrousTheTime

## Description
Projet de fin d'année LP MiAR

### Technologies
- Serveur : NestJS (Node, TypeScript)
- Client Web : Next (Node, TypeScript)
- Client Mobile : ? (à définir)
- Base de donnée : MariaDB

## Sujet d'origine
Le logiciel d'emploi du temps permet de consulter les emplois du temps au format ics pour un prof (https://edt.univ-nantes.fr/iut_nantes_pers/s360071.ics), une salle (https://edt.univ-nantes.fr/iut_nantes_pers/r89806.ics), un groupe d'étudiant (https://edt.univ-nantes.fr/iut_nantes/g3546.ics), ... Cependant, il manque des fonctionnalités comme connaître les salles libres, demander la réservation d'une salle, connaître la configuration des machines d'une salle, sa capacité, ... Le projet consiste à proposer des solutions pour pallier le manque de fonctionnalités

## Lancement
Après le lancement, les services sont accessibles aux adresses suivantes :
- Serveur : http://localhost:10000
    - Documentation de l'API (Swagger) : http://localhost:10000/documentation (ou en production : https://api.acrousthetime.gugustinette.com/documentation)
- Client Web : http://localhost:10001
- (Uniquement en dev) Interface d'administration de la base de donnée : http://localhost:10002
    - Identifiants :
        - System : `MySQL`
        - Serveur : `db`
        - Utilisateur : `gusmetal`
        - Mot de passe : `gusmetal`

### En développement
- Lancer le projet
```
docker compose -f ./docker/dev/docker-compose.yml up -d
```
- Fermer le projet
```
docker compose -f ./docker/dev/docker-compose.yml down
```

### En production
- Lancer le projet
```
docker compose -f ./docker/prod/docker-compose.yml up -d
```
- Fermer le projet
```
docker compose -f ./docker/prod/docker-compose.yml down
```

### Batch
- Lancer le batch d'import des ICS
```
docker compose -f ./docker/batch/docker-compose.yml up --build
```

## Déploiement
Le déploiement nécessite 3 technologies importantes :
- Docker (https://docs.docker.com/engine/install/), qui permet la gestion des conteneurs et des images applicatives
- Nginx (https://www.nginx.com/), qui sert de reverse proxy pour rediriger les requêtes vers les conteneurs
- Certbot (https://certbot.eff.org/), qui permet de générer des certificats SSL pour sécuriser les connexions
    ```
    sudo apt install certbot
    ```

### Déprécié
- Gitlab Runner (https://docs.gitlab.com/runner/install/), qui permet de lancer des scripts de test et de déploiement à chaque push sur la branche main
    ```
    sudo apt-get install gitlab-runner
    ```
    - Configurer le runner de déploiement
        ```
        sudo gitlab-runner register
        ```
        - URL du serveur Gitlab : https://gitlab.univ-nantes.fr/
        - Token : `Settings > CI/CD > Runners > Projet runners > Set up a project runner for a project`
        - Tags : `gus`
        - Executor : `shell`
        - Nom/Description : `Gus Runner for Gus Metal`
    - Configurer le runner de test
        ```
        sudo gitlab-runner register
        ```
        - URL du serveur Gitlab : https://gitlab.univ-nantes.fr/
        - Token : `Settings > CI/CD > Runners > Projet runners > Set up a project runner for a project`
        - Tags : `gusdocker`
        - Executor : `docker`
        - Nom/Description : `Gus Runner for running Docker configurations`
    - Lancez les runners
        ```
        sudo gitlab-runner start
        ```

### Scénario de déploiement
- /!\ Seulement la première fois /!\
    - Lancer le script `./deploy/script/deploy.sh`
        ```
        chmod +x ./deploy/scripts/deploy.sh
        ./deploy/scripts/deploy.sh
        ```
    - Récupérer les certificats SSL
        ```
        sudo certbot --nginx -d acrousthetime.gugustinette.com -d api.acrousthetime.gugustinette.com
        ```
- Lancer les conteneurs (devrait être fait automatiquement par le runner si correctement configuré)
    ```
    docker compose -f ./docker/prod/docker-compose.yml up -d
    ```
