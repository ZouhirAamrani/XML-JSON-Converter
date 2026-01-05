# 🔄 XML ↔ JSON Converter

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?style=for-the-badge&logo=java)
![Maven](https://img.shields.io/badge/Maven-3.8+-red?style=for-the-badge&logo=apache-maven)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

Une application de bureau élégante et intuitive pour convertir des documents entre les formats XML et JSON, avec prise en charge de deux méthodes de conversion : Native (Java pur) et Jackson (API).

[Fonctionnalités](#-fonctionnalités) • [Installation](#-installation) • [Utilisation](#-utilisation) • [Architecture](#-architecture) • [Captures d'écran](#-captures-décran) • [Contribution](#-contribution)

</div>

---

## 📋 Table des Matières

- [À propos](#-à-propos)
- [Fonctionnalités](#-fonctionnalités)
- [Technologies](#-technologies)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Utilisation](#-utilisation)
- [Architecture](#-architecture)
- [Structure du Projet](#-structure-du-projet)
- [Captures d'écran](#-captures-décran)
- [Exemples](#-exemples)
- [Tests](#-tests)
- [Auteur](#-auteur)

---

## 🎯 À propos

**XML ↔ JSON Converter** est une application de bureau développée en JavaFX qui permet de convertir facilement des documents et du texte entre les formats XML et JSON dans les deux sens.

### Pourquoi ce projet ?

- **Polyvalence** : Deux méthodes de conversion (Native et Jackson) pour répondre à différents besoins
- **Validation** : Validation syntaxique rigoureuse avant toute conversion
- **Intuitivité** : Interface graphique moderne et facile à utiliser
- **Robustesse** : Gestion complète des erreurs avec messages détaillés
- **Flexibilité** : Support des attributs XML, éléments répétés, et structures complexes

---

## ✨ Fonctionnalités

### Conversions

- ✅ **XML → JSON** (Méthode Native et Jackson)
- ✅ **JSON → XML** (Méthode Native et Jackson)

### Méthodes de Conversion

#### 🔧 Méthode Native (Java Pur)
- Parsing manuel avec DOM (XML) et parsing personnalisé (JSON)
- Contrôle total sur le format de sortie
- Aucune dépendance externe
- Gestion fine des cas spéciaux

#### 📚 Méthode Jackson
- Utilisation de la bibliothèque Jackson (Fasterxml)
- Conversion automatisée et optimisée
- Support natif des structures complexes
- Code concis et maintenable

### Fonctionnalités de l'Interface

- 📝 **Saisie manuelle** : Zone de texte pour entrer directement le code
- 📁 **Chargement de fichiers** : Import de fichiers XML/JSON depuis le système
- ✓ **Validation** : Vérification de la syntaxe avant conversion
- 🗑️ **Effacement** : Réinitialisation rapide des zones de texte
- 📋 **Copie** : Copie du résultat dans le presse-papiers
- 💾 **Téléchargement** : Export du résultat converti en fichier
- 🔄 **Navigation intuitive** : Retour facile au menu principal

### Gestion Avancée

- ⚠️ **Messages d'erreur détaillés** : Position exacte et cause de l'erreur
- 🎨 **Feedback visuel** : Indicateurs de statut colorés en temps réel
- 🔒 **Prévention d'erreurs** : Désactivation intelligente des boutons
- 📊 **Formatage automatique** : Pretty-print des résultats

---

## 🛠️ Technologies

### Langage et Framework

| Technologie | Version | Description |
|------------|---------|-------------|
| **Java** | 17 LTS | Langage de programmation principal |
| **JavaFX** | 21 | Framework d'interface graphique |
| **Maven** | 3.8+ | Gestionnaire de dépendances et build |

### Bibliothèques

| Bibliothèque | Version | Utilisation |
|-------------|---------|-------------|
| **Jackson Databind** | 2.16.1 | Traitement JSON |
| **Jackson Dataformat XML** | 2.16.1 | Traitement XML |

### Outils de Développement

- **NetBeans IDE** : Environnement de développement
- **Scene Builder** : Conception visuelle des interfaces FXML
- **Git** : Contrôle de version

---

## 📋 Prérequis

Avant d'installer le projet, assurez-vous d'avoir :

- ☕ **JDK 17** ou supérieur
- 📦 **Maven 3.8** ou supérieur
- 💻 Un système d'exploitation : Windows, macOS, ou Linux

### Vérification des prérequis
```bash
# Vérifier Java
java -version
# Sortie attendue : openjdk version "17.x.x" ou supérieur

# Vérifier Maven
mvn -version
# Sortie attendue : Apache Maven 3.8.x ou supérieur
```

---

## 🚀 Installation

### 1. Cloner le dépôt
```bash
git clone https://github.com/votre-username/xml-json-converter.git
cd xml-json-converter
```

### 2. Compiler le projet
```bash
mvn clean install
```

### 3. Lancer l'application
```bash
mvn javafx:run
```

### Alternative : NetBeans IDE

1. Ouvrez NetBeans
2. File → Open Project
3. Sélectionnez le dossier `xml-json-converter`
4. Clic droit sur le projet → Run

---

## 📖 Utilisation

### Démarrage Rapide

1. **Lancez l'application**
2. **Choisissez le type de conversion** : XML → JSON ou JSON → XML
3. **Sélectionnez la méthode** : Native ou Jackson
4. **Cliquez sur "Continuer"**

### Conversion XML → JSON

#### Méthode 1 : Saisie Manuelle
```xml
<!-- Entrez votre XML dans la zone de texte -->
<?xml version="1.0" encoding="UTF-8"?>
<person>
    <name>Jean Dupont</name>
    <age>30</age>
    <city>Paris</city>
</person>
```

1. Collez ou tapez votre XML
2. Cliquez sur **"Valider XML"**
3. Si valide ✅, cliquez sur **"Convertir"**
4. Le résultat JSON apparaît dans la zone de sortie
5. Utilisez **"Copier JSON"** ou **"Télécharger JSON"**

#### Méthode 2 : Chargement de Fichier

1. Cliquez sur **"📁 Charger Fichier XML"**
2. Sélectionnez votre fichier `.xml`
3. Suivez les mêmes étapes de validation et conversion

### Conversion JSON → XML
```json
{
  "person": {
    "name": "Jean Dupont",
    "age": "30",
    "city": "Paris"
  }
}
```

Le processus est identique, mais avec des fichiers JSON.

### Gestion des Erreurs

#### XML Invalide
```xml
<person>
    <name>Test</person>  <!-- Erreur : balise fermante incorrecte -->
```
➡️ **Message** : "XML mal formé : The element type "name" must be terminated by the matching end-tag "</name>"."

#### JSON Invalide
```json
{
  "name": "Test"
  "age": 25  // Erreur : virgule manquante
}
```
➡️ **Message** : "JSON mal formé : Unexpected character ('\"' (code 34)): was expecting comma to separate Object entries"

---

## 🏗️ Architecture

### Pattern Architectural : MVC (Model-View-Controller)
```
┌─────────────────────────────────────────────────────┐
│                    PRÉSENTATION                      │
│                                                      │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐         │
│  │MainMenu  │  │XmlToJson │  │JsonToXml │         │
│  │.fxml     │  │View.fxml │  │View.fxml │         │
│  └──────────┘  └──────────┘  └──────────┘         │
└───────────────────┬──────────────────────────────────┘
                    │
┌───────────────────┴──────────────────────────────────┐
│                  CONTROLLERS                          │
│                                                       │
│  ┌────────────────┐  ┌──────────────────────┐       │
│  │MainMenu        │  │XmlToJson / JsonToXml │       │
│  │Controller      │  │Controllers           │       │
│  └────────────────┘  └──────────────────────┘       │
└───────────────────┬──────────────────────────────────┘
                    │
┌───────────────────┴──────────────────────────────────┐
│               BUSINESS LOGIC                          │
│                                                       │
│  ┌──────────────┐  ┌─────────────┐  ┌───────────┐  │
│  │  Services    │  │ Validators  │  │   Utils   │  │
│  │              │  │             │  │           │  │
│  │ • Native     │  │ • XML       │  │ • File    │  │
│  │ • Jackson    │  │ • JSON      │  │ • Alert   │  │
│  └──────────────┘  └─────────────┘  └───────────┘  │
└───────────────────┬──────────────────────────────────┘
                    │
┌───────────────────┴──────────────────────────────────┐
│                    MODELS                             │
│                                                       │
│  ┌──────────────────┐  ┌────────────────────┐       │
│  │ConversionConfig  │  │ConversionResult    │       │
│  └──────────────────┘  └────────────────────┘       │
└───────────────────────────────────────────────────────┘
```

### Patterns de Conception Utilisés

#### 1. **Strategy Pattern**

L'interface `IConverter` permet de changer dynamiquement l'algorithme de conversion :
```java
public interface IConverter {
    ConversionResult convert(String input) throws Exception;
}

// Implémentations
- NativeXmlToJsonConverter implements IConverter
- JacksonXmlToJsonConverter implements IConverter
- NativeJsonToXmlConverter implements IConverter
- JacksonJsonToXmlConverter implements IConverter
```

#### 2. **MVC (Model-View-Controller)**

- **Model** : `ConversionConfig`, `ConversionResult`
- **View** : Fichiers FXML (MainMenu, XmlToJsonView, JsonToXmlView)
- **Controller** : Classes Controller pour gérer la logique UI

#### 3. **Facade Pattern**

Les classes `FileHandler` et `AlertHelper` fournissent une interface simplifiée pour des opérations complexes.

---

## 📁 Structure du Projet
```
XMLJSONConverter/
│
├── src/
│   ├── main/
│   │   ├── java/com/converter/
│   │   │   ├── Main.java                          # Point d'entrée
│   │   │   │
│   │   │   ├── controllers/                       # Contrôleurs JavaFX
│   │   │   │   ├── MainMenuController.java
│   │   │   │   ├── XmlToJsonController.java
│   │   │   │   └── JsonToXmlController.java
│   │   │   │
│   │   │   ├── models/                            # Classes de données
│   │   │   │   ├── ConversionConfig.java
│   │   │   │   └── ConversionResult.java
│   │   │   │
│   │   │   ├── services/                          # Logique métier
│   │   │   │   ├── IConverter.java
│   │   │   │   ├── NativeXmlToJsonConverter.java
│   │   │   │   ├── NativeJsonToXmlConverter.java
│   │   │   │   ├── JacksonXmlToJsonConverter.java
│   │   │   │   └── JacksonJsonToXmlConverter.java
│   │   │   │
│   │   │   ├── validators/                        # Validation
│   │   │   │   ├── XmlValidator.java
│   │   │   │   └── JsonValidator.java
│   │   │   │
│   │   │   └── utils/                             # Utilitaires
│   │   │       ├── FileHandler.java
│   │   │       └── AlertHelper.java
│   │   │
│   │   └── resources/
│   │       ├── fxml/                              # Interfaces FXML
│   │       │   ├── MainMenu.fxml
│   │       │   ├── XmlToJsonView.fxml
│   │       │   └── JsonToXmlView.fxml
│   │       │
│   │       └── css/                               # Styles
│   │           └── styles.css
│   │
│   └── test/                                      # Tests unitaires
│       └── java/com/converter/
│           ├── services/
│           └── validators/
├── pom.xml
└── README.md
```

---

## 📸 Captures d'écran

### Menu Principal

![Menu Principal](docs/screenshots/menu-principal.png)

*Interface de sélection du type de conversion et de la méthode*

### Conversion XML → JSON

![XML to JSON](docs/screenshots/xml-to-json.png)

*Interface de conversion XML vers JSON avec validation*

### Conversion JSON → XML

![JSON to XML](docs/screenshots/json-to-xml.png)

*Interface de conversion JSON vers XML avec validation*

### Gestion des Erreurs

![Error Handling](docs/screenshots/error-handling.png)

*Affichage d'erreurs avec messages détaillés*

---

## 💡 Exemples

### Exemple 1 : XML Simple → JSON

#### Entrée XML
```xml
<?xml version="1.0" encoding="UTF-8"?>
<person>
    <name>Marie Martin</name>
    <age>28</age>
    <city>Lyon</city>
</person>
```

#### Sortie JSON
```json
{
  "person": {
    "name": "Marie Martin",
    "age": "28",
    "city": "Lyon"
  }
}
```

### Exemple 2 : XML avec Attributs → JSON

#### Entrée XML
```xml
<?xml version="1.0" encoding="UTF-8"?>
<book id="B001" lang="fr">
    <title>Le Petit Prince</title>
    <author>Antoine de Saint-Exupéry</author>
    <year>1943</year>
</book>
```

#### Sortie JSON
```json
{
  "book": {
    "@id": "B001",
    "@lang": "fr",
    "title": "Le Petit Prince",
    "author": "Antoine de Saint-Exupéry",
    "year": "1943"
  }
}
```

### Exemple 3 : XML avec Éléments Répétés → JSON

#### Entrée XML
```xml
<?xml version="1.0" encoding="UTF-8"?>
<students>
    <student>
        <name>Alice</name>
        <grade>A</grade>
    </student>
    <student>
        <name>Bob</name>
        <grade>B</grade>
    </student>
    <student>
        <name>Charlie</name>
        <grade>A</grade>
    </student>
</students>
```

#### Sortie JSON
```json
{
  "students": {
    "student": [
      {
        "name": "Alice",
        "grade": "A"
      },
      {
        "name": "Bob",
        "grade": "B"
      },
      {
        "name": "Charlie",
        "grade": "A"
      }
    ]
  }
}
```

### Exemple 4 : JSON → XML

#### Entrée JSON
```json
{
  "company": {
    "name": "TechCorp",
    "employees": {
      "employee": [
        {
          "name": "Alice",
          "position": "Developer"
        },
        {
          "name": "Bob",
          "position": "Manager"
        }
      ]
    }
  }
}
```

#### Sortie XML
```xml
<?xml version="1.0" encoding="UTF-8"?>
<root>
  <company>
    <name>TechCorp</name>
    <employees>
      <employee>
        <name>Alice</name>
        <position>Developer</position>
      </employee>
      <employee>
        <name>Bob</name>
        <position>Manager</position>
      </employee>
    </employees>
  </company>
</root>
```
---

## 📝 Roadmap

### Version 1.1 (En cours)
- [ ] Support des namespaces XML
- [ ] Validation avec XSD/DTD
- [ ] Thème sombre
- [ ] Historique des conversions

### Version 1.2 (Planifiée)
- [ ] Batch conversion (plusieurs fichiers)
- [ ] API REST pour conversions
- [ ] Plugin CLI
- [ ] Export en autres formats (YAML, TOML)

### Version 2.0 (Future)
- [ ] Mode comparaison (diff entre XML et JSON)
- [ ] Éditeur avec coloration syntaxique
- [ ] Support des très gros fichiers (streaming)
- [ ] Internationalisation (i18n)

---

## 👨‍💻 Auteur

**[AAMRANI Zouhir]**

- 📧 Email: aamrani.zouhir@gmail.com
- 🐙 GitHub: [@ZouhirAamrani](https://github.com/ZouhirAamrani)
- 💼 LinkedIn: [zouhir-aamrani](https://www.linkedin.com/in/zouhir-aamrani/)

---

## 🙏 Remerciements

- [JavaFX](https://openjfx.io/) - Framework d'interface graphique
- [Jackson](https://github.com/FasterXML/jackson) - Bibliothèque de traitement JSON/XML
- [Scene Builder](https://gluonhq.com/products/scene-builder/) - Outil de conception FXML

---

<div align="center">

**⭐ Si ce projet vous a été utile, n'hésitez pas à lui donner une étoile ! ⭐**

Made with ❤️ and ☕

</div>

---
