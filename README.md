# UML Diagrams Generator

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-4285F4?style=for-the-badge&logo=java&logoColor=white)
![UML](https://img.shields.io/badge/UML-FF6B35?style=for-the-badge&logo=uml&logoColor=white)

##  Description

UML-Diagrams-Generator est un outil automatisé qui utilise la réflexion Java pour analyser et représenter la structure de projets Java sous forme de diagrammes UML. Ce projet vise à automatiser la génération de diagrammes UML en extrayant les classes, interfaces, énumérations et annotations d'un projet Java donné.

##  Fonctionnalités

- **Analyse automatique** : Utilise la réflexion Java pour extraire la structure du code
- **Génération de diagrammes UML** : Création automatique de diagrammes de classes et de packages
- **Interface graphique intuitive** : Interface utilisateur développée avec Java Swing
- **Export multiple** : Sauvegarde en formats XML et XMI
- **Performance optimisée** : Traitement efficace de grands projets Java
- **Sans dépendances externes** : Utilise uniquement les fonctionnalités natives de Java

##  Installation et Configuration

### Prérequis
- Java JDK 8 ou supérieur
- IDE Java (Eclipse, IntelliJ IDEA, NetBeans, etc.)

### Installation
1. Clonez le repository :
```bash
git clone [URL_DU_REPOSITORY]
cd UML-Diagrams-Generator
```

2. Compilez le projet :
```bash
javac -d bin src/org/mql/java/ui/*.java
```

##  Utilisation

### Étape 1 : Lancement de l'application
Exécutez le fichier principal :
```bash
java -cp bin org.mql.java.ui.ProjectUploader
```

### Étape 2 : Upload du projet
1. Cliquez sur le bouton **Upload** dans l'interface
2. Sélectionnez le répertoire contenant votre projet Java
3. L'outil analyse automatiquement la structure du projet

### Étape 3 : Visualisation des résultats
- **Console** : Affiche tous les détails de l'analyse (classes, interfaces, attributs, méthodes)
- **Diagramme de classes** : Visualisation graphique de la structure UML
- **Diagramme de packages** : Vue d'ensemble de l'organisation des packages

### Étape 4 : Export
Deux options d'export sont disponibles :
- **Save XML** : Exporte au format XML standard
- **Save XMI** : Exporte au format XMI (XML Metadata Interchange)

##  Capture d'écrans

### Interface de upload
![Upload Interface](resources/images/upoad.png)
*Interface permettant de sélectionner le projet Java à analyser*

### Sélection de projet
![Project Selection](resources/images/choise.png)
*Dialogue de sélection du répertoire du projet*

### Résultats d'analyse
![Analysis Results](resources/images/console.png)
*Console affichant les résultats détaillés de l'analyse*

### Options de sauvegarde
![Save Options](resources/images/save.png)
*Boutons pour exporter en XML ou XMI*

### Diagramme de packages
![Package Diagram](resources/images/package.png)
*Diagramme UML généré automatiquement*



##  Technologies utilisées

- **Java** : Langage de programmation principal
- **Java Reflection API** : Pour l'analyse dynamique du code
- **Java Swing** : Interface utilisateur graphique
- **XML/XMI** : Formats d'export des diagrammes

##  Développement

### Compétences techniques
- Java
- Java Swing
- Java Reflection
- Langage de modélisation unifié (UML)
- Architecture logicielle

### Fonctionnalités d'analyse
L'outil extrait automatiquement :
- Classes et leurs propriétés
- Interfaces et méthodes
- Relations d'héritage et d'implémentation
- Attributs avec modificateurs d'accès
- Méthodes avec signatures complètes
- Annotations et métadonnées
- Structure des packages

##  Contexte 
- Objectif : Maîtrise de la réflexion Java et des concepts UML
- Approche : Développement sans bibliothèques externes

## 🤝 Contribution

Les contributions sont les bienvenues ! Pour contribuer :

1. Forkez le projet
2. Créez une branche pour votre fonctionnalité (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Poussez vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request


##  Contact

Pour toute question ou suggestion, n'hésitez pas à ouvrir une issue sur GitHub.

---

⭐ Si ce projet vous a aidé, n'hésitez pas à lui donner une étoile !