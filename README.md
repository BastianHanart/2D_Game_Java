# Jeu Java : Auralis

## Présentation

Auralis est un jeu 2D de type metroidvania développé en Java avec le framework LibGDX. Le joueur explore des niveaux, affronte différents monstres, gagne de l'expérience et améliore son personnage grâce à un arbre de compétences.

Le projet comprend plusieurs systèmes de gameplay : déplacements et collisions, attaques, ennemis, personnages non joueurs, potions, progression, interface utilisateur, paramètres et sauvegardes.

Ce README décrit les fonctionnalités visibles dans les fichiers Java fournis.

## Fonctionnalités

### Joueur et déplacements

La classe `Character` gère le personnage contrôlé par le joueur, notamment :

- Les points de vie et les dégâts.
- Les déplacements horizontaux et le saut.
- Les animations de marche, de saut, d'attaque et de dash.
- La direction du personnage.
- Les attaques et leur zone de collision.
- Les potions de soin et de dégâts.
- Les statistiques améliorées par l'arbre de compétences.

La classe `Body` regroupe les éléments physiques du personnage, dont sa position, sa vitesse verticale, la gravité et son état au sol. Elle s'appuie sur `CollisionMap` pour gérer les déplacements et les collisions avec le niveau.

### Ennemis

La classe abstraite dans son rôle de base, `Monster`, regroupe les propriétés communes aux monstres : points de vie, récompense d'expérience, position, hitbox et état de mort.

Les types d'ennemis présents dans le code sont :

- `Squelette` : ennemi qui peut détecter le joueur, se déplacer vers lui et l'attaquer au corps à corps.
- `Fantome` : ennemi disposant de son propre comportement de mise à jour et de rendu.
- `Envie` : autre type de monstre avec sa propre logique de mise à jour et sa zone d'attaque.

Lorsqu'un monstre est vaincu, le jeu peut accorder de l'expérience et des potions au joueur.

### Niveaux et collisions

`Level` représente un niveau. Il contient notamment ses dimensions, sa musique, ses textures de fond et de couches, sa carte de collision, ses monstres, ses villageois et ses points d'apparition.

`LevelManager` est chargé de créer ou de charger le niveau correspondant au numéro demandé.

`CollisionMap` utilise une grille pour déterminer les zones solides et gérer les déplacements horizontaux, la recherche du sol et les limites horizontales.

`Platform` représente une plateforme rectangulaire avec une texture et une hitbox.

### Villageois

`Villageois` représente les personnages non joueurs présents dans les niveaux. La classe gère leur position, leur affichage et leurs collisions avec la carte. Elle utilise une feuille de sprites commune aux différentes variantes de villageois.

### Expérience et progression

`GameState` centralise plusieurs informations de progression, dont l'expérience, le niveau du joueur, le temps de jeu et l'arbre de compétences.

La barre d'expérience est affichée par `XPBar`. La progression de niveau peut entraîner le recalcul des statistiques du personnage.

### Arbre de compétences

`SkillTree` gère les points disponibles et les talents débloqués. Un talent peut être acheté si le joueur possède suffisamment de points et a débloqué ses éventuels prérequis.

Les talents sont définis dans l'énumération `Talent`. Les branches disponibles dans le code fourni permettent d'améliorer :

- Les points de vie maximum.
- Les dégâts.
- Le dash.
- Le double saut.

`SkillTreeScreen` affiche l'arbre de compétences et permet de consulter et de débloquer les talents.

### Potions et interface

Plusieurs classes assurent l'affichage des informations du joueur :

- `HealthBar` affiche les points de vie.
- `XPBar` affiche l'expérience et la progression vers le prochain niveau.
- `PotionHUD` affiche les potions disponibles et les informations associées.

Le personnage peut utiliser une potion de soin pour récupérer des points de vie et une potion de dégâts pour bénéficier temporairement d'une augmentation de ses dégâts.

### Menus et paramètres

`Main` initialise le jeu et son premier écran.

`MenuScreen` affiche le menu principal. Il permet d'accéder à la sélection des sauvegardes, aux paramètres ou de quitter le jeu.

`SaveSlotScreen` présente les emplacements de sauvegarde et permet de gérer les parties enregistrées.

`SettingsScreen` affiche les paramètres et permet notamment de modifier les touches ainsi que le mode d'affichage.

`Settings` centralise les touches par défaut et les fonctions de passage en plein écran ou en mode fenêtré.

