import React from 'react';
import './ProductCard.css';

const ProductCard = ({ product }) => {
  const {
    image,
    title,
    author,
    discountedPrice,
    originalPrice,
    discountPercent,
    soldCount,
    totalStock = 10
  } = product;

  const progressPercentage = (soldCount / totalStock) * 100;

  return (
    <div className="product-card">
      <div className="product-image-container">
        <img src={image} alt={title} className="product-image" />
        <div className="discount-badge">-{discountPercent}%</div>
      </div>
      <div className="product-info">
        <h3 className="product-title">{title}</h3>
        {author && <p className="product-author">{author}</p>}
        <div className="product-pricing">
          <span className="discounted-price">{discountedPrice.toLocaleString('vi-VN')} ₫</span>
          <span className="original-price">{originalPrice.toLocaleString('vi-VN')} ₫</span>
        </div>
        <div className="sales-progress">
          <div className="progress-bar">
            <div 
              className="progress-fill" 
              style={{ width: `${progressPercentage}%` }}
            ></div>
          </div>
          <span className="sold-text">Đã bán {soldCount}</span>
        </div>
      </div>
    </div>
  );
};

export default ProductCard;



