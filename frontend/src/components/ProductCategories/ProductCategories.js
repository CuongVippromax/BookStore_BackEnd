import React from 'react';
import './ProductCategories.css';

const ProductCategories = () => {
  const categories = [
    { id: 1, name: 'Sách Kinh Doanh', image: 'https://via.placeholder.com/150' },
    { id: 2, name: 'Sách Tâm Lý', image: 'https://via.placeholder.com/150' },
    { id: 3, name: 'Sách Kỹ Năng', image: 'https://via.placeholder.com/150' },
    { id: 4, name: 'Sách Văn Học', image: 'https://via.placeholder.com/150' },
    { id: 5, name: 'Sách Thiếu Nhi', image: 'https://via.placeholder.com/150' },
    { id: 6, name: 'Sách Giáo Khoa', image: 'https://via.placeholder.com/150' },
  ];

  return (
    <div className="product-categories">
      <div className="categories-header">
        <span className="grid-icon">⊞</span>
        <h2 className="categories-title">Danh mục sản phẩm</h2>
      </div>
      <div className="categories-grid">
        {categories.map(category => (
          <div key={category.id} className="category-item">
            <img src={category.image} alt={category.name} className="category-image" />
            <span className="category-name">{category.name}</span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProductCategories;



