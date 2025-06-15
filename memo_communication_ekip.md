## Exemple d'un cycle complet dans notre application (avec un ajout au panier)
---

### **Le Contexte : La Page Produit**

Imaginez que l'utilisateur est sur la page d'un T-Shirt. Cette page a été initialement générée par Spring et Thymeleaf. Votre navigateur a reçu une page HTML complète. Quelque part sur cette page, vous avez :

1.  **Le bouton "Ajouter au panier" :**
    ```html
    <!-- On utilise des attributs `data-*` pour passer l'ID du produit au JS -->
    <button class="btn btn-primary btn-add-to-cart" th:data-product-id="${produit.id}">
        Ajouter au panier
    </button>
    ```

2.  **L'icône du panier dans le header :**
    ```html
    <!-- On lui donne un ID pour que le JS puisse le trouver et le mettre à jour facilement -->
    <a href="/panier" class="nav-link">
        Panier <span id="cart-item-count" class="badge bg-secondary" th:text="${nombreArticlesPanier}">0</span>
    </a>
    ```
    Au chargement de la page, Thymeleaf a affiché `0` (ou le bon nombre si le panier n'était pas vide).

---

### **Le Flux d'Interaction : Du Clic à la Mise à Jour**

#### **Étape 1 : Le Clic (Frontend - JavaScript)**

L'utilisateur clique sur le bouton "Ajouter au panier".
Au lieu de soumettre un formulaire qui rechargerait toute la page, votre fichier JavaScript (`static/js/app.js`), chargé avec la page, intercepte cet événement.

```javascript
// static/js/app.js
$(document).ready(function() {
    // Intercepte le clic sur n'importe quel bouton avec la classe 'btn-add-to-cart'
    $('.btn-add-to-cart').on('click', function() {
        // 1. Récupérer l'ID du produit depuis l'attribut data-*
        const productId = $(this).data('productId');

        // 2. Préparer l'appel AJAX vers le backend
        $.ajax({
            type: "POST", // On modifie des données, donc on utilise POST
            url: "/api/panier/ajouter", // Un endpoint d'API dédié à cette action
            data: { // Les données envoyées au serveur
                produitId: productId
            },
            // NÉCESSAIRE pour Spring Security : inclure le jeton CSRF
            beforeSend: function(xhr) {
                const token = $("meta[name='_csrf']").attr("content");
                const header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            },
            // 5. Que faire quand l'appel réussit ?
            success: function(response) {
                // On met à jour le compteur du panier avec la nouvelle valeur renvoyée par le serveur
                $('#cart-item-count').text(response.newCartItemCount);
                
                // On peut aussi afficher une petite notification de succès
                alert('Produit ajouté au panier !');
            },
            // Que faire si le serveur renvoie une erreur ?
            error: function() {
                alert("Une erreur s'est produite. Veuillez réessayer.");
            }
        });
    });
});
```
*Note :* Pour que le CSRF fonctionne, vous devrez ajouter ces balises meta dans le `<head>` de vos pages HTML :
`<meta name="_csrf" th:content="${_csrf.token}"/>`
`<meta name="_csrf_header" th:content="${_csrf.headerName}"/>`

#### **Étape 2 : L'Appel au Backend (Spring Security & Controller)**

La requête AJAX `POST /api/panier/ajouter` arrive au serveur.

1.  **Vérification par Spring Security :** C'est la première barrière, et elle est **automatique**.
    *   Spring Security regarde le cookie `JSESSIONID` de la requête.
    *   Il vérifie si cet ID de session correspond à un utilisateur authentifié.
    *   Il vérifie que le jeton CSRF est valide.
    *   **Si l'utilisateur n'est pas connecté ou si le jeton CSRF est invalide, Spring Security rejette la requête immédiatement (avec une erreur 401 ou 403). La requête n'atteint MÊME PAS votre contrôleur.** C'est votre filet de sécurité.

2.  **Exécution par le Contrôleur :** Si la sécurité est passée, la requête arrive à votre méthode de contrôleur. Vous créerez un contrôleur dédié aux opérations d'API.

    ```java
    @RestController
    @RequestMapping("/api/panier")
    public class PanierApiController {

        @Autowired
        private PanierService panierService;

        @PostMapping("/ajouter")
        public ResponseEntity<?> ajouterAuPanier(@RequestParam Long produitId, Principal principal) {
            // "Principal" est injecté par Spring Security. Il représente l'utilisateur connecté.
            if (principal == null) {
                // Ne devrait pas arriver grâce à Spring Security, mais c'est une double sécurité.
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // `principal.getName()` renvoie généralement le nom d'utilisateur (notre email)
            String userEmail = principal.getName();
            
            // On délègue la logique métier au service
            panierService.ajouterProduit(userEmail, produitId);

            // 3. Obtenir le nouveau nombre d'articles pour le renvoyer au frontend
            int nouveauCompte = panierService.getNombreArticles(userEmail);
            
            // 4. Construire la réponse JSON
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("newCartItemCount", nouveauCompte);
            
            return ResponseEntity.ok(response);
        }
    }
    ```

#### **Étape 3 : La Réponse au Frontend (JSON)**

Le contrôleur ne renvoie pas de HTML. Il renvoie un objet **JSON** simple et efficace, qui ressemble à ceci :
`{ "success": true, "newCartItemCount": 1 }`

#### **Étape 4 : La Mise à Jour de la Vue (Retour au JavaScript)**

Retour dans le `success: function(response)` de notre appel AJAX :
*   Le `response` contient l'objet JSON envoyé par le serveur.
*   `response.newCartItemCount` vaut `1`.
*   La ligne `$('#cart-item-count').text(response.newCartItemCount);` trouve l'élément avec l'ID `cart-item-count` et change son contenu pour `1`.

**Résultat final :** L'utilisateur voit le chiffre sur l'icône du panier passer à 1, sans aucun rechargement de page. L'expérience est fluide, sécurisée et efficace. Vous avez "simulé" une SPA avec succès.