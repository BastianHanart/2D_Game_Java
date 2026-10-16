# Game Design Document : Auralis

## 1. Vue d'ensemble projet : 

Genre : MetroidVania
Plateforme : PC (Steam)
Target Audience : MetroidVania Fan / Celeste & Hollow Knight & Hades Fan


### 1.1 Concept : 

Auralis est un jeu de type MetroidVania, c'est un jeu en 2D dans lequel le personnage devra se battre contre les 7 péchés capitaux.
En avancant dans le jeu, le joueur débloquera de nouvelles compétences relatives au péchés qu'il pourra faire upgrade en effectuant des tâches précises.
L'objectif final est de reconstruire la boite de pandore divisé en 7 joyaux detenus par les péchés capitaux. En battant les 7, la boite se débloquent et active le boss final.

### 1.2 Map

La Map représente la région d'Auralis, une map qui jouera à la fois sur la verticalité et l'horizontalité. A l'instar de Hollow Knight. 

Elle est crée via le logiciel LDtk, et est importée via la librairie LibGDX.

## 2 Gameplay Loop

Lors de la découverte d'une nouvelle zone, le lore nous est expliqué soit par des locaux, soit par un mystérieux personnages qui semble vouloir notre bien... Mais à quel prix ?
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

### 5.2 Zones

#### 5.2.1 Zone Envie

Une prairie et un village de marchand. La zone de boss est la forêt du village

#### 5.2.2 Zone Gourmandise

En sortant de la forêt, on tombe sur la capitale de la région, des marchands et des voyageurs du monde entiers passe par ce point stratégique. La zone du boss se trouve dans le plus grand batiment de la ville

#### 5.2.3 Zone Luxure

C'est la zone balnéaire juste à la sortie de la ville. La zone du boss se trouve dans un bâtiments où sont rassemblés toutes les personnes cherchant le plaisir charnel.

#### 5.2.4 Zone Avarice

La zone est tout simplement un casino géant à l'extrémité de la zone balnéaire. Le but est de monter tout en haut qui se trouve être le bureau de l'avarice.

#### 5.2.5 Zone Paresse

En sortant du casino et en continuant notre quête, vers le nord, on tombe sur une gigantesque montagne. Dans le village en bas de la montagne, nous sommes témoins d'un enlevement d'une jeune fille. Une vielle dame nous rejoin en nous disans de grimper au sommets pour aller chercher de l'aide. Il se trouve que l'aide en question est la paresse qu'il faudra soigner, cette zone de boss se fera complétement dans les nuages.

#### 5.2.6 Zone Colère

Après avoir sauver la soeur du paresse, nous nous retrouvons a devoir aider un marchand ambulant à traverser un plateau volcanique pas très loin de la montagne. En effet, cette zone est remplie de démon très puissant encore plus enragé à cause de la présence de colère dans les parages. La zone du boss se trouve dans un trou au milieu du plateau.

#### 5.2.7 Zone Orgueil

Et enfin la zone d'orgueil, C'est l'ancien château du démon originel. C'est une zone très hardu, des pièges et des démons légendaires nous y attendent. La zone de boss est la salle principal du château. Avec l'aura démoniaque de la boite de pandore s'y trouvant.

### 5.3 Combat de boss

Chaque boss aura, en plus de ses patternes propre à chacun, un combat unique avec des mécaniques de gameplay très variés. Pour battre un boss, il faut qu'il fasse une action qui va à l'encontre de son péché.
En général, lorsque dans les autres jeux, le boss est censé être vaincu car on lui a infligé assez de dégats, ici notre personnage parlera avec lui tout au long du combat, et surtout à la fin pour le raisonné.

#### 5.3.1 Envie

- C'est le premier combat, le terrain de combat sera donc très basique, une zone assez plate sans trop de verticalité. le boss sera néanmoins assez rude à battre. 

- Pour battre Envie, il faut qu'elle accepte d'être comme elle est, sans chercher à se comparer aux autres, à "se contenter de ce qu'elle a". 

Le sprite d'envie nous fait penser à une magneuse de serpent, celle-ci tenant un miroir, elle copie les compétences des ennemis, puis au final, les nôtres, pour tenter de nous vaincre. En terminant le combat en faisant parler nos personnages, notre personnage lui disant de se rapeller de sa force de samourai, qu'elle n'a pas besoin d'envier qui que ce soit car elle est déjà exceptionnelle comme elle est etc...

