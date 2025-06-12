# README FILE pour le projet de PID 2025

### ❌ NE FAITES PAS DE PUSH DANS LE MAIN OU DEVELOP ! OUVREZ UNE PULL REQUEST SUR LA BRANCH DEVELOP MERCI ! ❌ 
---
## User Stories - Projet E-commerce
---
- ✅ Tâche terminée
- ❌ Tâche non terminée
---  
## Visiteur / Navigation 

### US1 - Affichage des catégories ✅
**En tant que** visiteur,  
**Je peux** voir la liste des catégories des produits sur la page d'accueil du site

### US2 - Navigation par catégorie ✅ 
**En tant que** visiteur,  
**Je peux** sélectionner une catégorie pour obtenir la liste des produits de cette catégorie

**Critères d'acceptation :**
- Les produits sont affichés sous forme d'images miniatures (thumbnails)
- Le nom du produit s'affiche sous la forme d'une infobulle associée à cette image
- Si aucun produit n'est associé à cette catégorie, un message en informe l'utilisateur
- Seule la catégorie sélectionnée est mise à jour, pas toute la page

### US3 - Détails des produits ✅
**En tant que** visiteur,  
**Je peux** sélectionner un produit pour obtenir ses détails

**Critères d'acceptation :**  
Les détails affichés sont :
- Nom du produit
- Nom de la marque
- Prix du produit
- Image du produit
- Seul le produit sélectionné est mis à jour, pas toute la page

## Gestion du compte

### US4 - Connexion ❌ 
**En tant que** visiteur,  
**Je peux** me connecter en indiquant mon email et mon mot de passe

**Critères d'acceptation :**
- L'adresse de courriel est utilisée comme login
- Les champs login et mot de passe sont obligatoires
- Un message indique un éventuel échec de l'authentification
- Une fois connecté, l'application affiche "Bienvenue, <prénom de l'membre>" sur toutes les pages

### US5 - Inscription ❌ 
**En tant que** visiteur anonyme,  
**Je peux** m'inscrire comme utilisateur enregistré

**Critères d'acceptation :**
- Inscription possible depuis n'importe quelle page
- Champs obligatoires : nom, prénom, adresse, code postal, ville, courriel et mot de passe
- Unicité du courriel
- Message d'erreur en cas d'échec
- Affichage "Bienvenue, <prénom du membre>" après connexion
- Conservation du panier si compte créé pendant l'achat

### US6 - Modification du profil ❌ 
**En tant que** membre,  
**Je peux** modifier mes informations personnelles

**Critères d'acceptation :**
- Accès au profil depuis n'importe quelle page
- Toutes les informations personnelles modifiables
- Unicité du courriel
- Message d'erreur en cas d'échec

### US7 - Déconnexion ❌ 
**En tant que** membre,  
**Je peux** me déconnecter pour redevenir visiteur

**Critères d'acceptation :**
- Déconnexion possible depuis n'importe quelle page

## Gestion du panier
### US8 - Affichage du panier
**En tant que** visiteur,  
**Je peux** afficher le contenu de mon panier d'achats

**Critères d'acceptation :**
- Accès au panier depuis n'importe quelle page
- Affichage par produit : nom, quantité, prix unitaire, prix total
- Affichage du prix total du panier
- Lien vers les détails des produits
- Message si panier vide

### US9 - Modification du panier
**En tant que** visiteur,  
**Je peux** modifier des produits dans mon panier

**Critères d'acceptation :**
- Bouton "Ajouter au panier" dans les détails produit
- Affichage du panier après ajout
- Incrémentation de la quantité si produit déjà présent

### US10 - Paiement
**En tant qu'** acheteur,  
**Je peux** payer mon panier avec ma technique de paiement préférée

**Méthodes de paiement acceptées :**
- PayPal
- Carte de crédit
- Carte de débit
- Virement bancaire
- Paysafe card

### US11 - Livraison
**En tant qu'** acheteur,  
**Je peux** me faire livrer mes achats par un service de livraison

## Administration
### US12 - Gestion des produits
**En tant que** gestionnaire,  
**Je peux** mettre à jour les produits

### US13 - Gestion des commandes
**En tant que** gestionnaire,  
**Je peux** voir et exploiter les commandes

### US14 - Statistiques
**En tant que** gestionnaire,  
**Je peux** voir et exploiter les différentes statistiques

### US15 - SEO
**En tant que** gestionnaire,  
**Je peux** optimiser le référencement de mon site

**Critères d'acceptation :**
- Gestion des mots-clés
- Ajout de descriptions complètes
- Modification des titres de produits

### US16 - Réseaux sociaux
**En tant que** gestionnaire,  
**Je peux** partager les informations de mon site sur les réseaux sociaux pour le référencement