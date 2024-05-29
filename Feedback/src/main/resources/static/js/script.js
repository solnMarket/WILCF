window.addEventListener("DOMContentLoaded", () => {
    const blockContainer = document.getElementById("blocks");
    const blockSize =  50;
    const screenWidth = window.innerWidth;
    const screenHeight = window.innerHeight;
    const numCols = Math.ceil(screenWidth / blockSize);
    const numRows = Math.ceil(screenHeight / blockSize);
    const numBlocks = numCols * numRows;

    function createBlocks () {
        for (let i = 0; i < numBlocks; i++){
            const block = document.createElement("div");
            block.classList.add("block");
            block.dataset.index = i;
            block.addEventListener("mousemove", highlightRandomNeighbors);
            blockContainer.appendChild(block);
        }
    }

    function highlightRandomNeighbors(){
        const index = parseInt(this.dataset.index);
        const neighbors = [
            index - 1,
            index + 1,
            index - numCols,
            index + numCols,
            index - numCols - 1,
            index - numCols + 1,
            index + numCols - 1,
            index + numCols + 1,
        ].filter(
            (i) =>
                i >= 0 &&
                i < numBlocks &&
                Math.abs((i % numCols) - (index  % numCols)) <= 1
        );

        this.classList.add("highlight");
        setTimeout(() => {
            this.classList.remove("highlight");
        }, 500);

        shuffleArray(neighbors)
            .slice(0, 1)
            .forEach((nIndex) => {
                const neighbor = blockContainer.children[nIndex];
                if (neighbor) {
                    neighbor.classList.add("highlight");
                    setTimeout(() => {
                        neighbor.classList.remove("highlight");
                    }, 500);
                }
            });
    }
    function shuffleArray(array) {
        for (let i = array.length - 1; i>0; i--) {
            const j = Math.floor(Math.random()  * (i+1));
            [array[i], array[j]] = [array[j], array[i]];
        }
        return array;
    }

    createBlocks();
});


window.addEventListener('scroll', function() {
    var nav = document.querySelector('.nav'); // Select the navigation bar
    if (window.scrollY > 150) { // Adjust the threshold as needed
      nav.classList.add('scrolled'); // Add the class "scrolled"
    } else {
      nav.classList.remove('scrolled'); // Remove the class "scrolled"
    }

  });

  document.addEventListener('DOMContentLoaded', function() {
    var links = document.querySelectorAll('nav ul.navbar-links a[href^="#"]');

    links.forEach(function(link) {
        link.addEventListener('click', function(e) {
            e.preventDefault();

            var targetId = this.getAttribute('href').substring(1);
            var targetElement = document.getElementById(targetId);
            var offset = 73;

            if (targetElement) {
                var elementPosition = targetElement.getBoundingClientRect().top + window.scrollY;
                var offsetPosition = elementPosition - offset;

                window.scrollTo({
                    top: offsetPosition,
                    behavior: 'smooth'
                });
            }
        });
    });
});


function validatePasswords() {
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirmPassword").value;
    if (password !== confirmPassword) {
        alert("Passwords do not match!");
        return false;
    }
    return true;
}
  