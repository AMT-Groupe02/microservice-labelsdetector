# 🐜 LabelsDetector microservice
Ce micro-service met à disposition des routes http permettant de faire la détection d'image.

Les étapes ci-dessous sont à faire depuis le dossier racine du projet.

## 👫 Collaborateurs
Développeurs : Luca Coduri & Chloé Fontaine

Professeur : Nicolas Glassey

Assistant : Adrien Allemand

## 🚧 Prérequis
Vous devez avoir au minimum Java 17 et Maven installés sur votre machine ou alors docker, car ce projet utilise Spring.
Il est aussi nécessaire de créer un fichier .env à la racine. Vous retrouverez un exemple de son contenu dans le fichier .env.example

Nous utilisons AWS dans la version actuelle, il faut donc fournir vos identifiants AWS (access_token, secret_token).

## Récupérer les dépendances
```
./mvnw dependency:resolve
```

## Compilation
La commande ci-dessous permet de compiler le projet sans lancer les tests unitaires.
```bash
 ./mvnw package -Dmaven.test.skip
```
## Run
Une fois le projet compilé il est possible de lancer le programme avec la commande suivante :
```bash
java -jar /app/target/microservice-labelsdetector*.jar #Remplacez l'étoile par le numéro de version.
```

## Tests
Pour lancer les tests, il faut faire la commande suivante :
```bash
./mvnw test
```

## 🐳 Docker

### Tests
```bash
docker build --build-arg ACCESS_KEY_ARG=YOUR_KEY --build-arg SECRET_KEY_ARG=YOUR_KEY -t dataobject-test:latest --target test .
```
Les tests sont lancés durant l'étape de build, ce qui signifie que le build échoue si les tests échouent.

`⚠ L'image résultante contient le fichier .env avec vos credentials, ne partagez donc pas cette image avec n'importe qui.`

### development
```bash
docker build -t dataobject-development:latest --target development .
docker run -p 8080:8080 dataobject-development:latest
```

Lors du build de cette image le fichier .env se trouvant à la racine du projet est copié,
veillez donc à ce qu'il soit existant et complet.

`⚠ L'image résultante contient le fichier .env avec vos credentials, ne partagez donc pas cette image avec n'importe qui.`

### Production
```bash
docker build -t dataobject-production:latest --target production .
docker run -p 8080:8080 dataobject-production:latest
```

L'image construite ne contient pas le fichier .env, il est donc important de ne pas oublier de le rajouter à la racine de l'image.

## 🏚 Structure du projet
```
./src/
├── main
│ ├── java.com.groupe2
│ │   └── microservicelabelsdetector
│ │       ├── controller --> contient les controllers
│ │       ├── labelsdetector --> contient la classe qui permet de gérer la detection de labels.
│ │       │ └── aws --> L'implémentation du code pour aws.
│ │       └── domain
│ │           ├── requestbody --> Classe représentant les requêtes http
│ │           └── responsebody --> Classe représentant les réponses http
│ └── resources --> Les ressources Spring
└── test.java.com.groupe2
                └── microservicelabelsdetector
                    └── dataobject --> Test sur la detection d'image.
                        └── aws --> Test plus spécifique à aws
```

## API Endpoints

## End-point: Detect labels
### Method: POST
>```
>http://localhost:8080/v1/detect/image
>```
### Body (**raw**)

```json
{
  "url":"URL TO IMAGE",
  "maxLabels": 2,
  "minConfidence": 70
}
```