### Sauvegardes

`SaveData` définit les informations enregistrées pour une partie, notamment :

- La version du format de sauvegarde.
- Le niveau actuel.
- La position du joueur.
- Ses points de vie.
- Le nombre de potions.
- Son niveau et son expérience.
- Les points et talents de l'arbre de compétences.
- Le temps de jeu et la date associée à la sauvegarde.

`SaveManager` gère la lecture, l'écriture, la suppression et l'application des sauvegardes. Le code prévoit cinq emplacements, stockés sous forme de fichiers JSON dans le dossier local `saves`.

## Commandes

Les touches par défaut définies dans `Settings` sont les suivantes :

| Action | Touche par défaut |
| --- | --- |
| Aller à gauche | A |
| Aller à droite | D |
| Sauter | W |
| Descendre | S |
| Courir | Maj gauche |
| Attaque principale | Clic gauche |
| Attaque secondaire | E |
| Dash | Espace |
| Potion de soin | H |
| Potion de dégâts | J |
| Menu | Échap |
| Arbre de compétences | M |

Le dash est lié au talent correspondant : il doit être débloqué avant de pouvoir être utilisé. Le double saut dépend également du talent correspondant.

Les paramètres permettent de modifier certaines commandes. Les touches indiquées ici correspondent aux valeurs par défaut trouvées dans le code.

## Organisation des classes

Les sources Java fournies utilisent le package `com.mygame`.

| Classe | Responsabilité principale |
| --- | --- |
| `Main` | Initialisation du jeu |
| `GameScreen` | Boucle de jeu, mise à jour des entités, rendu et caméra |
| `MenuScreen` | Menu principal |
| `SaveSlotScreen` | Sélection et gestion des emplacements de sauvegarde |
| `SettingsScreen` | Interface des paramètres |
| `Settings` | Touches par défaut et options d'affichage |
| `Character` | Joueur, statistiques, actions et animations |
| `Body` | Physique et déplacement d'un corps |
| `CollisionMap` | Détection et gestion des collisions avec la carte |
| `Monster` | Base commune aux monstres |
| `Squelette` | Comportement du squelette |
| `Fantome` | Comportement du fantôme |
| `Envie` | Comportement de l'ennemi Envie |
| `Villageois` | Personnages non joueurs |
| `Level` | Données et entités d'un niveau |
| `LevelManager` | Création ou chargement des niveaux |
| `Platform` | Plateforme et hitbox |
| `GameState` | État global et progression de la partie |
| `SkillTree` | Points de compétence et talents débloqués |
| `Talent` | Définition des talents et de leurs prérequis |
| `SkillTreeScreen` | Affichage de l'arbre de compétences |
| `HealthBar` | Barre de vie |
| `XPBar` | Barre d'expérience |
| `PotionHUD` | Interface des potions |
| `SaveData` | Structure des données de sauvegarde |
| `SaveManager` | Lecture et écriture des sauvegardes |

## Technologies

- **Java** : langage utilisé pour la logique du jeu.
- **LibGDX** : framework utilisé pour les écrans, les entrées, les textures, les animations, le rendu et la gestion du cycle de vie du jeu.
- **JSON** : format utilisé pour les fichiers de sauvegarde.

## Lancement du projet

Le ZIP fourni contient les fichiers sources Java, mais ne contient pas les fichiers de configuration du projet, tels que `build.gradle`, ni l'ensemble des ressources graphiques et audio nécessaires.

Il n'est donc pas possible de donner une commande de lancement fiable à partir de cette archive seule.

Pour lancer le jeu, il faut disposer du projet LibGDX complet, de sa configuration de compilation et des ressources utilisées par les classes Java. Les fichiers graphiques et audio sont chargés par leur nom, par exemple `MenuBackground.png`, `Personnage_Marche.png`, `Personnage_Jump.png`, `platform.png` ou les fichiers de musique associés aux niveaux. Ils doivent être placés dans le répertoire de ressources attendu par le projet.

## Limites de cette documentation

Ce README a été rédigé à partir des fichiers Java présents dans l'archive. Les fichiers de ressources, les paramètres de compilation et les éventuels documents de conception n'étaient pas inclus dans le ZIP. Les informations qui en dépendent, comme la procédure exacte de compilation, la liste exhaustive des niveaux et les caractéristiques visuelles de toutes les ressources, ne sont donc pas détaillées ici.