#### 5.3.2 Gourmandise

- On introduit l'idée que des éléments hors du boss peuvent nous attaquer pendant le combat. Un peu à la style de CupHead.

- Pour battre Gourmandise, il faut qu'elle partage ses vivres avec les habitants du village. Cette samourai était autre fois plein de bonté, mais en voyant de la noirceur dans le comportement de certains habitants qui ne pensent qu'à eux, elle ne voyais plus l'interêt de partager.

- Le but de notre personnage est de faire ouvrir les yeux sur la bonté des habitants du village qu'elle hante. Rappellant tous les actes que ceux-ci ont fait pour elle dans le passé. Le sprite de gourmandise se veut être une femme sensuel mais pas autant que la luxure, une femme plein de noblesse mais qui à tout de même des attraits de nourriture sans tomber dans l'aberration.

#### 5.3.3 Luxure

- La luxure doit servir de séparation entre le début et la fin du jeu. La difficulté monte d'un cran. Cette samourai avait fait le voeu de chasteté. Mais son désir était trop grand, à force de ces aventure, elle fut tenter par les plus beaux chevalier, combattant et ninja qu'elle pu croiser. L'envoutement par le roi des démons la fait passé le pas, et elle n'a jamais réussi à réchapper de ce désir.

- La luxure possède de véritable phases d'attaques. Jusqu'ici, chaque boss avait des phases d'attaques mais elles était plutôt anecdotiques. Ici elles ne le sont clairement pas. Elle va commencer par tenter d'user de son charme et de sa sensualité pour essayer de nous envouter. Ensuite, elle envoie des chevaliers sous son emprise et les contrôles comme des pantins. Et enfin sa phase final, Elle vrille totalement et va essayer de vous envouter par tous les moyens, des obstacles, des pièges des attaques qui vienne de tous les côtés. On revoit bien cette idée de cuphead ou le boss n'est plus réellement sur le terrain mais dans le décors avec énormémént de prétendant entrain de rire et de s'amuser. Le but du joueur est d'esquiver ces salves d'attaques incessante tout en attaquand certain éléments qui détruise petit à petit le maléfice qui envoutent la Luxure. La délivrant, enfin de ce mal qui l'a faisait perdre le total contrôle.

- Le sprite doit réellement faire comprendre au joueur en un éclair de quelle péchés il s'agit. Une sensualité débordante, des coeurs qui cachent l'essentielle, de la lingerie, un regard perçant et envoutant. Tout ce qui représente la luxure. 

#### 5.3.4 Avarice

- L'avarice. Ce boss se fera en plusieurs étapes. L'intrigue se passant dans son casino, notre but sera d'aller dans son bureau pour l'étape final, mais tous au long du parcours nous devrons nous battre contre des vigiles qui sont des minis Avarice. Quand ce samourai partait à l'aventure, il ne le faisait pas pour rien, ce trait de caractère s'est vu être démultiplié à cause du maléfice. Le but du personnage est de prouver à l'avarice que les biens matériels ne sont pas l'important dans la vie en lui parlant du samourai qui était sa femme et qui lui apportait le véritable bonheur, cette samourai qui était le péché de la gourmandise.

- Ici plusieurs étapes avec des phases différentes. Les premières phases de combats contre le vigiles se voient être des combat singulier, ou des combats avec éléments de casinos basiques comme un combat sur une roulette géante etc. mais le combat final mélange à la fois un combat contre des éléments hors décors et contre le boss. Celui-ci se démultipliant pour combattre de différente façon. 

- Le sprite de l'avarice est la parfaite représentation de l'argent et le luxe, une veste en poil blanc, au dessus d'un smoking noir. des lunettes de soleil en pointe, sur les yeux et des lunettes de soleil en étoile sur son frond, un sourire malicieux avec des dents en pointes. des bagues et bracelet en bijoux précieux. Tout doit renvoyer au luxe. 

#### 5.3.5 Paresse

- La paresse. La zone se passant sur une montagne, la paresse lui se trouve dans les nuages. Il s'ennuit et à tellement la flemme de se battre qu'il passera son temps au départ d'envoyer des autres combattant pour nous défaire, tout en soupirant envoyant des bourasques de vent. La deuxième partie est une escalade dangereuse parsemé de piège et d'attaques venant du ciel comme de la grêle, de la neige ou des éclairs venant du nuages de paresse. 

