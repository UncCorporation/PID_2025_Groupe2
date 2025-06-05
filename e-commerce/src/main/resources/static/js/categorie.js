document.addEventListener("DOMContentLoaded", function() {
    getProducts();
});

const getProducts = () => {
    fetch('/api/produits')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(products => {
        console.log(`Number of products: ${products.length}`);
        products.forEach(product => {
            console.log(`Product title: ${product.nom}`);
        });
        const productList = document.getElementById('product-list');
        if (products.length === 0) {
            productList.innerHTML = '<p>No products found.</p>';
            return;
        }

        products.forEach((product, index) => {
            try {
                const cardContainer = document.createElement('div');
                cardContainer.className = 'col mb-5';
                const productCard = document.createElement('div');
                productCard.className = 'card h-100';
                const imageUrl = product.miniatureUrl ? product.miniatureUrl : (product.imagesVitrine && product.imagesVitrine.length > 0 ? product.imagesVitrine[0] : 'placeholder.jpg');
                const img = document.createElement('img');
                img.src = imageUrl;
                img.alt = `Image of ${product.nom}`;
                img.className = 'card-img-top'
                const cardBody = document.createElement('div');
                cardBody.className = 'card-body p-4';
                const textBody = document.createElement('div');
                textBody.className = 'text-center';

                const h5 = document.createElement('h5');
                h5.textContent = product.nom;

                const pDesc = document.createElement('p');
                pDesc.textContent = product.description;

                const pPrice = document.createElement('p');
                pPrice.className = 'price';
                pPrice.textContent = `${product.prix} €`;

                textBody.appendChild(h5);
                textBody.appendChild(pDesc);
                textBody.appendChild(pPrice);

                cardBody.appendChild(textBody);

                productCard.appendChild(img);
                productCard.appendChild(cardBody);

                cardContainer.appendChild(productCard);

                productList.appendChild(cardContainer);
            } catch (e) {
                console.error(`Erreur lors du traitement du produit à l'index ${index}:`, product, e);
            }
        });
    })
    .catch(error => {
        console.error('Error fetching products:', error);
        const productList = document.getElementById('product-list');
        productList.innerHTML = '<p>Error loading products. Please try again later.</p>';
    });
}