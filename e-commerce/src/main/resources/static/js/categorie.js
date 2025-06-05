document.addEventListener("DOMContentLoaded", function() {
    getCategories();
    getProducts();
    const allCatecories = document.getElementById('all-cat');
    allCatecories.addEventListener("click", function(){
      getProducts();
    });
});

const renderSingleProduct = (id) =>{
    console.log(`_______${id}_______`);
    fetch(`/api/produits/${id}`)
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(product => {
        console.log(product);
        const productList = document.getElementById('product-list');
        productList.replaceChildren();
        const cardContainer = document.createElement('div');
        cardContainer.className = 'pcard';
        const divf = document.createElement('div');
        divf.className = 'container-fliud';
        const wrapper = document.createElement('div');
        wrapper.className = 'wrapper row';
        const preview = document.createElement('div');
        preview.className = 'preview col-md-6';
        const previewpic = document.createElement('div');
        previewpic.className = 'preview-pic tab-content';
        const imageUrl = product.miniatureUrl ? product.miniatureUrl : (product.imagesVitrine && product.imagesVitrine.length > 0 ? product.imagesVitrine[0] : 'placeholder.jpg');
        const img = document.createElement('img');
        img.src = imageUrl;
        img.alt = `Image of ${product.nom}`;
        previewpic.appendChild(img);
        preview.appendChild(previewpic);

        const details = document.createElement('div');
        details.className = 'details col-md-6';
        const title = document.createElement('h3');
        title.className = 'product-title';
        title.textContent = product.nom;
        const description = document.createElement('p');
        description.className = 'product-description';
        description.textContent = product.description;
        const price = document.createElement('h5');
        price.className = 'price';
        price.textContent = `${product.prix} €`;
        details.appendChild(title);
        details.appendChild(description);
        details.appendChild(price);


        wrapper.appendChild(preview);
        wrapper.appendChild(details);
        divf.appendChild(wrapper);
        cardContainer.appendChild(divf);
        productList.appendChild(cardContainer);
    })
    .catch(error => {
        console.error('Error fetching product:', error);
    });


}

const renderCardProduct = (product) => {
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
    pDesc.style.display = 'none';
    pDesc.id = `desc-${product.id}`;

    const pPrice = document.createElement('p');
    pPrice.className = 'price';
    pPrice.textContent = `${product.prix} €`;

    textBody.appendChild(h5);
    textBody.appendChild(pPrice);
    textBody.appendChild(pDesc);

    cardBody.appendChild(textBody);

    productCard.appendChild(img);
    productCard.appendChild(cardBody);

    cardContainer.appendChild(productCard);
    cardContainer.addEventListener("mouseover", function(){
        const description = document.getElementById(`desc-${product.id}`);
        description.style.display = 'block';
    });
    cardContainer.addEventListener("mouseleave", function(){
        const description = document.getElementById(`desc-${product.id}`);
        description.style.display = 'none';
    });
    cardContainer.addEventListener("click", function(){
      renderSingleProduct(product.id);
    });
    return cardContainer;
}

const renderProductList = (products) => {
        const productList = document.getElementById('product-list');
        productList.replaceChildren();
        if (products.length === 0) {
            productList.innerHTML = '<p>No products found.</p>';
            return;
        }

        products.forEach((product, index) => {
            try {
                productList.appendChild(renderCardProduct(product));
            } catch (e) {
                console.error(`Erreur lors du traitement du produit à l'index ${index}:`, product, e);
            }
        });
}

const categorieListner = (id) => {
    console.log(`_______${id}_______`);
    fetch(`/api/produits/categorie/${id}`)
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
        renderProductList(products);
    })
    .catch(error => {
        console.error('Error fetching products:', error);
        const productList = document.getElementById('product-list');
        productList.innerHTML = '<p>Error loading products. Please try again later.</p>';
    });
}

const getCategories = () => {
    fetch('/api/categories')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(categories => {
        console.log(`Number of products: ${categories.length}`);
        categories.forEach(categorie => {
            console.log(`categorie title: ${categorie.nomCategorie}`);
        });
        const categoriesUl = document.getElementById('categorie-list');
        if (categories.length === 0) {
            return;
        }

        categories.forEach((categorie, index) => {
            try {
                const li = document.createElement('li');
                const link = document.createElement('a');
                link.className = 'dropdown-item';
                link.href = '#';
                link.innerHTML= categorie.nomCategorie;
                li.appendChild(link);
                li.addEventListener("click", function(){
                  categorieListner(categorie.id);
                });
                categoriesUl.appendChild(li);
            } catch (e) {
                console.error(`Erreur lors du traitement du categorie à l'index ${index}:`, product, e);
            }
        });
    })
    .catch(error => {
        console.error('Error fetching categories:', error);
    });
};

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
        renderProductList(products);
    })
    .catch(error => {
        console.error('Error fetching products:', error);
        const productList = document.getElementById('product-list');
        productList.innerHTML = '<p>Error loading products. Please try again later.</p>';
    });
}