---

## 🚀 Installation et Exécution

### 1. Prérequis

* Installer **Java (JDK 17 ou supérieur)** sur la machine.
* Installer un serveur **MySQL ou MariaDB** (via WAMP, XAMPP ou Docker).
* Télécharger le **pilote JDBC MySQL** (`mysql-connector-j.jar`).

### 2. Préparation de la base de données

1. Ouvrir un outil d'administration de base de données (phpMyAdmin, DBeaver, DataGrip).
2. Créer une nouvelle base de données nommée `rapizz`.
3. Exécuter les scripts SQL dans cet ordre exact :
* `suppressions.sql` (nettoyage de l'environnement).
* `schema.sql` (création de la structure des tables).
* `insert.sql` (insertion du jeu de données de test).
* `triggers.sql` (automatisation de la règle de fidélité).
* `procedures.sql` (automatisation de la règle des retards).



### 3. Configuration du projet Java

1. Ouvrir le projet dans un environnement de développement (IntelliJ IDEA, Eclipse, VS Code).
2. Ajouter le pilote `mysql-connector-j.jar` aux dépendances du projet (Build Path).
3. Ouvrir le fichier `db.properties` situé dans le dossier `src/main/resources`.
4. Mettre à jour les informations de connexion avec les identifiants locaux :
```properties
db.url=jdbc:mysql://localhost:3306/rapizz
db.user=votre_utilisateur
db.password=votre_mot_de_passe

```



### 4. Lancement de l'application

1. Vérifier la présence d'une classe principale `Main.java` à la racine du projet.
2. Si elle n'existe pas, créer le fichier avec ce code :

```java
package com.example;

import com.example.views.MainWindow;

public class Main {
    public static void main(String[] args) {
        MainWindow app = new MainWindow();
        app.setVisible(true);
    }
}

```

3. Compiler le projet.
4. Exécuter la classe `Main`. L'interface graphique de gestion RaPizz va s'ouvrir à l'écran.
