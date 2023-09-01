# lpmiar.2023.208

Application mobile module développement Android

## API

L'API utilisé est celle développé lors du module de Web Serveur.
La documentation de l'API est disponible à l'adresse suivante : [API Meteo Gugustinette](meteo.gugustinette.com/documentation)

## Build
Pour build l'application en ligne de commande, il faut utiliser la commande suivante :
```bash
./gradlew assembleDebug
```
L'APK est ensuite disponible dans le dossier `app/build/outputs/apk/debug/`

## Tests
Pour lancer les tests unitaires en ligne de commande, il faut utiliser la commande suivante :
```bash
./gradlew testDebugUnitTest
```
Pour lancer les tests d'intégration en ligne de commande, il faut utiliser la commande suivante (nécessite un appareil connecté) :
```bash
./gradlew connectedDebugAndroidTest
```
