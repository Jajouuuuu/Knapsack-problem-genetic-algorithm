# Algorithme génétique : problème du sac à dos multidimensionnel

Ce projet implémente un algorithme génétique pour résoudre le problème du sac à dos multidimensionnel (MKP). Le but est de sélectionner un ensemble d'objets d'utilité maximale sous diverses contraintes de coûts.

## Prérequis
- Java Development Kit (JDK)
- Un environnement de développement Java (comme IntelliJ IDEA, Eclipse, etc.)

## Installation

Clonez ce dépôt :

``` 
git clone https://github.com/Jajouuuuu/Knapsack-problem-genetic-algorithm.git
cd algorithme-genetique-mkp
```

Importez le projet dans votre environnement de développement Java préféré.

## Utilisation

### Génération des fichiers CSV pour l'analyse

Pour tester notre code d'analyse avec de nouvelles instances, vous devez générer des fichiers CSV à l'aide des classes *ResultCSV* et *DiversityCSV*.

### Classe ResultCSV

Cette classe génère un fichier .csv contenant les indicateurs de fitness et la durée d'exécution pour chaque exécution de l'algorithme génétique. Elle explore toutes les combinaisons possibles des paramètres suivants :

- Taux de mutation : entre 0,1 et 0,5
- Taux d'élitisme : entre 0,1 et 0,5
- Taille de la population : 50, 200, 350

- Pour l'utiliser, exécutez la méthode principale de la classe ResultCSV.

### Classe DiversityCSV

Cette classe stocke les paramètres additionnels toutes les 10 itérations pour étudier la diversité de la population au cours de l'exécution de l'algorithme. Les paramètres incluent :

- Taux de mutation : entre 0,1 et 0,5
- Taux d'élitisme : entre 0,1 et 0,5
- Taille de la population : fixée à 300

Pour l'utiliser, exécutez la méthode principale de la classe DiversityCSV.

### Exécution de l'algorithme sur une instance unique

Pour exécuter l'algorithme génétique sur une instance spécifique, lancez simplement le fichier main.java dans votre IDE.

Les résultats d'analyse détaillés sont disponibles dans le Google Colab associé. Consultez le notebook Google Colab pour une visualisation et une analyse approfondies des résultats.

## Auteur

- Lefèvre Jeanne-Emma
- Malet Paul
- Boursin Alice
