const carouselList = document.querySelector('.carousel-list');
const carouselItems = Array.from(carouselList.children);
const carouselPrev = document.querySelector('.carousel-prev');
const carouselNext = document.querySelector('.carousel-next');

let currentIndex = 0;

carouselPrev.addEventListener('click', () => {
    // Move to the previous item
    currentIndex = (currentIndex - 1 + carouselItems.length) % carouselItems.length;
    updateCarousel();
});

carouselNext.addEventListener('click', () => {
    // Move to the next item
    currentIndex = (currentIndex + 1) % carouselItems.length;
    updateCarousel();
});

function updateCarousel() {
    carouselItems.forEach((item, index) => {
        if (index === currentIndex) {
            // Show the current item
            item.style.display = 'block';
        } else if (index === (currentIndex + 3) % carouselItems.length) {
            // Show the next item
            item.style.display = 'block';
        } else {
            // Hide other items
            item.style.display = 'none';
        }
    });
}

updateCarousel();