- Pour battre paresse, il faut réussir à nous faire nous écouter de lui parler de sa petite soeur qui est en bas de la vallée et qui est enfermé par des démons. Lui donner envie de la sauver va le séparer de son maléfice, le retransformer en samourai et te rejoindre afin d'aller vaincre cette horde au service du supposé déchu démon originel.

- Le sprite de paresse est à l'image du péchès. C'est un être avec des vetements emple qui flotte au vent. Des cheveux roux en batailles et un lance avec une forme de lune en guise d'arme.  


#### 5.3.6 Colère

- La colère est très sanguin. Il contrôle la zone à la perfection ce qui lui permet d'user du décor de lave et de roche en fusion pour nous attaquer. Il fait apparaître des golems pour nous contrer. Il a beau être colérique il reste fier, il se battra au sabre de toute ces forces. Ce combat est rude et il faudra faire preuve de tactique pour le vaincre. C'est surement le boss le plus dure à battre en combat singulier. 

- Pour battre colère, il faut appaiser son coeur. Avec l'aide d'un harmonica donner par la vieille dame au village de la montagne, le marchand ambulant réussi petit à petit à calmer la férocité du héros maudit.

- Le sprite de colère est synonyme de fierté, de rage. Il possède de sabre et est très grand. Des sourcis et des yeux couleurs sang. Des vêtements style sportifs avec pas mal de couche montrant une certaine noblesse. Il a garder une alure de général samourai malgré son maléfice, dû à sa fierté.

#### 5.3.7 Orgueuil

- Et enfin orgeuil. Prendre tout le monde de haut est sa spécialité. Il ne s'abaissera jamais à se battre directement avec vous. Il ferai apparaitre les anciens boss pour essayer de vous teracer. C'est copie certe moins puissante donnerons du fils à retordre à notre personnage. Quand il aura vu de quoi vous êtes capable, il acceptera de se battre plus ou moins contre vous. Mais sa puissance est ecrasante. Au bout d'un certain moment clé il s'approchera du personnage et le soulevera par le col. Cette action fera rentrer en raisonnance les 6 joyaux de notre personnage avec celui de Orgueil. Cette action aura, au vu de la proximité de nos deux protagoniste de la boite de pandore, la réactivé et brisé le sceau qui renfermais tous les pouvoirs du démon originel. 

- Le sprite d'orgueil ressemble à un roi avec une gigantesque cape. Une couleur de vêtements bleu-gris. Des vêtements royaux. Des cheveux gris en arrière, un regard perçant et menancant, une bouche sans émotions mais qui transgresse un dégout ou un air supérieur. Une bague de saphir. Un septre comme arme qui permet d'invoquer ses sorts, mais également le sabre royale pour la phase final.

### 5.3 Final

#### 5.3.1 Zone

La zone final sera rempli de monstre les plus puissant, rendant son accès et sa traversé véritablement compliqué. Les déplacement seront également complexes avec énormément de verticalité, de vide et de pièges. 

#### 5.3.2 Boss

Le boss final n'est personne d'autres que le petit personnage que nous rencontrions tout au long de l'aventure. Celui-ci était en fait le démon originel qui avait perdu tous ses pouvoirs au cours d'un combat contre les 7 samourais légendaires il y a une vingtaine d'années. Ces mêmes samourais qui, au fil du temps, maudit par le démon avant sa mort, les a transformé en ces 7 péchés capitaux. Lors du combat, on en apprend plus sur ce combat d'entant. Les 7 samourais venu pour nous aider ce batte contre les vassaux du démon originel pendant qu'on s'occupe de le vaincre. Mais plus le combat dure, plus le démons regagne de la force et ses pouvoirs, commençant petit à petit à nous écraser sous un niveau inégalable. C'est alors que quand il s'appretais à nous assainer le coup final, que les 7 samourais apparaisse et contre le coup. Dans un dernier élan héroique et cette fois pour de bon, les 7 samourais libérés de tout leur maléfice achève et terassent le démon ce qui fait disparaître tous ses maléfices, ses sbires et tous ce qui le concerne de la région d'Auralis. 

## 6 Idée 

- Celeste
- Hollow Knight
- Undertale
- Ghost & Goblin
- Fez
- Cuphead
- (Pokémon)