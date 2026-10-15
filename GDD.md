# Game Design Document : Auralis

## 1. Vue d'ensemble projet : 

Genre : MetroidVania
Plateforme : PC (Steam)
Target Audience : MetroidVania Fan / Celeste & Hollow Knight & Hades Fan


### 1.1 Concept : 

Auralis est un jeu de type MetroidVania, c'est un jeu en 2D dans lequel le personnage devra se battre contre les 7 péchés capitaux.
En avancant dans le jeu, le joueur débloquera de nouvelles compétences relatives au péchés qu'il pourra faire upgrade en effectuant des tâches précises.
L'objectif final est de reconstruire la boite de pandore divisé en 7 joyaux detenus par les péchés capitaux. En battant les 7, la boite se débloquent et active le boss final.

## 2 Gameplay Loop

Lors de la découverte d'une nouvelle zone, le lore nous est expliqué. 
Pendant que nous nous déplacons dans la zone, le but est d'en apprendre davantage sur les quêtes, mais aussi d'XP afin de gagner de débloquer de nouvelles compétences.
Lorsqu'une quêtes nous est donnée, il faut aller l'effeectuer afin d'obtenir une récompence importante pour l'aventure.
Quand la dernière quête de la zone est effectué, la salle du boss nous est débloquer afin d'aller le défier.
Lorsque le boss est déchu, une nouvelle zone nous est ouvert, et une branche de l'arbre de l'arbre de compétences est déblocable.

## 3. Core Méchanique

### 3.1 Attaque

- Coup simple

- Coup vers le bas

- Coup vers le haut

- Coup chargé (Débloquer par Gourmandise)

### 3.2 Spell

- Dash (Débloquer par Envie)

- Double saut (Débloquer par Envie)

- Saut mural (Débloquer par Avarice)

- Soin immobile (Paresse)

- Active Buff Général temporaire (Débloquer par Orgueil)

### 3.3 Buff

- Buff de vie (Débloquer par Gourmandise)

- Buff de dégats (Débloquer par Colère)

- Buff de défense (Débloquer par Colère) -> Premier coup qui aurait dû te tuer te laisse à un point de vie

- Sangsue (Débloquer par Luxure)

### Achat

- Potions (soin, dégats ...)

- Amélioration carte

- Nourriture

- Amélioration arme -> Forge

## 4. System & RPG Progression

Comme dit dans le concept, le but du joueur est de battre les 7 péchés capitaux.

Dans l'ordre du jeu, les péchés apparaissent de cette manière : 

1. Envie
2. Gourmandise
3. Luxure
4. Avarice
5. Paresse
6. Colère
7. Orgueil

À chaque boss battu, le joueur obtiendra un des 7 joyaux nécessaires à la reconstruction de la boite de pandore. Chaque boss à des patternes relatif à son péché.
À l'ouverture de la boite de Pandore, on active la phase finale du jeu avec comme objectif de battre le boss final qui tourmante la région d'Auralis depuis des dizaines d'années.
Tout au long de sa quète, le joueur parcourera différentes zones avec des phases de gameplay variées.

## 5. Idées de Gameplay:

### 5.1 Expérience

#### 5.1.1 Obtention

Lorsqu'un ennemi est vaicu, le joueur reçois des points d'expérience. Selon l'ennemi et sa difficulté, la quantité d'expérience est différente.

#### 5.1.2 But

Le but de récupérer ces points d'expérience permette de débloquer des nouvelles compétences dans l'arbre de compétence. Ces compétences permette de débloquer l'accès à certaines zones ou tout simplement de diversifier le gameplay.

### 5.2 Combat de boss

Chaque boss aura, en plus de ses patternes propre à chacun, différents pour chacun. Pour battre un boss, il faut qu'il fasse une action qui va à l'encontre de son péché.

#### 5.2.1 Envie

- C'est le premier combat, le terrain de combat sera donc très basique, une zone assez plate sans trop de verticalité. le boss sera néanmoins assez rude à battre

- Pour battre Envie, il faut qu'il accepte d'être comme il est, sans chercher à se comparer aux autres, à se contenter de ce qu'il a.

#### 5.2.2 Gourmandise

- On introduit l'idée que des éléments hors du boss peuvent noys attaquer pendant le combat.

- Pour battre Gourmandise, il faut qu'il partage ses vivres avec les habitants du village.

#### 5.2.3 Luxure


#### 5.2.4 Avarice


#### 5.2.5 Paresse


#### 5.2.6 Colère


#### 5.2.7 Orgueil

### 5.3 Final

#### 5.3.1 Zone

La zone final sera rempli de monstre les plus puissant, rendant son accès et sa traversé véritablement compliqué. Les déplacement seront également complexes avec énormément de verticalité, de vide et de pièges. 

#### 5.3.2 Boss

Le boss final n'est personne d'autres que le petit personnage que nous rencontrions tout au long de l'aventure. Celui-ci était en fait le démon originel qui avait perdu tous ses pouvoirs au cours d'un combat contre les 7 samourais légendaires il y a des milliers d'années. Ces mêmes samourais qui, au fil du temps, maudit par le démon avant sa mort, les a transformé en ces 7 péchés capitaux. 

## 6. Map

La Map représente la région d'Auralis, une map qui jouera à la fois sur la verticalité et l'horizontalité. A l'instar de Hollow Knight. 

Elle est crée via le logiciel LDtk, et est importée via la librairie LibGDX.

## 7 Idée 

- Celeste

- Hollow Knight

- Undertale

- Ghost & Goblin

- Fez

- (Pokémon)