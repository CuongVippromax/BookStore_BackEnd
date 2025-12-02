import React, { useRef } from 'react';
import ProductCard from '../ProductCard/ProductCard';
import './ProductCarousel.css';

const ProductCarousel = ({ products }) => {
  const carouselRef = useRef(null);

  const scrollLeft = () => {
    if (carouselRef.current) {
      carouselRef.current.scrollBy({ left: -300, behavior: 'smooth' });
    }
  };

  const scrollRight = () => {
    if (carouselRef.current) {
      carouselRef.current.scrollBy({ left: 300, behavior: 'smooth' });
    }
  };

  return (
    <div className="product-carousel-container">
      <div className="product-carousel" ref={carouselRef}>
        {products.map((product, index) => (
          <ProductCard key={index} product={product} />
        ))}
      </div>
      <button className="carousel-button carousel-button-right" onClick={scrollRight}>
        →
      </button>
    </div>
  );
};

export default ProductCarousel;



